package com.genersoft.iot.vmp.vmanager.server;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** 仅在ApplicationReadyEvent的全部启动恢复任务完成后才返回就绪。 */
@RestController
public class ReadinessController {
    private final ApplicationAvailability availability;

    public ReadinessController(ApplicationAvailability availability) {
        this.availability = availability;
    }

    @GetMapping("/api/server/health/ready")
    @Operation(summary = "服务就绪检查：启动恢复完成返回200，否则503；无需认证")
    public ResponseEntity<Map<String, String>> ready() {
        boolean ready = availability.getReadinessState() == ReadinessState.ACCEPTING_TRAFFIC;
        return ResponseEntity.status(ready ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE)
                .cacheControl(CacheControl.noStore())
                .body(Map.of("status", ready ? "UP" : "NOT_READY"));
    }
}
