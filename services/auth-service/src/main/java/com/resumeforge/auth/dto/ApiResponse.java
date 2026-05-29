package com.resumeforge.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper for all endpoints.
 *
 * Provides consistent response format across the service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Response message
     */
    private String message;

    /**
     * Response data
     */
    private T data;

    /**
     * Error details (if applicable)
     */
    private ErrorDetails error;

    /**
     * Timestamp of response
     */
    private LocalDateTime timestamp;

    /**
     * Create a success response
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .status(200)
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Create a success response
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Request successful");
    }

    /**
     * Create an error response
     */
    public static <T> ApiResponse<T> error(int status, String message, ErrorDetails errorDetails) {
        return ApiResponse.<T>builder()
            .status(status)
            .message(message)
            .error(errorDetails)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Create an error response
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        return error(status, message, null);
    }
}
