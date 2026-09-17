package com.genersoft.iot.vmp.gb28181.task.deviceStatus;

import com.genersoft.iot.vmp.common.RemoteAddressInfo;
import com.genersoft.iot.vmp.gb28181.bean.DeviceRegisterAttempt;
import com.genersoft.iot.vmp.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存尚未完成接入的下级设备注册请求。数据存放于Redis，供集群内页面共享。
 */
@Slf4j
@Component
public class DeviceRegisterAttemptManager {

    private static final String KEY = "VMP_DEVICE_REGISTER_ATTEMPT";

    private static final Duration EXPIRE = Duration.ofDays(7);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public synchronized void savePendingConfig(String deviceId, RemoteAddressInfo remoteAddressInfo, String transport) {
        DeviceRegisterAttempt previous = get(deviceId);
        save(DeviceRegisterAttempt.pendingConfig(deviceId, remoteAddressInfo.getIp(), remoteAddressInfo.getPort(),
                transport, previous));
    }

    public synchronized void saveAuthFailed(String deviceId, RemoteAddressInfo remoteAddressInfo, String transport,
                                            String authUsername) {
        DeviceRegisterAttempt previous = get(deviceId);
        save(DeviceRegisterAttempt.authFailed(deviceId, remoteAddressInfo.getIp(), remoteAddressInfo.getPort(),
                transport, authUsername, previous));
    }

    public List<DeviceRegisterAttempt> getAll() {
        List<DeviceRegisterAttempt> result = new ArrayList<>();
        try {
            List<Object> values = redisTemplate.opsForHash().values(KEY);
            for (Object value : values) {
                if (value instanceof DeviceRegisterAttempt) {
                    DeviceRegisterAttempt attempt = (DeviceRegisterAttempt) value;
                    if (attempt.getLastAttemptTime() != null
                            && DateUtil.getDifferenceForNow(attempt.getLastAttemptTime()) <= EXPIRE.toMillis()) {
                        result.add(attempt);
                    } else {
                        redisTemplate.opsForHash().delete(KEY, attempt.getDeviceId());
                    }
                }
            }
            result.sort((first, second) -> String.valueOf(second.getLastAttemptTime())
                    .compareTo(String.valueOf(first.getLastAttemptTime())));
        } catch (Exception e) {
            log.error("[设备注册] 查询待处理注册请求失败", e);
        }
        return result;
    }

    public void remove(String deviceId) {
        if (deviceId == null) {
            return;
        }
        try {
            redisTemplate.opsForHash().delete(KEY, deviceId);
        } catch (Exception e) {
            log.error("[设备注册] 删除待处理注册请求失败: {}", deviceId, e);
        }
    }

    private DeviceRegisterAttempt get(String deviceId) {
        try {
            Object value = redisTemplate.opsForHash().get(KEY, deviceId);
            if (value instanceof DeviceRegisterAttempt) {
                return (DeviceRegisterAttempt) value;
            }
        } catch (Exception e) {
            log.error("[设备注册] 查询待处理注册请求失败: {}", deviceId, e);
        }
        return null;
    }

    private void save(DeviceRegisterAttempt attempt) {
        try {
            redisTemplate.opsForHash().put(KEY, attempt.getDeviceId(), attempt);
            redisTemplate.expire(KEY, EXPIRE);
        } catch (Exception e) {
            log.error("[设备注册] 保存待处理注册请求失败: {}", attempt.getDeviceId(), e);
        }
    }
}
