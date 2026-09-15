package com.genersoft.iot.vmp.gb28181.bean;

import com.genersoft.iot.vmp.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 国标级联注册结果, 用于向前端展示最近一次注册（或注册测试）的明确原因，
 * 避免用户只能看到"离线"两个字而必须去翻日志。
 *
 * @author lin
 */
@Data
@Schema(description = "国标级联注册结果")
public class PlatformRegisterResult implements Serializable {

    /**
     * 测试注册, 由用户在页面手动触发
     */
    public static final String SOURCE_TEST = "TEST";

    /**
     * 平台自动注册
     */
    public static final String SOURCE_AUTO = "AUTO";

    /**
     * 本地发送阶段（还没发出去）
     */
    public static final String STAGE_LOCAL_SEND = "LOCAL_SEND";

    /**
     * 首次REGISTER阶段
     */
    public static final String STAGE_REGISTER = "REGISTER";

    /**
     * 携带Digest认证信息的第二次REGISTER阶段
     */
    public static final String STAGE_REGISTER_AUTH = "REGISTER_AUTH";

    /**
     * 注册成功后的心跳阶段
     */
    public static final String STAGE_KEEPALIVE = "KEEPALIVE";

    /**
     * 超时（本地等待超时，非上级返回408）
     */
    public static final int CODE_TIMEOUT = -1024;

    @Schema(description = "上级平台国标编号")
    private String platformId;

    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "所处阶段: LOCAL_SEND/REGISTER/REGISTER_AUTH/KEEPALIVE")
    private String stage;

    @Schema(description = "SIP状态码, -1024表示本地等待超时")
    private int statusCode;

    @Schema(description = "结果描述")
    private String message;

    @Schema(description = "处理建议")
    private String suggestion;

    @Schema(description = "发生时间")
    private String time;

    @Schema(description = "来源: TEST-手动测试注册, AUTO-平台自动注册")
    private String source;

    public PlatformRegisterResult() {
    }

    /**
     * 注册成功
     */
    public static PlatformRegisterResult success(Platform platform, String source) {
        PlatformRegisterResult result = base(platform, source, STAGE_REGISTER);
        result.setSuccess(true);
        result.setStatusCode(200);
        result.setMessage("200 OK：注册成功");
        result.setSuggestion(null);
        return result;
    }

    /**
     * 本地发送失败, 消息还没有发出去
     */
    public static PlatformRegisterResult localFail(Platform platform, String source, String message, String suggestion) {
        PlatformRegisterResult result = base(platform, source, STAGE_LOCAL_SEND);
        result.setSuccess(false);
        result.setStatusCode(0);
        result.setMessage(message);
        result.setSuggestion(suggestion);
        return result;
    }

    /**
     * 心跳失败（注册是成功的）
     */
    public static PlatformRegisterResult keepaliveFail(Platform platform, String source, int statusCode, String rawMsg) {
        PlatformRegisterResult result = base(platform, source, STAGE_KEEPALIVE);
        result.setSuccess(false);
        result.setStatusCode(statusCode);
        if (statusCode == CODE_TIMEOUT || statusCode == 0) {
            result.setMessage("注册成功，但连续3次心跳未收到200 OK");
            result.setSuggestion(String.format("上级平台 %s:%s 收到了注册但不回复心跳, 请检查上级平台是否已把本平台设置为在线设备, " +
                    "以及UDP/TCP(当前%s)是否被中间设备限制", platform.getServerIp(), platform.getServerPort(), platform.getTransport()));
        } else {
            result.setMessage(String.format("注册成功，但心跳被上级拒绝：%d %s", statusCode, rawMsg == null ? "" : rawMsg));
            result.setSuggestion("请确认上级平台是否要求心跳携带认证信息, 或本平台国标编号在上级是否已被删除");
        }
        return result;
    }

    /**
     * 注册失败, 按照SIP状态码给出明确原因与建议
     *
     * @param stage      REGISTER: 首次注册; REGISTER_AUTH: 携带Digest认证信息的第二次注册
     * @param statusCode SIP状态码, -1024表示本地等待超时
     * @param rawMsg     SIP回复中的原始描述
     */
    public static PlatformRegisterResult fail(Platform platform, String source, String stage, int statusCode, String rawMsg) {
        PlatformRegisterResult result = base(platform, source, stage);
        result.setSuccess(false);
        result.setStatusCode(statusCode);
        String address = platform.getServerIp() + ":" + platform.getServerPort();
        String msg = rawMsg == null ? "" : rawMsg;
        switch (statusCode) {
            case CODE_TIMEOUT:
            case 408:
                result.setMessage(String.format("注册超时：上级平台 %s 未在规定时间内响应", address));
                result.setSuggestion(String.format("请检查上级平台IP、端口是否正确, 信令传输模式(当前%s)是否与上级一致, " +
                        "以及双方网络、防火墙是否放通", platform.getTransport()));
                break;
            case 400:
                result.setMessage(String.format("400 Bad Request：上级平台认为注册报文有误 %s", msg));
                result.setSuggestion(String.format("请检查上级平台国标编号(%s)、SIP域(%s)、本平台国标编号(%s)的长度与格式是否符合规范",
                        platform.getServerGBId(), platform.getServerGBDomain(), platform.getDeviceGBId()));
                break;
            case 401:
                if (STAGE_REGISTER_AUTH.equals(stage)) {
                    result.setMessage("401 Unauthorized：认证失败");
                    result.setSuggestion(String.format("请检查SIP认证用户名(%s)与密码是否与上级平台配置一致",
                            platform.getUsername() == null ? platform.getDeviceGBId() : platform.getUsername()));
                } else {
                    result.setMessage("401 Unauthorized：上级平台要求认证, 但认证流程未完成");
                    result.setSuggestion("请确认已填写SIP认证密码, 并检查上级平台的认证方式是否为Digest");
                }
                break;
            case 403:
                result.setMessage("403 Forbidden：上级平台拒绝注册");
                result.setSuggestion(String.format("请确认上级平台已添加本平台国标编号 %s, 并允许来源IP %s 注册",
                        platform.getDeviceGBId(), platform.getDeviceIp()));
                break;
            case 404:
                result.setMessage("404 Not Found：上级平台未找到注册地址");
                result.setSuggestion(String.format("请检查上级平台国标编号(%s)与SIP域(%s)是否填写正确",
                        platform.getServerGBId(), platform.getServerGBDomain()));
                break;
            default:
                if (statusCode >= 500 && statusCode <= 599) {
                    result.setMessage(String.format("%d %s：上级平台内部异常", statusCode, msg));
                    result.setSuggestion("上级平台处理注册时发生异常, 请联系上级平台运维查看其日志");
                } else if (statusCode > 0) {
                    result.setMessage(String.format("%d %s：注册失败", statusCode, msg));
                    result.setSuggestion("请根据上级平台返回的状态码检查平台配置");
                } else {
                    result.setMessage(msg.isEmpty() ? "注册失败" : msg);
                    result.setSuggestion("请查看服务日志中[国标级联]相关内容");
                }
                break;
        }
        return result;
    }

    private static PlatformRegisterResult base(Platform platform, String source, String stage) {
        PlatformRegisterResult result = new PlatformRegisterResult();
        result.setPlatformId(platform.getServerGBId());
        result.setSource(source);
        result.setStage(stage);
        result.setTime(DateUtil.getNow());
        return result;
    }
}
