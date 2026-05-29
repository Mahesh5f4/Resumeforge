package com.resumeforge.auth.service;

import com.resumeforge.auth.dto.request.LoginRequest;
import com.resumeforge.auth.dto.request.RegisterRequest;
import com.resumeforge.auth.dto.response.AuthResponse;
import com.resumeforge.auth.entity.User;
import com.resumeforge.auth.exception.InvalidCredentialsException;
import com.resumeforge.auth.exception.UserAlreadyExistsException;
import com.resumeforge.auth.mapper.UserMapper;
import com.resumeforge.auth.repository.RefreshTokenRepository;
import com.resumeforge.auth.repository.UserRepository;
import com.resumeforge.auth.security.JwtService;
import com.resumeforge.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = User.builder()
            .id(testUserId)
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .active(true)
            .emailVerified(true)
            .build();
    }

    @Test
    @DisplayName("Should register new user successfully")
    void testRegisterSuccess() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
            .email("newuser@example.com")
            .password("Password123")
            .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateAccessToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");
        when(jwtService.hashToken(anyString())).thenReturn("hashedToken");
        when(jwtService.getTokenExpirationTime(anyString())).thenReturn(System.currentTimeMillis() + 86400000);
        when(userMapper.toResponse(any(User.class))).thenReturn(
            com.resumeforge.auth.dto.response.UserResponse.builder()
                .id(testUserId)
                .email("test@example.com")
                .build()
        );

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        verify(userRepository, times(1)).save(any(User.class));
        verify(refreshTokenRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user already exists")
    void testRegisterUserAlreadyExists() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
            .email("existing@example.com")
            .password("Password123")
            .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccess() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
            .email("test@example.com")
            .password("password")
            .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getPassword(), testUser.getPasswordHash())).thenReturn(true);
        when(jwtService.generateAccessToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");
        when(jwtService.hashToken(anyString())).thenReturn("hashedToken");
        when(jwtService.getTokenExpirationTime(anyString())).thenReturn(System.currentTimeMillis() + 86400000);
        when(userMapper.toResponse(any(User.class))).thenReturn(
            com.resumeforge.auth.dto.response.UserResponse.builder()
                .id(testUserId)
                .email("test@example.com")
                .build()
        );

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    @DisplayName("Should throw exception on invalid credentials")
    void testLoginInvalidPassword() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
            .email("test@example.com")
            .password("wrongpassword")
            .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.getPassword(), testUser.getPasswordHash())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("Should throw exception when user not found during login")
    void testLoginUserNotFound() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
            .email("nonexistent@example.com")
            .password("password")
            .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
