package com.resumeforge.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeforge.auth.dto.ApiResponse;
import com.resumeforge.auth.dto.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Custom access denied handler for handling authorization failures.
 *
 * Returns a proper JSON response when user lacks permissions.
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.error("Access denied: {}", accessDeniedException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        final PrintWriter writer = response.getWriter();

        ApiResponse<?> errorResponse = ApiResponse.error(
            HttpServletResponse.SC_FORBIDDEN,
            "Access denied",
            ErrorDetails.builder()
                .code("FORBIDDEN")
                .details("You do not have permission to access this resource")
                .path(request.getRequestURI())
                .build()
        );

        writer.println(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
