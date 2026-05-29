package com.resumeforge.auth.rate_limit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for rate limiting specific endpoints.
 *
 * Applies stricter limits to login and register endpoints.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String clientIp = getClientIp(request);

        boolean allowed = true;

        if (requestURI.endsWith("/login")) {
            allowed = rateLimiter.resolveLoginBucket(clientIp).tryConsume(1);
        } else if (requestURI.endsWith("/register")) {
            allowed = rateLimiter.resolveRegisterBucket(clientIp).tryConsume(1);
        } else {
            allowed = rateLimiter.resolveBucket(clientIp).tryConsume(1);
        }

        if (!allowed) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract client IP from request
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // Don't apply rate limiting to health check and swagger endpoints
        return requestURI.startsWith("/health") || 
               requestURI.startsWith("/docs") || 
               requestURI.startsWith("/swagger-ui") ||
               requestURI.startsWith("/v3/api-docs");
    }
}
