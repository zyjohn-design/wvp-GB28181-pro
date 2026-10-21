package com.genersoft.iot.vmp.gb28181.task.deviceSubscribe;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.bean.SipTransactionInfo;
import com.genersoft.iot.vmp.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SubscribeTaskRunner{

    private final Map<String, SubscribeTask> subscribes = new ConcurrentHashMap<>();

    private final DelayQueue<SubscribeTask> delayQueue = new DelayQueue<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserSetting userSetting;

    private final String prefix = "VMP_DEVICE_SUBSCRIBE";

    // 订阅过期检查
    @Scheduled(fixedDelay = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void expirationCheck(){
        SubscribeTask take;
        while ((take = delayQueue.poll()) != null) {
            try {
                if (subscribes.get(take.getKey()) == take) {
                    removeSubscribe(take.getKey());
                    take.expired();
                }
            } catch (Exception e) {
                log.error("[设备订阅到期] {} 到期处理时出现异常，设备编号: {}", take.getName(), take.getDeviceId(), e);
            }
        }
    }

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public void addSubscribe(SubscribeTask task) {
        long remainingMillis = task.getDelayTime() - currentTimeMillis();
        if (remainingMillis <= 0) {
            log.debug("[设备订阅恢复] 跳过已过期任务，key: {}", task.getKey());
            return;
        }
        String key = String.format("%s_%s_%s", prefix, userSetting.getServerId(), task.getKey());
        // 订阅提前500ms续期，剩余不足1秒仍是有效任务，不能截断成Redis不接受的0秒TTL。
        redisTemplate.opsForValue().set(key, task.getInfo(), Duration.ofMillis(remainingMillis));
        SubscribeTask previous = subscribes.put(task.getKey(), task);
        if (previous != null) {
            delayQueue.remove(previous);
        }
        delayQueue.offer(task);
    }

    public boolean removeSubscribe(String key) {
        SubscribeTask task = subscribes.get(key);
        if (task == null) {
            return false;
        }
        String redisKey = String.format("%s_%s_%s", prefix, userSetting.getServerId(), task.getKey());
        redisTemplate.delete(redisKey);
        subscribes.remove(key);
        if (delayQueue.contains(task)) {
            boolean remove = delayQueue.remove(task);
            if (!remove) {
                log.info("[移除订阅任务] 从延时队列内移除失败： {}", key);
            }
        }
        return true;
    }

    public SipTransactionInfo getTransactionInfo(String key) {
        SubscribeTask task = subscribes.get(key);
        if (task == null) {
            return null;
        }
        return task.getTransactionInfo();
    }

    public boolean updateDelay(String key, long expirationTime) {
        SubscribeTask task = subscribes.get(key);
        if (task == null) {
            return false;
        }
        long remainingMillis = expirationTime - currentTimeMillis();
        if (remainingMillis <= 0) {
            removeSubscribe(key);
            return false;
        }
        log.info("[更新订阅任务时间] {}, 编号： {}", task.getName(), key);
        String redisKey = String.format("%s_%s_%s", prefix, userSetting.getServerId(), task.getKey());
        redisTemplate.expire(redisKey, Duration.ofMillis(remainingMillis));
        delayQueue.remove(task);
        task.setDelayTime(expirationTime);
        delayQueue.offer(task);
        return true;
    }

    public boolean containsKey(String key) {
        return subscribes.containsKey(key);
    }

    public List<SubscribeTaskInfo> getAllTaskInfo(){
        String scanKey = String.format("%s_%s_*", prefix, userSetting.getServerId());
        List<Object> values = RedisUtil.scan(redisTemplate, scanKey);
        if (values.isEmpty()) {
            return new ArrayList<>();
        }
        List<SubscribeTaskInfo> result = new ArrayList<>();
        for (Object value : values) {
            String redisKey = (String)value;
            Object cached = redisTemplate.opsForValue().get(redisKey);
            if (!(cached instanceof SubscribeTaskInfo taskInfo)) {
                continue;
            }
            Long expire = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
            if (expire == null || expire <= 0) {
                // SCAN后键可能已过期；永久键也不能作为有效订阅恢复，交由订阅丢失检查重新发起。
                log.debug("[设备订阅恢复] 跳过无有效剩余期限的记录，key: {}, ttl: {}", redisKey, expire);
                continue;
            }
            taskInfo.setExpireTime(expire);
            result.add(taskInfo);
        }
        return result;

    }
}
