package com.genersoft.iot.vmp.conf;

import com.genersoft.iot.vmp.media.bean.MediaServer;
import com.genersoft.iot.vmp.utils.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

@Slf4j
@Configuration("mediaConfig")
@Order(0)
@Data
public class MediaConfig{

    // 修改必须配置，不再支持自动获取
    @Value("${media.id}")
    private String id;

    @Value("${media.ip}")
    private String ip;

    @Value("${media.wan_ip:}")
    private String wanIp;

    @Value("${media.hook-ip:127.0.0.1}")
    private String hookIp;

    @Value("${sip.domain}")
    private String sipDomain;

    @Value("${media.sdp-ip:${media.wan_ip:}}")
    private String sdpIp;

    @Value("${media.stream-ip:${media.wan_ip:}}")
    private String streamIp;

    @Value("${media.http-port:0}")
    private Integer httpPort;

    // 现场旧配置可能将 MediaHttp/MediaHttps 展开为空字符串，使用 String 后自行容错，
    // 避免 Spring 将空值直接转换 Integer 导致整个 WVP 上下文启动失败。
    @Value("${media.flv-port:}")
    private String flvPort;

    @Value("${media.flv-ssl-port:}")
    private String flvSslPort;

    @Value("${media.ws-flv-port:}")
    private String wsFlvPort;

    @Value("${media.ws-flv-ssl-port:}")
    private String wsFlvSslPort;

    @Value("${media.auto-config:true}")
    private boolean autoConfig = true;

    @Value("${media.secret}")
    private String secret;

    @Value("${media.rtp.enable}")
    private boolean rtpEnable;

    @Value("${media.rtp.port-range}")
    private String rtpPortRange;

    @Value("${media.rtp.send-port-range}")
    private String rtpSendPortRange;

    @Value("${media.record-assist-port:0}")
    private Integer recordAssistPort = 0;

    @Value("${media.record-day:7}")
    private Integer recordDay;

    @Value("${media.record-path:}")
    private String recordPath;

    @Value("${media.type:zlm}")
    private String type;


    public String getSdpIp() {
        if (ObjectUtils.isEmpty(sdpIp)){
            return ip;
        }else {
            if (isValidIPAddress(sdpIp)) {
                return sdpIp;
            }else {
                // 按照域名解析
                String hostAddress = null;
                try {
                    hostAddress = InetAddress.getByName(sdpIp).getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("[获取SDP IP]: 域名解析失败");
                }
                return hostAddress;
            }
        }
    }

    public String getStreamIp() {
        if (ObjectUtils.isEmpty(streamIp)){
            return ip;
        }else {
            return streamIp;
        }
    }

    public MediaServer buildMediaSer(){
        MediaServer mediaServer = new MediaServer();
        mediaServer.setId(id);
        mediaServer.setIp(ip);
        mediaServer.setDefaultServer(true);
        mediaServer.setHookIp(getHookIp());
        mediaServer.setSdpIp(getSdpIp());
        mediaServer.setStreamIp(getStreamIp());
        mediaServer.setHttpPort(httpPort);
        mediaServer.setFlvPort(parsePort(flvPort, httpPort));
        mediaServer.setFlvSSLPort(parsePort(flvSslPort, 0));
        mediaServer.setWsFlvPort(parsePort(wsFlvPort, httpPort));
        mediaServer.setWsFlvSSLPort(parsePort(wsFlvSslPort, 0));
        mediaServer.setAutoConfig(autoConfig);
        mediaServer.setSecret(secret);
        mediaServer.setRtpEnable(rtpEnable);
        mediaServer.setRtpPortRange(rtpPortRange);
        mediaServer.setSendRtpPortRange(rtpSendPortRange);
        mediaServer.setRecordAssistPort(recordAssistPort);
        mediaServer.setHookAliveInterval(10f);
        mediaServer.setRecordDay(recordDay);
        mediaServer.setStatus(false);
        mediaServer.setType(type);
        if (recordPath != null) {
            mediaServer.setRecordPath(recordPath);
        }
        mediaServer.setCreateTime(DateUtil.getNow());
        mediaServer.setUpdateTime(DateUtil.getNow());

        return mediaServer;
    }

    private int parsePort(String value, int fallback) {
        if (ObjectUtils.isEmpty(value)) {
            return fallback;
        }
        try {
            int port = Integer.parseInt(value.trim());
            return port > 0 && port <= 65535 ? port : fallback;
        } catch (NumberFormatException e) {
            log.warn("[媒体端口配置] 无法解析端口值：{}，使用默认值：{}", value, fallback);
            return fallback;
        }
    }

    private boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null) && (!ipAddress.isEmpty())) {
            return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
        }
        return false;
    }
}
