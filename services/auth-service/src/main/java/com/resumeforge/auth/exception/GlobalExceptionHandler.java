package com.resumeforge.auth.exception;

import com.resumeforge.auth.dto.ApiResponse;
import com.resumeforge.auth.dto.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all REST endpoints.
 *
 * Centralizes exception handling and provides consistent error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle user already exists exception
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleUserAlreadyExistsException(
        UserAlreadyExistsException ex,
        WebRequest request
    ) {
        log.warn("User already exists: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code(ex.getErrorCode())
            .details(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(
                HttpStatus.CONFLICT.value(),
                "User already exists",
                errorDetails
            ));
    }

    /**
     * Handle user not found exception
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(
        UserNotFoundException ex,
        WebRequest request
    ) {
        log.warn("User not found: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code(ex.getErrorCode())
            .details(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "User not found",
                errorDetails
            ));
    }

    /**
     * Handle invalid credentials exception
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidCredentialsException(
        InvalidCredentialsException ex,
        WebRequest request
    ) {
        log.warn("Invalid credentials: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code(ex.getErrorCode())
            .details(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed",
                errorDetails
            ));
    }

    /**
     * Handle invalid refresh token exception
     */
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidRefreshTokenException(
        InvalidRefreshTokenException ex,
        WebRequest request
    ) {
        log.warn("Invalid refresh token: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code(ex.getErrorCode())
            .details(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "Token refresh failed",
                errorDetails
            ));
    }

    /**
     * Handle authentication exceptions
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthException(
        AuthException ex,
        WebRequest request
    ) {
        log.warn("Authentication error: {}", ex.getErrorCode(), ex);

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code(ex.getErrorCode())
            .details(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed",
                errorDetails
            ));
    }

    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
        MethodArgumentNotValidException ex,
        WebRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        log.warn("Validation error: {}", fieldErrors);

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code("VALIDATION_ERROR")
            .details("One or more fields failed validation")
            .fieldErrors(fieldErrors)
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errorDetails
            ));
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(
        Exception ex,
        WebRequest request
    ) {
        log.error("Unexpected error", ex);

        ErrorDetails errorDetails = ErrorDetails.builder()
            .code("INTERNAL_SERVER_ERROR")
            .details(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                errorDetails
            ));
    }
}
