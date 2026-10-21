package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.common.enums.ChannelDataType;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import com.genersoft.iot.vmp.gb28181.bean.Preset;
import com.genersoft.iot.vmp.gb28181.service.IDeviceChannelService;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.gb28181.service.IPTZService;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.gb28181.event.SipSubscribe;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.redisMsg.IRedisRpcPlayService;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class PTZServiceImpl implements IPTZService {


    @Autowired
    private SIPCommander cmder;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private IRedisRpcPlayService redisRpcPlayService;

    @Autowired
    private IDeviceChannelService deviceChannelService;

    @Autowired
    private IDeviceService deviceService;


    @Override
    public void ptz(Device device, String channelId, int cmdCode, int horizonSpeed, int verticalSpeed, int zoomSpeed) {
        try {
            cmder.frontEndCmd(device, channelId, cmdCode, horizonSpeed, verticalSpeed, zoomSpeed);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 云台控制: {}", e.getMessage());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "命令发送失败: " + e.getMessage());
        }
    }

    @Override
    public void frontEndCommand(Device device, String channelId, int cmdCode, int parameter1, int parameter2, int combindCode2) {
        // 判断设备是否属于当前平台, 如果不属于则发起自动调用
        if (!userSetting.getServerId().equals(device.getServerId())) {
            // 通道ID
            DeviceChannel deviceChannel = deviceChannelService.getOneForSource(device.getDeviceId(), channelId);
            Assert.notNull(deviceChannel, "通道不存在");
            String msg = redisRpcPlayService.frontEndCommand(device.getServerId(), deviceChannel.getId(), cmdCode, parameter1, parameter2, combindCode2);
            if (msg != null) {
                throw new ControllerException(ErrorCode.ERROR100.getCode(), msg);
            }
            return;
        }
        try {
            cmder.frontEndCmd(device, channelId, cmdCode, parameter1, parameter2, combindCode2);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 前端控制: {}", e.getMessage());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "命令发送失败: " + e.getMessage());
        }
    }

    @Override
    public void frontEndCommand(CommonGBChannel channel, Integer cmdCode, Integer parameter1, Integer parameter2, Integer combindCode2) {
        if (channel.getDataType() != ChannelDataType.GB28181) {
            // 只有国标通道的支持云台控制
            log.warn("[INFO 消息] 只有国标通道的支持云台控制， 通道ID： {}", channel.getGbId());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持");
        }
        Device device = deviceService.getDevice(channel.getDataDeviceId());
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到设备ID");
        }
        DeviceChannel deviceChannel = deviceChannelService.getOneForSourceById(channel.getGbId());
        frontEndCommand(device, deviceChannel.getDeviceId(), cmdCode, parameter1, parameter2, combindCode2);
    }

    @Override
    public void preset(Device device, String channelId, int cmdCode, int presetId, ErrorCallback<String> callback) {
        AtomicBoolean completed = new AtomicBoolean();
        ErrorCallback<String> once = (code, msg, data) -> {
            if (completed.compareAndSet(false, true)) {
                callback.run(code, msg, data);
            }
        };
        if (cmdCode < 0x81 || cmdCode > 0x83 || presetId < 1 || presetId > 255) {
            once.run(ErrorCode.ERROR400.getCode(), "预置位指令或编号无效，编号必须为1-255", null);
            return;
        }
        try {
            Assert.notNull(device, "设备不存在");
            DeviceChannel source = deviceChannelService.getOneForSource(device.getDeviceId(), channelId);
            Assert.notNull(source, "通道不存在");
            if (!userSetting.getServerId().equals(device.getServerId())) {
                redisRpcPlayService.preset(device.getServerId(), source.getId(), cmdCode, presetId, once);
                return;
            }
            // 云台控制只等待 SIP 200 OK，不等待 XML 执行结果或镜头到位通知。
            String command = SIPCommander.frontEndCmdString(cmdCode, 0, presetId, 0);
            cmder.fronEndCmd(device, source.getDeviceId(), command,
                    event -> {
                        boolean timeout = event.type == SipSubscribe.EventResultType.timeout;
                        once.run(timeout ? Response.REQUEST_TIMEOUT : (event.statusCode > 0 ? event.statusCode : ErrorCode.ERROR100.getCode()),
                                timeout ? "预置位指令应答超时，未确认设备接收，请刷新设备状态后重试" : "预置位指令失败：" + event.msg, null);
                    },
                    event -> {
                        if (event.statusCode == Response.OK) {
                            once.run(ErrorCode.SUCCESS.getCode(), "设备已接收预置位指令", null);
                        } else {
                            once.run(event.statusCode > 0 ? event.statusCode : ErrorCode.ERROR100.getCode(),
                                    "预置位指令未收到 SIP 200 OK：" + event.msg, null);
                        }
                    });
        } catch (Exception e) {
            log.error("[预置位指令失败] {}", e.getMessage());
            once.run(ErrorCode.ERROR100.getCode(), "预置位指令发送失败：" + e.getMessage(), null);
        }
    }

    @Override
    public void preset(CommonGBChannel channel, int cmdCode, int presetId, ErrorCallback<String> callback) {
        if (channel == null || channel.getDataType() == null || channel.getDataType() != ChannelDataType.GB28181) {
            callback.run(ErrorCode.ERROR400.getCode(), "通道不支持国标预置位控制", null);
            return;
        }
        Device device = deviceService.getDevice(channel.getDataDeviceId());
        DeviceChannel source = deviceChannelService.getOneForSourceById(channel.getGbId());
        if (device == null || source == null) {
            callback.run(ErrorCode.ERROR404.getCode(), "设备或原始通道不存在", null);
            return;
        }
        preset(device, source.getDeviceId(), cmdCode, presetId, callback);
    }

    @Override
    public void dragZoomIn(CommonGBChannel channel, int length, int width, int midPointX, int midPointY, int lengthX, int lengthY) {
        if (channel.getDataType() != ChannelDataType.GB28181) {
            log.warn("[INFO 消息] 只有国标通道的支持云台控制， 通道ID： {}", channel.getGbId());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持");
        }
        Device device = deviceService.getDevice(channel.getDataDeviceId());
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到设备ID");
        }
        DeviceChannel deviceChannel = deviceChannelService.getOneForSourceById(channel.getGbId());
        deviceService.dragZoomIn(device, deviceChannel.getDeviceId(), length, width, midPointX, midPointY, lengthX, lengthY);
    }

    @Override
    public void dragZoomOut(CommonGBChannel channel, int length, int width, int midPointX, int midPointY, int lengthX, int lengthY) {
        if (channel.getDataType() != ChannelDataType.GB28181) {
            log.warn("[INFO 消息] 只有国标通道的支持云台控制， 通道ID： {}", channel.getGbId());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持");
        }
        Device device = deviceService.getDevice(channel.getDataDeviceId());
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到设备ID");
        }
        DeviceChannel deviceChannel = deviceChannelService.getOneForSourceById(channel.getGbId());
        deviceService.dragZoomOut(device, deviceChannel.getDeviceId(), length, width, midPointX, midPointY, lengthX, lengthY);
    }

    @Override
    public void homePosition(CommonGBChannel channel, Boolean enabled, Integer resetTime, Integer presetIndex, ErrorCallback<String> callback) {
        if (channel.getDataType() != ChannelDataType.GB28181) {
            log.warn("[INFO 消息] 只有国标通道支持看守位，通道ID：{}", channel.getGbId());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持");
        }
        Device device = deviceService.getDevice(channel.getDataDeviceId());
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到设备");
        }
        DeviceChannel deviceChannel = deviceChannelService.getOneForSourceById(channel.getGbId());
        if (deviceChannel == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到通道");
        }
        deviceService.homePosition(device, deviceChannel.getDeviceId(), enabled, resetTime, presetIndex, callback);
    }

    @Override
    public void queryPresetList(CommonGBChannel channel, ErrorCallback<List<Preset>> callback) {
        if (channel.getDataType() != ChannelDataType.GB28181) {
            // 只有国标通道的支持云台控制
            log.warn("[INFO 消息] 只有国标通道的支持云台控制， 通道ID： {}", channel.getGbId());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持");
        }
        Device device = deviceService.getDevice(channel.getDataDeviceId());
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到设备");
        }
        DeviceChannel deviceChannel = deviceChannelService.getOneForSourceById(channel.getGbId());
        if (deviceChannel == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未找到通道");
        }
        deviceService.queryPreset(device, deviceChannel.getDeviceId(), callback);
    }


}
