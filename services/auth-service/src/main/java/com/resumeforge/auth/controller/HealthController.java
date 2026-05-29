package com.resumeforge.auth.controller;

import com.resumeforge.auth.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check endpoint for service monitoring and deployment verification.
 *
 * Provides liveness and readiness probes for container orchestration platforms.
 */
@Slf4j
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Service health check endpoints")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    /**
     * Liveness probe - indicates if the service is running
     */
    @GetMapping
    @Operation(summary = "Liveness probe", description = "Check if service is running")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Service is up and running",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        log.debug("Health check requested");

        Map<String, Object> healthData = new HashMap<>();
        healthData.put("service", applicationName);
        healthData.put("status", "UP");
        healthData.put("port", serverPort);

        return ResponseEntity
            .ok(ApiResponse.success(healthData, "Service is healthy"));
    }

    /**
     * Readiness probe - indicates if service is ready to receive traffic
     */
    @GetMapping("/ready")
    @Operation(summary = "Readiness probe", description = "Check if service is ready to accept requests")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Service is ready",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "503",
            description = "Service is not ready"
        )
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> ready() {
        log.debug("Readiness check requested");

        Map<String, Object> readinessData = new HashMap<>();
        readinessData.put("service", applicationName);
        readinessData.put("ready", true);

        return ResponseEntity
            .ok(ApiResponse.success(readinessData, "Service is ready"));
    }
}
