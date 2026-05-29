package com.resumeforge.auth.exception;

/**
 * Exception thrown when user already exists
 */
public class UserAlreadyExistsException extends AuthException {

    public UserAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS");
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, "USER_ALREADY_EXISTS", cause);
    }
}
