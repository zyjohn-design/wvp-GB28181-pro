package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.gb28181.bean.*;
import com.genersoft.iot.vmp.gb28181.service.IDeviceChannelService;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DevicePresetQueryService {
    private final IDeviceService devices;
    private final IDeviceChannelService channels;
    private final PresetRemarkService remarks;
    private final TaskExecutor taskExecutor;

    public DevicePresetQueryService(IDeviceService devices, IDeviceChannelService channels,
                                    PresetRemarkService remarks, TaskExecutor taskExecutor) {
        this.devices = devices;
        this.channels = channels;
        this.remarks = remarks;
        this.taskExecutor = taskExecutor;
    }

    public DeferredResult<WVPResult<List<ChannelPresets>>> query(Device device, int timeout) {
        DeferredResult<WVPResult<List<ChannelPresets>>> result = new DeferredResult<>(timeout * 1000L);
        List<ChannelPresets> matches = new ArrayList<>();
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeout);
        result.onTimeout(() -> {
            synchronized (matches) {
                result.setResult(new WVPResult<>(408, "设备预置位查询超时，结果不完整，请勿作为全量同步依据", List.copyOf(matches)));
            }
        });
        try {
            taskExecutor.execute(() -> queryChannels(device, deadline, result, matches));
        } catch (RuntimeException exception) {
            result.setResult(WVPResult.fail(100, "预置位查询任务繁忙，请稍后重试"));
        }
        return result;
    }

    private void queryChannels(Device device, long deadline,
                               DeferredResult<WVPResult<List<ChannelPresets>>> result, List<ChannelPresets> matches) {
        try {
            List<DeviceChannel> sources = channels.queryChaneListByDeviceId(device.getDeviceId()).stream()
                    .filter(channel -> channel.getChannelType() == DeviceChannel.RESOURCE_CHANNEL).toList();
            int failed = 0;
            int completed = 0;
            // 同一请求逐个查询，避免同时向设备下发大量 SIP 指令。
            for (DeviceChannel channel : sources) {
                if (result.isSetOrExpired() || System.nanoTime() >= deadline) break;
                try {
                    CompletableFuture<WVPResult<List<Preset>>> response = new CompletableFuture<>();
                    devices.queryPreset(device, channel.getDeviceId(), (code, msg, data) ->
                            response.complete(new WVPResult<>(code, msg, data)));
                    long remaining = deadline - System.nanoTime();
                    if (remaining <= 0) break;
                    WVPResult<List<Preset>> presets = response.get(Math.min(TimeUnit.SECONDS.toNanos(5), remaining),
                            TimeUnit.NANOSECONDS);
                    if (presets.getCode() != 0 || presets.getData() == null) {
                        failed++;
                    } else if (!presets.getData().isEmpty()) {
                        List<Preset> named = remarks.apply(channel.getId(), presets.getData());
                        synchronized (matches) {
                            matches.add(new ChannelPresets(channel.getDeviceId(), channel.getName(), named));
                        }
                    }
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception exception) {
                    failed++;
                    log.warn("[设备预置位查询] 通道查询失败，设备: {}, 通道: {}", device.getDeviceId(), channel.getDeviceId(), exception);
                }
                completed++;
            }
            int incomplete = failed + sources.size() - completed;
            synchronized (matches) {
                result.setResult(new WVPResult<>(incomplete == 0 ? 0 : 100,
                        incomplete == 0 ? "成功" : "有 " + incomplete + " 个通道查询失败或未完成，结果不完整，请勿作为全量同步依据",
                        List.copyOf(matches)));
            }
        } catch (Exception exception) {
            log.error("[设备预置位查询] 查询失败，设备: {}", device.getDeviceId(), exception);
            synchronized (matches) {
                result.setResult(new WVPResult<>(100, "设备预置位查询失败，结果不完整", List.copyOf(matches)));
            }
        }
    }
}
