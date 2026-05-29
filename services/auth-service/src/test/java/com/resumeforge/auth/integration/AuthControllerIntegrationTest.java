package com.resumeforge.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeforge.auth.dto.request.LoginRequest;
import com.resumeforge.auth.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for authentication endpoints using Testcontainers.
 *
 * Tests the full authentication flow with a real PostgreSQL database.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Authentication Integration Tests")
class AuthControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest validRegistrationRequest;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        validRegistrationRequest = RegisterRequest.builder()
            .email("testuser@example.com")
            .password("TestPassword123")
            .build();

        validLoginRequest = LoginRequest.builder()
            .email("testuser@example.com")
            .password("TestPassword123")
            .build();
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRegistrationRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status", is(201)))
            .andExpect(jsonPath("$.data.access_token", notNullValue()))
            .andExpect(jsonPath("$.data.refresh_token", notNullValue()))
            .andExpect(jsonPath("$.data.token_type", is("Bearer")))
            .andExpect(jsonPath("$.data.user.email", is("testuser@example.com")));
    }

    @Test
    @DisplayName("Should fail registration with invalid password")
    void testRegisterInvalidPassword() throws Exception {
        RegisterRequest invalidRequest = RegisterRequest.builder()
            .email("test@example.com")
            .password("weak")
            .build();

        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.error.fieldErrors.password", notNullValue()));
    }

    @Test
    @DisplayName("Should fail registration with duplicate email")
    void testRegisterDuplicateEmail() throws Exception {
        // Register first user
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRegistrationRequest)))
            .andExpect(status().isCreated());

        // Try to register with same email
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRegistrationRequest)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status", is(409)));
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccess() throws Exception {
        // Register user first
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRegistrationRequest)))
            .andExpect(status().isCreated());

        // Login
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(200)))
            .andExpect(jsonPath("$.data.access_token", notNullValue()))
            .andExpect(jsonPath("$.data.refresh_token", notNullValue()));
    }

    @Test
    @DisplayName("Should fail login with invalid credentials")
    void testLoginInvalidCredentials() throws Exception {
        // Register user first
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRegistrationRequest)))
            .andExpect(status().isCreated());

        // Try login with wrong password
        LoginRequest wrongPasswordRequest = LoginRequest.builder()
            .email("testuser@example.com")
            .password("WrongPassword123")
            .build();

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(wrongPasswordRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void testRefreshTokenSuccess() throws Exception {
        // Register and get tokens
        var registerResponse = mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validRegistrationRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String refreshToken = objectMapper.readTree(registerResponse)
            .get("data").get("refresh_token").asText();

        // Refresh token
        mockMvc.perform(post("/api/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"refreshToken\": \"" + refreshToken + "\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(200)))
            .andExpect(jsonPath("$.data.access_token", notNullValue()))
            .andExpect(jsonPath("$.data.refresh_token", notNullValue()));
    }
}
