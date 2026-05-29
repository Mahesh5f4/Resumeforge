package com.resumeforge.auth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for User entity
 */
@DisplayName("User Entity Unit Tests")
class UserEntityTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create("test@example.com", "hashedPassword123");
    }

    @Test
    @DisplayName("Should create user with correct email and password hash")
    void testUserCreation() {
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedPassword123", user.getPasswordHash());
    }

    @Test
    @DisplayName("User should be active by default")
    void testUserActiveByDefault() {
        assertTrue(user.getActive());
    }

    @Test
    @DisplayName("User should not be email verified by default")
    void testUserEmailNotVerifiedByDefault() {
        assertFalse(user.getEmailVerified());
    }

    @Test
    @DisplayName("Should set user as active")
    void testSetUserActive() {
        user.setActive(true);
        assertTrue(user.getActive());
    }

    @Test
    @DisplayName("Should set user as inactive")
    void testSetUserInactive() {
        user.setActive(false);
        assertFalse(user.getActive());
    }

    @Test
    @DisplayName("Should verify user email")
    void testVerifyUserEmail() {
        user.setEmailVerified(true);
        assertTrue(user.getEmailVerified());
    }

    @Test
    @DisplayName("User should be enabled when active")
    void testUserEnabled() {
        user.setActive(true);
        user.setEmailVerified(true);
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("User should not be enabled when inactive")
    void testUserDisabled() {
        user.setActive(false);
        assertFalse(user.isEnabled());
    }

    @Test
    @DisplayName("Should generate ID when user is created")
    void testUserIdGenerated() {
        assertNotNull(user.getId());
        assertNotNull(user.getId().toString());
    }



    @Test
    @DisplayName("Should update email")
    void testUpdateEmail() {
        String newEmail = "newemail@example.com";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    @DisplayName("Should update password hash")
    void testUpdatePasswordHash() {
        String newHash = "newHashedPassword456";
        user.setPasswordHash(newHash);
        assertEquals(newHash, user.getPasswordHash());
    }
}
