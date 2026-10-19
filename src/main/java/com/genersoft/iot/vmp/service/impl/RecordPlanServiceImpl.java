package com.genersoft.iot.vmp.service.impl;

import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.dao.CommonGBChannelMapper;
import com.genersoft.iot.vmp.gb28181.service.IGbChannelPlayService;
import com.genersoft.iot.vmp.media.bean.MediaInfo;
import com.genersoft.iot.vmp.media.event.media.MediaDepartureEvent;
import com.genersoft.iot.vmp.media.service.IMediaServerService;
import com.genersoft.iot.vmp.service.IRecordPlanService;
import com.genersoft.iot.vmp.service.bean.InviteErrorCode;
import com.genersoft.iot.vmp.service.bean.RecordPlan;
import com.genersoft.iot.vmp.service.bean.RecordPlanItem;
import com.genersoft.iot.vmp.storager.dao.RecordPlanMapper;
import com.genersoft.iot.vmp.utils.DateUtil;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RecordPlanServiceImpl implements IRecordPlanService {

    @Autowired
    private RecordPlanMapper recordPlanMapper;

    @Autowired
    private CommonGBChannelMapper channelMapper;

    @Autowired
    private IGbChannelPlayService channelPlayService;

    @Autowired
    private IMediaServerService mediaServerService;



    // 定时任务、接口调用和异步媒体回调会同时访问。
    final Map<Integer, StreamInfo> recordStreamMap = new ConcurrentHashMap<>();
    private final Set<Integer> startingChannels = ConcurrentHashMap.newKeySet();

    /** 流断开后重新检查当前计划，防止已结束的任务被重新拉起。 */
    @Async
    @EventListener
    public synchronized void onApplicationEvent(MediaDepartureEvent event) {
        Integer channelId = recording(event.getApp(), event.getStream());
        if (channelId == null) return;
        StreamInfo info = recordStreamMap.get(channelId);
        if (event.getMediaServer() != null && info.getMediaServer() != null
                && !event.getMediaServer().getId().equals(info.getMediaServer().getId())) return;
        recordStreamMap.remove(channelId);
        execution();
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public synchronized void execution() {
        Set<Integer> required = new HashSet<>(queryCurrentChannelRecord());
        Set<Integer> stopped = new HashSet<>(recordStreamMap.keySet());
        stopped.removeAll(required);
        stopStreams(stopped, recordStreamMap);
        required.removeAll(recordStreamMap.keySet());
        required.removeAll(startingChannels);
        if (required.isEmpty()) return;
        for (CommonGBChannel channel : channelMapper.queryByIds(new ArrayList<>(required))) {
            Integer channelId = channel.getGbId();
            if (!startingChannels.add(channelId)) continue;
            try {
                channelPlayService.play(channel, null, true, (code, msg, streamInfo) -> {
                    synchronized (RecordPlanServiceImpl.this) {
                        try {
                            if (code != InviteErrorCode.SUCCESS.getCode() || streamInfo == null) {
                                log.warn("[录制计划] 点播失败，下次调度重试，通道ID: {}，原因: {}", channelId, msg);
                                return;
                            }
                            // 异步点播完成时，计划可能已经被删除、修改或解除关联。
                            if (!queryCurrentChannelRecord().contains(channelId)) {
                                recordStreamMap.put(channelId, streamInfo);
                                stopStreams(Collections.singleton(channelId), recordStreamMap);
                                return;
                            }
                            // 复用已有直播/推流时，也必须显式开启录像。
                            if (mediaServerService.startRecord(streamInfo.getMediaServer(), streamInfo.getApp(), streamInfo.getStream())) {
                                recordStreamMap.put(channelId, streamInfo);
                                log.info("[录制计划] 开始录像，通道ID: {}", channelId);
                            } else {
                                log.warn("[录制计划] 开启录像失败，请检查媒体节点是否支持动态MP4录像，下次调度重试，通道ID: {}，节点类型: {}",
                                        channelId, streamInfo.getMediaServer().getType());
                            }
                        } catch (Exception e) {
                            log.error("[录制计划] 开启录像异常，通道ID: {}", channelId, e);
                        } finally {
                            startingChannels.remove(channelId);
                        }
                    }
                });
            } catch (Exception e) {
                startingChannels.remove(channelId);
                log.error("[录制计划] 点播异常，下次调度重试，通道ID: {}", channelId, e);
            }
        }
    }

    /**
     * 获取当前时间段应该录像的通道Id列表
     */
    private List<Integer> queryCurrentChannelRecord(){
        // 前端和数据库均使用当天分钟数，区间为[start, stop)。
        LocalDateTime now = LocalDateTime.now();
        int week = now.getDayOfWeek().getValue();
        int index = now.getHour() * 60 + now.getMinute();

        // 查询现在需要录像的通道Id
        return recordPlanMapper.queryRecordIng(week, index);
    }

    private void stopStreams(Collection<Integer> channelIds, Map<Integer, StreamInfo> streams) {
        for (Integer channelId : channelIds) {
            StreamInfo streamInfo = streams.get(channelId);
            if (streamInfo == null) continue;
            try {
                // 先停止MP4，仍有观众时保留直播；失败时保留状态供下次调度重试。
                if (!mediaServerService.stopRecord(streamInfo.getMediaServer(), streamInfo.getApp(), streamInfo.getStream())) {
                    log.warn("[录制计划] 停止录像失败，下次调度重试，通道ID: {}", channelId);
                    continue;
                }
                recordStreamMap.remove(channelId, streamInfo);
                MediaInfo mediaInfo = mediaServerService.getMediaInfo(streamInfo.getMediaServer(), streamInfo.getApp(), streamInfo.getStream());
                if (mediaInfo != null && Integer.valueOf(0).equals(mediaInfo.getReaderCount())) {
                    mediaServerService.closeStreams(streamInfo.getMediaServer(), streamInfo.getApp(), streamInfo.getStream());
                }
                log.info("[录制计划] 停止录像，通道ID: {}", channelId);
            } catch (Exception e) {
                log.error("[录制计划] 停止时异常，通道ID: {}", channelId, e);
            }
        }
    }

    @Override
    public Integer recording(String app, String stream) {
        for (Map.Entry<Integer, StreamInfo> entry : recordStreamMap.entrySet()) {
            Integer channelId = entry.getKey();
            StreamInfo streamInfo = entry.getValue();
            if (streamInfo != null && streamInfo.getApp().equals(app) && streamInfo.getStream().equals(stream)) {
                return channelId;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void add(RecordPlan plan) {
        validatePlan(plan);
        if (plan.getSnap() == null) plan.setSnap(false);
        plan.setCreateTime(DateUtil.getNow());
        plan.setUpdateTime(DateUtil.getNow());
        recordPlanMapper.add(plan);
        if (plan.getId() > 0 && !plan.getPlanItemList().isEmpty()) {
            for (RecordPlanItem recordPlanItem : plan.getPlanItemList()) {
                recordPlanItem.setPlanId(plan.getId());
            }
            recordPlanMapper.batchAddItem(plan.getId(), plan.getPlanItemList());
        }
        refreshAfterCommit();
    }

    @Override
    public RecordPlan get(Integer planId) {
        RecordPlan recordPlan = recordPlanMapper.get(planId);
        if (recordPlan == null) {
            return null;
        }
        List<RecordPlanItem> recordPlanItemList = recordPlanMapper.getItemList(planId);
        recordPlan.setPlanItemList(recordPlanItemList);
        return recordPlan;
    }

    @Override
    @Transactional
    public void update(RecordPlan plan) {
        validatePlan(plan);
        RecordPlan existing = requirePlan(plan.getId());
        if (plan.getSnap() == null) plan.setSnap(existing.getSnap());
        plan.setUpdateTime(DateUtil.getNow());
        recordPlanMapper.update(plan);
        recordPlanMapper.cleanItems(plan.getId());
        recordPlanMapper.batchAddItem(plan.getId(), plan.getPlanItemList());
        refreshAfterCommit();
    }

    @Override
    @Transactional
    public void delete(Integer planId) {
        RecordPlan recordPlan = recordPlanMapper.get(planId);
        if (recordPlan == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "录制计划不存在");
        }
        // 清理关联的通道
        channelMapper.removeRecordPlanByPlanId(recordPlan.getId());
        recordPlanMapper.cleanItems(planId);
        recordPlanMapper.delete(planId);
        refreshAfterCommit();
    }

    @Override
    public PageInfo<RecordPlan> query(Integer page, Integer count, String query) {
        PageHelper.startPage(page, count);
        if (query != null) {
            query = query.replaceAll("/", "//")
                    .replaceAll("%", "/%")
                    .replaceAll("_", "/_");
        }
        List<RecordPlan> all = recordPlanMapper.query(query);
        return new PageInfo<>(all);
    }

    @Override
    public void link(List<Integer> channelIds, Integer planId) {
        if (channelIds == null || channelIds.isEmpty()) {
            log.info("[录制计划] 关联/移除关联时, 通道编号必须存在");
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "通道编号必须存在");
        }
        if (planId == null) {
            channelMapper.removeRecordPlan(channelIds);
        }else {
            requirePlan(planId);
            channelMapper.addRecordPlan(channelIds, planId);
        }
        // 查看当前的待录制列表是否变化,如果变化,则调用录制计划马上开始录制
        refreshAfterCommit();
    }

    @Override
    public PageInfo<CommonGBChannel> queryChannelList(int page, int count, String query, Integer dataType, Boolean online, Integer planId, Boolean hasLink) {
        PageHelper.startPage(page, count);
        if (query != null) {
            query = query.replaceAll("/", "//")
                    .replaceAll("%", "/%")
                    .replaceAll("_", "/_");
        }
        List<CommonGBChannel> all = channelMapper.queryForRecordPlanForWebList(planId, query, dataType, online, hasLink);
        return new PageInfo<>(all);
    }

    @Override
    public void linkAll(Integer planId) {
        requirePlan(planId);
        channelMapper.addRecordPlanForAll(planId);
        refreshAfterCommit();
    }

    @Override
    public void cleanAll(Integer planId) {
        requirePlan(planId);
        channelMapper.removeRecordPlanByPlanId(planId);
        refreshAfterCommit();
    }

    private RecordPlan requirePlan(Integer planId) {
        RecordPlan plan = planId == null || planId <= 0 ? null : recordPlanMapper.get(planId);
        if (plan == null) throw new ControllerException(ErrorCode.ERROR100.getCode(), "录制计划不存在");
        return plan;
    }

    private void validatePlan(RecordPlan plan) {
        if (plan == null || plan.getName() == null || plan.getName().trim().isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "录制计划名称不能为空");
        }
        plan.setName(plan.getName().trim());
        if (plan.getName().length() > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "录制计划名称不能超过255个字符");
        }
        if (plan.getPlanItemList() == null || plan.getPlanItemList().isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "请至少选择一个录制时间段");
        }
        for (RecordPlanItem item : plan.getPlanItemList()) {
            if (item == null || item.getWeekDay() == null || item.getWeekDay() < 1 || item.getWeekDay() > 7
                    || item.getStart() == null || item.getStop() == null || item.getStart() < 0
                    || item.getStop() > 1440 || item.getStart() >= item.getStop()) {
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "录制时间段无效：星期须为1-7，分钟须满足0≤开始<结束≤1440");
            }
        }
    }

    private void refreshAfterCommit() {
        Runnable refresh = () -> {
            try {
                execution();
            } catch (Exception e) {
                log.error("[录制计划] 刷新任务失败，下次调度重试", e);
            }
        };
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    refresh.run();
                }
            });
        } else {
            refresh.run();
        }
    }
}
