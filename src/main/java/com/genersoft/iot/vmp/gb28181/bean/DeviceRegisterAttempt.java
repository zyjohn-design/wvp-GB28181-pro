package com.genersoft.iot.vmp.gb28181.bean;

import com.genersoft.iot.vmp.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 尚未完成接入的下级设备注册请求，供国标设备页面展示状态和处理指引。
 */
@Data
@Schema(description = "下级设备待处理注册请求")
public class DeviceRegisterAttempt implements Serializable {

    public static final String STATUS_PENDING_CONFIG = "PENDING_CONFIG";

    public static final String STATUS_AUTH_FAILED = "AUTH_FAILED";

    @Schema(description = "REGISTER From头中的设备国标编号")
    private String deviceId;

    @Schema(description = "注册请求来源IP")
    private String remoteIp;

    @Schema(description = "注册请求来源端口")
    private int remotePort;

    @Schema(description = "SIP传输协议")
    private String transport;

    @Schema(description = "认证用户名，不包含密码")
    private String authUsername;

    @Schema(description = "状态: PENDING_CONFIG-待配置, AUTH_FAILED-认证失败")
    private String status;

    @Schema(description = "返回给下级的SIP状态码")
    private int statusCode;

    @Schema(description = "失败原因")
    private String message;

    @Schema(description = "处理建议")
    private String suggestion;

    @Schema(description = "首次尝试时间")
    private String firstAttemptTime;

    @Schema(description = "最后尝试时间")
    private String lastAttemptTime;

    @Schema(description = "尝试次数")
    private int attemptCount;

    public static DeviceRegisterAttempt pendingConfig(String deviceId, String remoteIp, int remotePort,
                                                       String transport, DeviceRegisterAttempt previous) {
        DeviceRegisterAttempt result = base(deviceId, remoteIp, remotePort, transport, previous);
        result.setStatus(STATUS_PENDING_CONFIG);
        result.setStatusCode(403);
        result.setAuthUsername(null);
        result.setMessage("设备尚未配置，且公共注册密码已禁用");
        result.setSuggestion("点击“配置接入”，填写REGISTER From头中的设备编号和下级实际使用的注册密码；保存后等待下级重新注册。");
        return result;
    }

    public static DeviceRegisterAttempt authFailed(String deviceId, String remoteIp, int remotePort,
                                                    String transport, String authUsername,
                                                    DeviceRegisterAttempt previous) {
        DeviceRegisterAttempt result = base(deviceId, remoteIp, remotePort, transport, previous);
        result.setStatus(STATUS_AUTH_FAILED);
        result.setStatusCode(403);
        result.setAuthUsername(authUsername);
        result.setMessage("Digest认证失败：注册密码或SIP服务器ID/域不一致");
        result.setSuggestion("检查本页保存的设备注册密码，并核对下级的SIP服务器ID、域编码和认证用户名；密码不会在页面或日志中显示。");
        return result;
    }

    private static DeviceRegisterAttempt base(String deviceId, String remoteIp, int remotePort,
                                               String transport, DeviceRegisterAttempt previous) {
        String now = DateUtil.getNow();
        DeviceRegisterAttempt result = new DeviceRegisterAttempt();
        result.setDeviceId(deviceId);
        result.setRemoteIp(remoteIp);
        result.setRemotePort(remotePort);
        result.setTransport(transport);
        result.setFirstAttemptTime(previous == null ? now : previous.getFirstAttemptTime());
        result.setLastAttemptTime(now);
        result.setAttemptCount(previous == null ? 1 : previous.getAttemptCount() + 1);
        return result;
    }
}
