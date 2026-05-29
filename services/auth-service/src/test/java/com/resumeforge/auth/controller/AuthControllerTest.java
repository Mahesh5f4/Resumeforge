package com.resumeforge.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeforge.auth.dto.request.LoginRequest;
import com.resumeforge.auth.dto.request.RefreshTokenRequest;
import com.resumeforge.auth.dto.request.RegisterRequest;
import com.resumeforge.auth.dto.response.AuthResponse;
import com.resumeforge.auth.dto.response.UserResponse;
import com.resumeforge.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AuthController (mocked service)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new AuthController(authService))
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should register new user and return 201 Created")
    void testRegisterSuccess() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("SecurePass123!");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID());
        userResponse.setEmail("newuser@example.com");
        userResponse.setActive(true);
        userResponse.setEmailVerified(true);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("access-token-123");
        authResponse.setRefreshToken("refresh-token-123");
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(86400L);
        authResponse.setUser(userResponse);

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.access_token").value("access-token-123"))
            .andExpect(jsonPath("$.data.token_type").value("Bearer"))
            .andExpect(jsonPath("$.data.user.email").value("newuser@example.com"));
    }

    @Test
    @DisplayName("Should return 200 OK with tokens on successful login")
    void testLoginSuccess() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("Password123!");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID());
        userResponse.setEmail("user@example.com");
        userResponse.setActive(true);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("access-token-456");
        authResponse.setRefreshToken("refresh-token-456");
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(86400L);
        authResponse.setUser(userResponse);

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.access_token").value("access-token-456"))
            .andExpect(jsonPath("$.data.refresh_token").value("refresh-token-456"))
            .andExpect(jsonPath("$.data.user.email").value("user@example.com"));
    }

    @Test
    @DisplayName("Should refresh token and return new tokens")
    void testRefreshTokenSuccess() throws Exception {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(UUID.randomUUID());
        userResponse.setEmail("user@example.com");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("new-access-token");
        authResponse.setRefreshToken("new-refresh-token");
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(86400L);
        authResponse.setUser(userResponse);

        when(authService.refresh(any(RefreshTokenRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.access_token").value("new-access-token"))
            .andExpect(jsonPath("$.data.refresh_token").value("new-refresh-token"));
    }

    @Test
    @DisplayName("Should return current user info when authenticated")
    void testGetCurrentUserSuccess() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setEmail("user@example.com");
        userResponse.setActive(true);
        userResponse.setEmailVerified(true);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("access-token");
        authResponse.setUser(userResponse);

        when(authService.getCurrentUser(userId.toString())).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer valid-jwt-token")
                .principal(new UsernamePasswordAuthenticationToken(userId.toString(), null)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.user.email").value("user@example.com"))
            .andExpect(jsonPath("$.data.user.id").value(userId.toString()));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid register request")
    void testRegisterInvalidRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid login request")
    void testLoginInvalidRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid refresh request")
    void testRefreshInvalidRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}
