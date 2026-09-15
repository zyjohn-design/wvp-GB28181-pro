package com.genersoft.iot.vmp.gb28181.task.platformStatus;

import com.genersoft.iot.vmp.gb28181.bean.PlatformRegisterResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存国标级联最近一次的注册结果, 供页面直接展示失败原因。
 * 存放于redis, 集群内共享, 保留7天。
 *
 * @author lin
 */
@Slf4j
@Component
public class PlatformRegisterResultManager {

    private static final String PREFIX = "VMP_PLATFORM_REGISTER_RESULT";

    private static final Duration EXPIRE = Duration.ofDays(7);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(PlatformRegisterResult result) {
        if (result == null || result.getPlatformId() == null) {
            return;
        }
        if (result.isSuccess()) {
            log.info("[国标级联] 注册结果: {}, {}", result.getPlatformId(), result.getMessage());
        } else {
            log.warn("[国标级联] 注册结果: {}, 阶段: {}, {}", result.getPlatformId(), result.getStage(), result.getMessage());
        }
        try {
            redisTemplate.opsForValue().set(getKey(result.getPlatformId()), result, EXPIRE);
        } catch (Exception e) {
            log.error("[国标级联] 保存注册结果失败: {}", result.getPlatformId(), e);
        }
    }

    public PlatformRegisterResult get(String platformId) {
        if (platformId == null) {
            return null;
        }
        try {
            Object value = redisTemplate.opsForValue().get(getKey(platformId));
            if (value instanceof PlatformRegisterResult) {
                return (PlatformRegisterResult) value;
            }
        } catch (Exception e) {
            log.error("[国标级联] 查询注册结果失败: {}", platformId, e);
        }
        return null;
    }

    /**
     * 批量查询, 用于列表展示
     */
    public Map<String, PlatformRegisterResult> get(List<String> platformIdList) {
        Map<String, PlatformRegisterResult> resultMap = new HashMap<>();
        if (platformIdList == null || platformIdList.isEmpty()) {
            return resultMap;
        }
        for (String platformId : platformIdList) {
            PlatformRegisterResult result = get(platformId);
            if (result != null) {
                resultMap.put(platformId, result);
            }
        }
        return resultMap;
    }

    public void remove(String platformId) {
        if (platformId == null) {
            return;
        }
        redisTemplate.delete(getKey(platformId));
    }

    private String getKey(String platformId) {
        return String.format("%s_%s", PREFIX, platformId);
    }
}
