package com.genersoft.iot.vmp.gb28181.service.impl;

import com.alibaba.fastjson2.JSON;
import com.genersoft.iot.vmp.common.*;
import com.genersoft.iot.vmp.common.enums.MediaStreamUtil;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.dao.DeviceChannelMapper;
import com.genersoft.iot.vmp.gb28181.dao.DeviceMapper;
import com.genersoft.iot.vmp.gb28181.service.IInviteStreamService;
import com.genersoft.iot.vmp.media.event.media.MediaDepartureEvent;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.bean.InviteErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class InviteStreamServiceImpl implements IInviteStreamService {

    private final Map<String, PendingInvite> inviteErrorCallbackMap = new ConcurrentHashMap<>();

    private static class PendingInvite {
        private final long createdNanos;
        private final long timeoutNanos;
        private List<ErrorCallback<StreamInfo>> callbacks = new ArrayList<>();
        private volatile boolean completed;
        private boolean successful;

        private PendingInvite(long createdNanos, long timeoutNanos) {
            this.createdNanos = createdNanos;
            this.timeoutNanos = timeoutNanos;
        }

        private boolean expired(long now) {
            return now - createdNanos >= timeoutNanos;
        }
    }

    // 使用单调时钟，避免系统校时使在途请求永久不超时。
    long nanoTime() {
        return System.nanoTime();
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceChannelMapper deviceChannelMapper;

    /**
     * 流离开的处理
     */
    @Async
    @EventListener
    public void onApplicationEvent(MediaDepartureEvent event) {
        if ("rtsp".equals(event.getSchema()) && MediaStreamUtil.isGB28181(event.getApp(), event.getStream())) {
            InviteInfo inviteInfo = getInviteInfoByStream(null, event.getStream());
            if (inviteInfo != null && (inviteInfo.getType() == InviteSessionType.PLAY || inviteInfo.getType() == InviteSessionType.PLAYBACK)) {
                try {
                    removeInviteInfo(inviteInfo);
                    Device device = deviceMapper.getDeviceByDeviceId(inviteInfo.getDeviceId());
                    if (device != null) {
                        deviceChannelMapper.stopPlayById(inviteInfo.getChannelId());
                    }
                } catch (Exception e) {
                    log.error("[流离开] 清理Invite异常: deviceId={}, channelId={}, stream={}",
                            inviteInfo.getDeviceId(), inviteInfo.getChannelId(), inviteInfo.getStream(), e);
                }
            }
        }
    }

    @Override
    public void updateInviteInfo(InviteInfo inviteInfo) {
        if (InviteSessionStatus.ready == inviteInfo.getStatus()) {
            updateInviteInfo(inviteInfo, Long.valueOf(userSetting.getPlayTimeout()) * 2);
        } else {
            updateInviteInfo(inviteInfo, null);
        }
    }

    @Override
    public void updateInviteInfo(InviteInfo inviteInfo, Long time) {
        if (inviteInfo == null || (inviteInfo.getDeviceId() == null || inviteInfo.getChannelId() == null)) {
            log.warn("[更新Invite信息]，参数不全： {}", JSON.toJSON(inviteInfo));
            return;
        }
        InviteInfo inviteInfoForUpdate;

        if (InviteSessionStatus.ready == inviteInfo.getStatus()) {
            if (inviteInfo.getDeviceId() == null || inviteInfo.getChannelId() == null
                    || inviteInfo.getType() == null || inviteInfo.getStream() == null
            ) {
                return;
            }
            inviteInfoForUpdate = inviteInfo;
        } else {
            InviteInfo inviteInfoInRedis = getInviteInfo(inviteInfo.getType(), inviteInfo.getChannelId(), inviteInfo.getStream());
            if (inviteInfoInRedis == null) {
                log.warn("[更新Invite信息]，未从缓存中读取到Invite信息： deviceId: {}, channel: {}, stream: {}",
                        inviteInfo.getDeviceId(), inviteInfo.getChannelId(), inviteInfo.getStream());
                return;
            }
            if (inviteInfo.getStreamInfo() != null) {
                inviteInfoInRedis.setStreamInfo(inviteInfo.getStreamInfo());
            }
            if (inviteInfo.getSsrcInfo() != null) {
                inviteInfoInRedis.setSsrcInfo(inviteInfo.getSsrcInfo());
            }
            if (inviteInfo.getStreamMode() != null) {
                inviteInfoInRedis.setStreamMode(inviteInfo.getStreamMode());
            }
            if (inviteInfo.getReceiveIp() != null) {
                inviteInfoInRedis.setReceiveIp(inviteInfo.getReceiveIp());
            }
            if (inviteInfo.getReceivePort() != null) {
                inviteInfoInRedis.setReceivePort(inviteInfo.getReceivePort());
            }
            if (inviteInfo.getStatus() != null) {
                inviteInfoInRedis.setStatus(inviteInfo.getStatus());
            }

            inviteInfoForUpdate = inviteInfoInRedis;

        }
        if (inviteInfoForUpdate.getCreateTime() == null) {
            inviteInfoForUpdate.setCreateTime(System.currentTimeMillis());
        }
        String key = VideoManagerConstants.INVITE_PREFIX;
        String objectKey = inviteInfoForUpdate.getType() +
                ":" + inviteInfoForUpdate.getChannelId() +
                ":" + inviteInfoForUpdate.getStream();
        if (time != null && time > 0) {
            inviteInfoForUpdate.setExpirationTime(time);
        }
        redisTemplate.opsForHash().put(key, objectKey, inviteInfoForUpdate);
    }

    @Override
    public InviteInfo updateInviteInfoForStream(InviteInfo inviteInfo, String stream) {

        InviteInfo inviteInfoInDb = getInviteInfo(inviteInfo.getType(), inviteInfo.getChannelId(), inviteInfo.getStream());
        if (inviteInfoInDb == null) {
            return null;
        }
        removeInviteInfo(inviteInfoInDb);
        String key = VideoManagerConstants.INVITE_PREFIX;
        String objectKey = inviteInfo.getType() +
                ":" + inviteInfo.getChannelId() +
                ":" + stream;
        inviteInfoInDb.setStream(stream);
        if (inviteInfoInDb.getSsrcInfo() != null) {
            inviteInfoInDb.getSsrcInfo().setStream(stream);
        }
        if (InviteSessionStatus.ready == inviteInfo.getStatus()) {
            inviteInfoInDb.setExpirationTime((long) (userSetting.getPlayTimeout() * 2));
        }
        if (inviteInfoInDb.getCreateTime() == null) {
            inviteInfoInDb.setCreateTime(System.currentTimeMillis());
        }
        redisTemplate.opsForHash().put(key, objectKey, inviteInfoInDb);
        return inviteInfoInDb;
    }

    @Override
    public InviteInfo getInviteInfo(InviteSessionType type, Integer channelId, String stream) {
        String key = VideoManagerConstants.INVITE_PREFIX;
        String keyPattern = (type != null ? type : "*") +
                ":" + (channelId != null ? channelId : "*") +
                ":" + (stream != null ? stream : "*");
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(20).build();
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options)) {
            if (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                cursor.close();
                if (entry.getValue() instanceof InviteInfo) {
                    return (InviteInfo) entry.getValue();
                } else {
                    log.warn("[Redis-InviteInfo] 发现脏数据并清理: key={}, value={}", entry.getKey(), entry.getValue());
                    redisTemplate.opsForHash().delete(key, entry.getKey());
                }
            }
        } catch (Exception e) {
            log.error("[Redis-InviteInfo] 查询异常: ", e);
        }
        return null;
    }

    @Override
    public List<InviteInfo> getAllInviteInfo() {
        List<InviteInfo> result = new ArrayList<>();
        String key = VideoManagerConstants.INVITE_PREFIX;
        List<Object> values = redisTemplate.opsForHash().values(key);
        if(values.isEmpty()) {
            return result;
        }
        for (Object value : values) {
            result.add((InviteInfo)value);
        }
        return result;
    }

    @Override
    public InviteInfo getInviteInfoByDeviceAndChannel(InviteSessionType type, Integer channelId) {
        return getInviteInfo(type, channelId, null);
    }

    @Override
    public InviteInfo getInviteInfoByStream(InviteSessionType type, String stream) {
        return getInviteInfo(type, null, stream);
    }

    @Override
    public void removeInviteInfo(InviteSessionType type, Integer channelId, String stream) {
        String key = VideoManagerConstants.INVITE_PREFIX;
        if (type == null && channelId == null && stream == null) {
            redisTemplate.delete(key);
            return;
        }
        String keyPattern = (type != null ? type : "*") + ":" + (channelId != null ? channelId : "*") + ":" + (stream != null ? stream : "*");
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(20).build();
        List<Object> objectKeys = new ArrayList<>();
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options)) {
            while (cursor.hasNext()) {
                objectKeys.add(cursor.next().getKey());
            }
        } catch (Exception e) {
            log.error("[Redis-InviteInfo] 删除异常: ", e);
        }
        if (!objectKeys.isEmpty()) {
            redisTemplate.opsForHash().delete(key, objectKeys.toArray());
        }
    }

    @Override
    public void removeInviteInfoByDeviceAndChannel(InviteSessionType inviteSessionType, Integer channelId) {
        removeInviteInfo(inviteSessionType, channelId, null);
    }

    @Override
    public void removeInviteInfo(InviteInfo inviteInfo) {
        removeInviteInfo(inviteInfo.getType(), inviteInfo.getChannelId(), inviteInfo.getStream());
    }

    @Override
    public void once(InviteSessionType type, Integer channelId, String stream, ErrorCallback<StreamInfo> callback) {
        onceAndFirst(type, channelId, stream, callback);
    }

    @Override
    public boolean onceAndFirst(InviteSessionType type, Integer channelId, String stream, ErrorCallback<StreamInfo> callback) {
        return register(type, channelId, stream, callback).isFirst();
    }

    @Override
    public InviteRequest register(InviteSessionType type, Integer channelId, String stream, ErrorCallback<StreamInfo> callback) {
        String key = buildKey(type, channelId, stream);
        AtomicBoolean first = new AtomicBoolean(false);
        List<PendingInvite> expired = new ArrayList<>(1);
        PendingInvite registered = inviteErrorCallbackMap.compute(key, (k, pending) -> {
            if (pending == null || pending.completed || pending.expired(nanoTime())) {
                if (pending != null && !pending.completed) {
                    expired.add(pending);
                }
                // 与 ready Invite 的兜底有效期一致；正常收流超时仍由 RTP 层在 playTimeout 处理。
                // 新加入的等待者不能延长发起者的有效期。
                pending = new PendingInvite(nanoTime(),
                        TimeUnit.MILLISECONDS.toNanos(Math.max(1L, userSetting.getPlayTimeout()) * 2));
                first.set(true);
            }
            if (callback != null) {
                pending.callbacks.add(callback);
            }
            return pending;
        });
        expired.forEach(pending -> notifyTimeout(key, pending));
        return new InviteRequest() {
            @Override
            public boolean isFirst() {
                return first.get();
            }

            @Override
            public boolean isPending() {
                return isCurrent() && !registered.completed;
            }

            @Override
            public boolean isCurrent() {
                return inviteErrorCallbackMap.get(key) == registered && (!registered.completed || registered.successful);
            }

            @Override
            public void run(int code, String msg, StreamInfo data) {
                complete(key, registered, code, msg, data);
            }
        };
    }

    private void notifyTimeout(String key, PendingInvite pending) {
        log.warn("[点播等待超时] 清理残留在途请求，key: {}, 等待时长: {}ms, 回调数: {}",
                key, TimeUnit.NANOSECONDS.toMillis(nanoTime() - pending.createdNanos), pending.callbacks.size());
        notifyCallbacks(key, pending.callbacks, InviteErrorCode.ERROR_FOR_STREAM_TIMEOUT.getCode(),
                "点播等待超时，已释放在途请求，请重试", null);
    }

    private void notifyCallbacks(String key, List<ErrorCallback<StreamInfo>> callbacks, int code, String msg, StreamInfo data) {
        if (callbacks == null) {
            return;
        }
        for (ErrorCallback<StreamInfo> callback : callbacks) {
            try {
                callback.run(code, msg, data);
            } catch (Exception e) {
                // 一个 HTTP/RPC 等待者失败不能中断其他回调，也不能中断调用方的资源清理。
                log.error("[点播回调异常] key: {}, code: {}", key, code, e);
            }
        }
    }

    void expirePendingInvites() {
        inviteErrorCallbackMap.forEach((key, pending) -> {
            if (pending.expired(nanoTime()) && inviteErrorCallbackMap.remove(key, pending)) {
                if (!pending.completed) {
                    notifyTimeout(key, pending);
                }
            }
        });
    }

    private String buildKey(InviteSessionType type, Integer channelId, String stream) {
        String key = type + ":" + channelId;
        // 如果ssrc未null那么可以实现一个通道只能一次操作，ssrc不为null则可以支持一个通道多次invite
        if (stream != null) {
            key += (":" + stream);
        }
        return key;
    }


    @Override
    public void clearInviteInfo(String deviceId) {
        List<InviteInfo> inviteInfoList = getAllInviteInfo();
        for (InviteInfo inviteInfo : inviteInfoList) {
            if (inviteInfo.getDeviceId().equals(deviceId)) {
                removeInviteInfo(inviteInfo);
            }
        }
    }

    @Override
    public int getStreamInfoCount(String mediaServerId) {
        int count = 0;
        String key = VideoManagerConstants.INVITE_PREFIX;
        List<Object> values = redisTemplate.opsForHash().values(key);
        if (values.isEmpty()) {
            return count;
        }
        for (Object value : values) {
            InviteInfo inviteInfo = (InviteInfo)value;
            if (inviteInfo != null
                    && inviteInfo.getStreamInfo() != null
                    && inviteInfo.getStreamInfo().getMediaServer() != null
                    && inviteInfo.getStreamInfo().getMediaServer().getId().equals(mediaServerId)) {
                if (inviteInfo.getType().equals(InviteSessionType.DOWNLOAD) && inviteInfo.getStreamInfo().getProgress() == 1) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    @Override
    public void call(InviteSessionType type, Integer channelId, String stream, int code, String msg, StreamInfo data) {
        String key = buildKey(type, channelId, stream);
        complete(key, null, code, msg, data);
    }

    private void complete(String key, PendingInvite expected, int code, String msg, StreamInfo data) {
        AtomicReference<List<ErrorCallback<StreamInfo>>> callbacks = new AtomicReference<>();
        inviteErrorCallbackMap.computeIfPresent(key, (k, pending) -> {
            if ((expected == null || pending == expected) && !pending.completed) {
                pending.successful = code == InviteErrorCode.SUCCESS.getCode();
                pending.completed = true;
                callbacks.set(pending.callbacks);
                pending.callbacks = Collections.emptyList();
            }
            // 短暂保留轮次标识，允许先收流、后收到 SIP 200；回调引用立即释放。
            // 新请求可立即替换已完成的轮次，定时任务按原期限清理标识。
            return pending;
        });
        notifyCallbacks(key, callbacks.get(), code, msg, data);
    }

    @Override
    public void cancel(InviteSessionType type, Integer channelId, String stream) {
        String key = buildKey(type, channelId, type == InviteSessionType.PLAY ? null : stream);
        inviteErrorCallbackMap.forEach((pendingKey, pending) -> {
            if ((pendingKey.equals(key) || (stream == null && pendingKey.startsWith(key + ":")))
                    && inviteErrorCallbackMap.remove(pendingKey, pending)) {
                notifyCallbacks(pendingKey, pending.callbacks, InviteErrorCode.ERROR_FOR_FINISH.getCode(), "点播已停止", null);
            }
        });
    }

    @Override
    public InviteInfo getInviteInfoBySSRC(String ssrc) {
        List<InviteInfo> inviteInfoList = getAllInviteInfo();
        if (inviteInfoList.isEmpty()) {
            return null;
        }
        for (InviteInfo inviteInfo : inviteInfoList) {
            if (inviteInfo.getSsrcInfo() != null && ssrc.equals(inviteInfo.getSsrcInfo().getSsrc())) {
                return inviteInfo;
            }
        }
        return null;
    }

    @Override
    public InviteInfo updateInviteInfoForSSRC(InviteInfo inviteInfo, String ssrc) {
        InviteInfo inviteInfoInDb = getInviteInfo(inviteInfo.getType(), inviteInfo.getChannelId(), inviteInfo.getStream());
        if (inviteInfoInDb == null) {
            return null;
        }
        removeInviteInfo(inviteInfoInDb);
        String key = VideoManagerConstants.INVITE_PREFIX;
        String objectKey = inviteInfo.getType() +
                ":" + inviteInfo.getChannelId() +
                ":" + inviteInfo.getStream();
        if (inviteInfoInDb.getSsrcInfo() != null) {
            inviteInfoInDb.getSsrcInfo().setSsrc(ssrc);
        }
        redisTemplate.opsForHash().put(key, objectKey, inviteInfoInDb);
        return inviteInfoInDb;
    }

    @Scheduled(fixedRate = 10000)   //定时检测,清理错误的redis数据,防止因为错误数据导致的点播不可用
    public void execute(){
        // 必须独立于 Redis 清理：Redis 已无 Invite 时，进程内仍可能残留在途标记。
        expirePendingInvites();
        String key = VideoManagerConstants.INVITE_PREFIX;
        if(redisTemplate.opsForHash().size(key) == 0) {
            return;
        }
        List<Object> values = redisTemplate.opsForHash().values(key);
        for (Object value : values) {
            try {
                InviteInfo inviteInfo = (InviteInfo)value;
                if (inviteInfo.getStreamInfo() != null) {
                    continue;
                }
                if (inviteInfo.getCreateTime() == null || inviteInfo.getExpirationTime() == null) {
                    removeInviteInfo(inviteInfo);
                    continue;
                }
                long time = inviteInfo.getCreateTime() + inviteInfo.getExpirationTime();
                if (System.currentTimeMillis() > time) {
                    removeInviteInfo(inviteInfo);
                }
            } catch (Exception e) {
                log.error("[定时清理Invite] 处理异常，尝试删除该数据: {}", value, e);
                removeInviteInfoByValue(value);
            }
        }
    }

    private void removeInviteInfoByValue(Object value) {
        if (value instanceof InviteInfo) {
            removeInviteInfo((InviteInfo) value);
        } else {
            String key = VideoManagerConstants.INVITE_PREFIX;
            redisTemplate.opsForHash().entries(key).forEach((k, v) -> {
                if (v.equals(value)) {
                    redisTemplate.opsForHash().delete(key, k);
                }
            });
        }
    }
}
