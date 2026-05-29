package com.resumeforge.auth.util;

import com.resumeforge.auth.exception.InvalidJwtException;
import com.resumeforge.auth.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utility component for JWT extraction and validation from HTTP requests.
 *
 * Handles Bearer token extraction from Authorization headers.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtService jwtService;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Extract JWT token from Authorization header
     *
     * @param request the HTTP request
     * @return Optional containing token if found
     */
    public Optional<String> extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || authHeader.isBlank()) {
            return Optional.empty();
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Authorization header does not start with Bearer");
            return Optional.empty();
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        return token.isBlank() ? Optional.empty() : Optional.of(token);
    }

    /**
     * Extract JWT token from Authorization header or throw exception
     *
     * @param request the HTTP request
     * @return the JWT token
     * @throws InvalidJwtException if token is not found or invalid
     */
    public String extractJwtTokenOrThrow(HttpServletRequest request) throws InvalidJwtException {
        return extractJwtToken(request)
            .orElseThrow(() -> new InvalidJwtException("JWT token not found in Authorization header"));
    }

    /**
     * Validate JWT token
     *
     * @param token the JWT token
     * @return true if valid
     * @throws InvalidJwtException if token is invalid
     */
    public boolean validateToken(String token) throws InvalidJwtException {
        if (!jwtService.validateToken(token)) {
            throw new InvalidJwtException("JWT token is invalid or expired");
        }
        return true;
    }

    /**
     * Extract user ID from token
     *
     * @param token the JWT token
     * @return user ID (subject)
     * @throws InvalidJwtException if token is invalid
     */
    public String extractUserId(String token) throws InvalidJwtException {
        return jwtService.extractSubject(token);
    }
}
