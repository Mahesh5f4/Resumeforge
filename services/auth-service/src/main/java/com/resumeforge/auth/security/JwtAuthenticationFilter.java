package com.resumeforge.auth.security;

import com.resumeforge.auth.exception.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

/**
 * JWT Authentication Filter for extracting and validating JWT tokens from requests.
 *
 * This filter intercepts incoming requests and validates JWT tokens in the Authorization header.
 * It is a skeleton implementation - implement token extraction and validation logic as needed.
 */
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtService jwtService;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Initialize the filter with JWT service
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        super(new AntPathRequestMatcher("/api/auth/**"));
        this.jwtService = jwtService;
    }

    /**
     * Attempt authentication by extracting JWT from request
     */
    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {

        String token = extractJwtToken(request);

        if (token == null) {
            log.debug("No JWT token found in request");
            return null;
        }

        // Validate token
        try {
            if (!jwtService.validateToken(token)) {
                log.warn("JWT token validation failed");
                throw new InvalidJwtException("Invalid JWT token");
            }

            String subject = jwtService.extractSubject(token);
            log.debug("JWT token validated successfully for subject: {}", subject);

            // TODO: Create and return authentication object
            // This is a skeleton - implement full authentication logic
            return null;

        } catch (InvalidJwtException ex) {
            log.error("JWT authentication failed: {}", ex.getMessage());
            throw new InvalidJwtException("JWT authentication failed: " + ex.getMessage());
        }
    }

    /**
     * Extract JWT token from Authorization header
     *
     * @param request the HTTP request
     * @return JWT token or null if not found
     */
    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
