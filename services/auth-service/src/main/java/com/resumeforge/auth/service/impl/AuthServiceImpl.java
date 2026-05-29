package com.resumeforge.auth.service.impl;

import com.resumeforge.auth.dto.request.LoginRequest;
import com.resumeforge.auth.dto.request.RefreshTokenRequest;
import com.resumeforge.auth.dto.request.RegisterRequest;
import com.resumeforge.auth.dto.response.AuthResponse;
import com.resumeforge.auth.entity.RefreshToken;
import com.resumeforge.auth.entity.User;
import com.resumeforge.auth.exception.InvalidCredentialsException;
import com.resumeforge.auth.exception.InvalidRefreshTokenException;
import com.resumeforge.auth.exception.UserAlreadyExistsException;
import com.resumeforge.auth.exception.UserNotFoundException;
import com.resumeforge.auth.mapper.UserMapper;
import com.resumeforge.auth.repository.RefreshTokenRepository;
import com.resumeforge.auth.repository.UserRepository;
import com.resumeforge.auth.security.JwtService;
import com.resumeforge.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resumeforge.auth.event.AuthEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Authentication service implementation.
 *
 * Implements user registration, login, refresh token, and user info retrieval.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("User already exists with email: {}", request.getEmail());
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        // Create new user
        String passwordHash = passwordEncoder.encode(request.getPassword());
        User user = User.create(request.getEmail(), passwordHash);
        user.setEmailVerified(true); // TODO: Implement email verification flow
        
        User savedUser = userRepository.save(user);
        
        eventPublisher.publishEvent(new AuthEvent("REGISTER_SUCCESS", request.getEmail(), "User registered ID: " + savedUser.getId()));

        // Generate tokens
        return generateAuthResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt with email: {}", request.getEmail());

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> {
                eventPublisher.publishEvent(new AuthEvent("LOGIN_FAILURE", request.getEmail(), "User not found"));
                return new InvalidCredentialsException("Invalid email or password");
            });

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            eventPublisher.publishEvent(new AuthEvent("LOGIN_FAILURE", request.getEmail(), "Invalid password"));
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Check if user is active
        if (!user.isEnabled()) {
            eventPublisher.publishEvent(new AuthEvent("LOGIN_FAILURE", request.getEmail(), "Account disabled"));
            throw new InvalidCredentialsException("User account is disabled");
        }

        eventPublisher.publishEvent(new AuthEvent("LOGIN_SUCCESS", request.getEmail(), "Successful login"));

        // Generate tokens
        return generateAuthResponse(user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        log.debug("Refreshing token");

        // Hash the provided token
        String tokenHash = jwtService.hashToken(request.getRefreshToken());

        // Find refresh token in database
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
            .orElseThrow(() -> {
                log.warn("Refresh token not found");
                return new InvalidRefreshTokenException("Invalid refresh token");
            });

        // Check if token is valid
        if (!refreshToken.isValid()) {
            log.warn("Refresh token is expired or revoked");
            throw new InvalidRefreshTokenException("Refresh token is expired or revoked");
        }

        // Validate token signature
        if (!jwtService.validateToken(request.getRefreshToken())) {
            log.warn("Refresh token validation failed");
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        // Extract user ID from token
        UUID userId = UUID.fromString(jwtService.extractSubject(request.getRefreshToken()));

        // Get user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", userId);
                return new UserNotFoundException("User not found");
            });

        log.info("Token refreshed for user: {}", user.getEmail());

        // Generate new tokens
        return generateAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public AuthResponse getCurrentUser(String userId) {
        log.debug("Getting current user: {}", userId);

        UUID userUUID = UUID.fromString(userId);
        User user = userRepository.findById(userUUID)
            .orElseThrow(() -> {
                log.warn("User not found with ID: {}", userUUID);
                return new UserNotFoundException("User not found");
            });

        // Generate new tokens
        return generateAuthResponse(user);
    }

    /**
     * Generate authentication response with tokens
     */
    private AuthResponse generateAuthResponse(User user) {
        // Generate access token
        String accessToken = jwtService.generateAccessToken(user.getId().toString());

        // Generate refresh token
        String refreshToken = jwtService.generateRefreshToken(user.getId().toString());

        // Store refresh token hash in database
        String tokenHash = jwtService.hashToken(refreshToken);
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        RefreshToken refreshTokenEntity = RefreshToken.create(user.getId(), tokenHash, expiresAt);
        refreshTokenRepository.save(refreshTokenEntity);

        // Calculate expiration time
        long expiresIn = jwtService.getTokenExpirationTime(accessToken) - System.currentTimeMillis();

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(expiresIn / 1000) // Convert to seconds
            .user(userMapper.toResponse(user))
            .build();
    }
}
