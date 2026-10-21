package com.genersoft.iot.vmp.web.gb28181;

import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.gb28181.service.IPTZService;
import com.genersoft.iot.vmp.gb28181.service.impl.PresetRemarkService;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import org.springframework.web.context.request.async.DeferredResult;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import java.text.ParseException;

/**
 * API兼容：设备控制
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/control")
@Hidden
public class ApiControlController {

    @Autowired
    private SIPCommander cmder;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IPTZService ptzService;

    @Autowired
    private PresetRemarkService presetRemarkService;

    /**
     * 设备控制 - 云台控制
     * @param serial 设备编号
     * @param command 控制指令 允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop
     * @param channel 通道序号
     * @param code 通道编号
     * @param speed 速度(0~255) 默认值: 129
     */
    @GetMapping(value = "/ptz")
    private void ptz(String serial,String command,
                            @RequestParam(required = false)Integer channel,
                            @RequestParam(required = false)String code,
                            @RequestParam(required = false)Integer speed){

        if (log.isDebugEnabled()) {
            log.debug("模拟接口> 设备云台控制 API调用，deviceId：{} ，channelId：{} ，command：{} ，speed：{} ",
                    serial, code, command, speed);
        }
        if (channel == null) {channel = 0;}
        if (speed == null) {speed = 0;}
        Device device = deviceService.getDeviceByDeviceId(serial);
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "device[ " + serial + " ]未找到");
        }
        int cmdCode = -1;
        switch (command){
            case "left":
                cmdCode = 2;
                break;
            case "right":
                cmdCode = 1;
                break;
            case "up":
                cmdCode = 8;
                break;
            case "down":
                cmdCode = 4;
                break;
            case "upleft":
                cmdCode = 10;
                break;
            case "upright":
                cmdCode = 9;
                break;
            case "downleft":
                cmdCode = 6;
                break;
            case "downright":
                cmdCode = 5;
                break;
            case "zoomin":
                cmdCode = 16;
                break;
            case "zoomout":
                cmdCode = 32;
                break;
            case "stop":
                cmdCode = 0;
                break;
            default:
                break;
        }
        if (cmdCode == -1) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "未识别的指令：" + command);
        }
        // 默认值 50
        try {
            cmder.frontEndCmd(device, code, cmdCode, speed, speed, speed);
        } catch (SipException | InvalidArgumentException | ParseException e) {
            log.error("[命令发送失败] 云台控制: {}", e.getMessage());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "命令发送失败: " + e.getMessage());
        }
    }

    /**
     * 设备控制 - 预置位控制
     * @param serial 设备编号
     * @param code 通道编号,通过 /api/v1/device/channellist 获取的 ChannelList.ID, 必须指定此参数以确定目标通道
     * @param channel 保留兼容参数，当前需通过 code 指定目标通道
     * @param command 控制指令 允许值: set, goto, remove
     * @param preset 预置位编号(1~255)
     * @param name 平台名称备注，不填默认预置点+编号；不下发设备
     */
    @GetMapping(value = "/preset")
    public DeferredResult<WVPResult<String>> preset(String serial,String command,
                            @RequestParam(required = false)Integer channel,
                            @RequestParam(required = false)String code,
                            @RequestParam(required = false)String name,
                            @RequestParam(required = false)Integer preset){

        if (log.isDebugEnabled()) {
            log.debug("模拟接口> 预置位控制 API调用，deviceId：{} ，channelId：{} ，command：{} ，name：{} ，preset：{} ",
                    serial, code, command, name, preset);
        }

        if (preset == null || preset < 1 || preset > 255) {
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "预置位编号必须为1-255之间的整数");
        }
        if (code == null || code.isBlank()) {
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "请通过code指定目标通道国标编号");
        }
        int cmdCode;
        if ("set".equals(command)) {
            cmdCode = 0x81;
        } else if ("goto".equals(command)) {
            cmdCode = 0x82;
        } else if ("remove".equals(command)) {
            cmdCode = 0x83;
        } else {
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "未识别的预置位指令：" + command);
        }
        Device device = deviceService.getDeviceByDeviceId(serial);
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR404.getCode(), "device[ " + serial + " ]未找到");
        }
        String remark = PresetRemarkService.name(preset, name);
        int sourceId = presetRemarkService.channelId(device, code);
        DeferredResult<WVPResult<String>> result = new DeferredResult<>(30_000L);
        result.onTimeout(() -> result.setResult(WVPResult.fail(408, "预置位指令应答超时，未确认设备接收")));
        ptzService.preset(device, code, cmdCode, preset,
                (status, msg, data) -> result.setResult(presetRemarkService.controlResult(sourceId, cmdCode, preset, remark, status, msg, data)));
        return result;
    }
}
