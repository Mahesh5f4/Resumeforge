package com.resumeforge.auth.exception;

/**
 * Exception thrown when refresh token is invalid or expired
 */
public class InvalidRefreshTokenException extends AuthException {

    public InvalidRefreshTokenException(String message) {
        super(message, "INVALID_REFRESH_TOKEN");
    }

    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, "INVALID_REFRESH_TOKEN", cause);
    }
}
