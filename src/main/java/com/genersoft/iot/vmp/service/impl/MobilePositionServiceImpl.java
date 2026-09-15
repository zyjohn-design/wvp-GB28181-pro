package com.genersoft.iot.vmp.service.impl;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.bean.MobilePosition;
import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.dao.MobilePositionMapper;
import com.genersoft.iot.vmp.gb28181.dao.PlatformMapper;
import com.genersoft.iot.vmp.gb28181.event.subscribe.mobilePosition.MobilePositionEvent;
import com.genersoft.iot.vmp.gb28181.service.IPlatformChannelService;
import com.genersoft.iot.vmp.gb28181.service.ISourceOtherService;
import com.genersoft.iot.vmp.service.IMobilePositionService;
import com.genersoft.iot.vmp.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class MobilePositionServiceImpl implements IMobilePositionService {

    private BlockingQueue<MobilePosition> mobilePositionQueue;

    private final Map<String, ISourceOtherService> sourceOtherServiceMap;

    private final MobilePositionMapper mobilePositionMapper;

    private final IPlatformChannelService platformChannelService;

    private final PlatformMapper platformMapper;

    private final UserSetting userSetting;

    private final TaskExecutor taskExecutor;

    @PostConstruct
    public void init() {
        mobilePositionQueue = new LinkedBlockingQueue<>(Math.max(1_000, userSetting.getMaxNotifyCountQueue()));
    }

    /**
     * 查询移动位置轨迹
     */
    @Override
    public List<MobilePosition> queryMobilePositions(Integer channelId, String startTime, String endTime) {
        Long startTimestamp = null;
        Long endTimestamp = null;
        if (startTime != null) {
            startTimestamp = DateUtil.yyyy_MM_dd_HH_mm_ssToTimestampMs(startTime);
        }
        if (endTime != null) {
            endTimestamp = DateUtil.yyyy_MM_dd_HH_mm_ssToTimestampMs(endTime);
        }
        return mobilePositionMapper.queryPositionByDeviceIdAndTime(channelId, startTimestamp, endTimestamp);
    }

    @Override
    public List<Platform> queryEnablePlatformListWithAsMessageChannel() {
        return platformMapper.queryEnablePlatformListWithAsMessageChannel();
    }

    /**
     * 查询最新移动位置
     */
    @Override
    public MobilePosition queryLatestPosition(Integer channelId) {
        return mobilePositionMapper.queryLatestPosition(channelId);
    }

    @Async
    @EventListener
    public void onApplicationEvent(MobilePositionEvent event) {
        if (event.getMobilePositionList() == null || event.getMobilePositionList().isEmpty()) {
            return;
        }
        if (event.getMobilePositionList().get(0).getChannelId() != null) {
            enqueuePositions(event.getMobilePositionList());
            return;
        }
        for (ISourceOtherService sourceOtherService : sourceOtherServiceMap.values()) {
            try {
                // 此时已经完成了通道ID的添加，以及坐标系的转换，后续只需要将数据保存到数据库即可
                Boolean addResult = sourceOtherService.addChannelIdForMobilePosition(event.getMobilePositionList());
                if (addResult != null && addResult) {
                    enqueuePositions(event.getMobilePositionList());
                }
            }catch (Exception e) {
                log.error("[移动位置事件] 处理移动位置事件失败", e);
            }
        }
    }

    @Scheduled(fixedDelay = 500)
    public void executeMobilePositionQueue() {
        if (mobilePositionQueue.isEmpty()) {
            return;
        }
        List<MobilePosition> handlerCatchDataList = new ArrayList<>();
        mobilePositionQueue.drainTo(handlerCatchDataList, 5_000);
        if (handlerCatchDataList.isEmpty()) {
            return;
        }
        List<MobilePosition> mobilePositionList = handlerCatchDataList.stream().filter(
                mobilePosition -> mobilePosition.getChannelId() != null && mobilePosition.getChannelId() != 0).toList();
        // 发送通知，方便国标级联转发给上级
        taskExecutor.execute(() -> platformChannelService.notifyMobilePosition(mobilePositionList));

        // 批量保存到数据库
        int batchSize = 1000;
        for (int i = 0; i < mobilePositionList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, mobilePositionList.size());
            List<MobilePosition> batchList = mobilePositionList.subList(i, end);
            mobilePositionMapper.batchAdd(batchList);
        }
    }

    private void enqueuePositions(List<? extends MobilePosition> positions) {
        for (MobilePosition position : positions) {
            if (!mobilePositionQueue.offer(position)) {
                log.warn("移动位置待处理队列已满，丢弃后续数据，channelId={}", position.getChannelId());
                break;
            }
        }
    }

}
