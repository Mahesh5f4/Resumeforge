package com.resumeforge.auth.exception;

/**
 * Exception thrown when JWT token is invalid or malformed.
 */
public class InvalidJwtException extends AuthException {

    public InvalidJwtException(String message) {
        super(message, "INVALID_JWT");
    }

    public InvalidJwtException(String message, Throwable cause) {
        super(message, "INVALID_JWT", cause);
    }
}
