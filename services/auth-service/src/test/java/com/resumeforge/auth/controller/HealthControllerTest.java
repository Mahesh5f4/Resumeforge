package com.resumeforge.auth.controller;

import com.resumeforge.auth.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for Health Controller.
 *
 * Tests health check and readiness probe endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Health Controller Tests")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 200 OK for health check")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(200)))
            .andExpect(jsonPath("$.data.status", is("UP")))
            .andExpect(jsonPath("$.message", containsString("healthy")));
    }

    @Test
    @DisplayName("Should return 200 OK for readiness check")
    void testReadinessCheck() throws Exception {
        mockMvc.perform(get("/health/ready"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(200)))
            .andExpect(jsonPath("$.data.ready", is(true)))
            .andExpect(jsonPath("$.message", containsString("ready")));
    }
}
