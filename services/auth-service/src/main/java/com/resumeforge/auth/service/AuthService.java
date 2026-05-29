package com.resumeforge.auth.service;

import com.resumeforge.auth.dto.request.LoginRequest;
import com.resumeforge.auth.dto.request.RefreshTokenRequest;
import com.resumeforge.auth.dto.request.RegisterRequest;
import com.resumeforge.auth.dto.response.AuthResponse;

/**
 * Authentication service interface.
 *
 * Defines contract for authentication operations.
 */
public interface AuthService {

    /**
     * Register a new user
     *
     * @param request registration request with email and password
     * @return authentication response with tokens
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate user with email and password
     *
     * @param request login request with credentials
     * @return authentication response with tokens
     */
    AuthResponse login(LoginRequest request);

    /**
     * Refresh access token using refresh token
     *
     * @param request request containing refresh token
     * @return new authentication response with tokens
     */
    AuthResponse refresh(RefreshTokenRequest request);

    /**
     * Get current authenticated user info
     *
     * @param userId the user ID from JWT token
     * @return user information response
     */
    AuthResponse getCurrentUser(String userId);
}
