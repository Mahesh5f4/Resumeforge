package com.resumeforge.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthEvent {
    private String eventType; // LOGIN_SUCCESS, LOGIN_FAILURE, REGISTER
    private String email;
    private String details;
}
