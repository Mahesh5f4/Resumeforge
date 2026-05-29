package com.resumeforge.auth.exception;

/**
 * Exception thrown when user is not found
 */
public class UserNotFoundException extends AuthException {

    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND");
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, "USER_NOT_FOUND", cause);
    }
}
