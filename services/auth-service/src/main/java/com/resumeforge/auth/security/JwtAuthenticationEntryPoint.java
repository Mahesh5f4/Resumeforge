package com.resumeforge.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeforge.auth.dto.ApiResponse;
import com.resumeforge.auth.dto.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Custom authentication entry point for handling authentication failures.
 *
 * Returns a proper JSON response instead of default Spring Security error page.
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        log.error("Authentication failed: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final PrintWriter writer = response.getWriter();

        ApiResponse<?> errorResponse = ApiResponse.error(
            HttpServletResponse.SC_UNAUTHORIZED,
            "Authentication failed",
            ErrorDetails.builder()
                .code("UNAUTHORIZED")
                .details(authException.getMessage())
                .path(request.getRequestURI())
                .build()
        );

        writer.println(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
