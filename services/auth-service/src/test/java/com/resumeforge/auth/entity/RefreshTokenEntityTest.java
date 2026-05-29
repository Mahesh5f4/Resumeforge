package com.resumeforge.auth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RefreshToken entity
 */
@DisplayName("RefreshToken Entity Unit Tests")
class RefreshTokenEntityTest {

    private RefreshToken refreshToken;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        refreshToken = RefreshToken.create(userId, "hashedTokenValue", java.time.LocalDateTime.now().plusDays(7));
    }

    @Test
    @DisplayName("Should create refresh token with user ID and token hash")
    void testRefreshTokenCreation() {
        assertNotNull(refreshToken);
        assertEquals(userId, refreshToken.getUserId());
        assertEquals("hashedTokenValue", refreshToken.getTokenHash());
    }

    @Test
    @DisplayName("Refresh token should not be revoked by default")
    void testRefreshTokenNotRevokedByDefault() {
        assertFalse(refreshToken.getRevoked());
    }

    @Test
    @DisplayName("Should generate ID when token is created")
    void testRefreshTokenIdGenerated() {
        assertNotNull(refreshToken.getId());
    }

    @Test
    @DisplayName("Should set expiration time")
    void testRefreshTokenExpiration() {
        assertNotNull(refreshToken.getExpiresAt());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(refreshToken.getExpiresAt().isAfter(now));
    }

    @Test
    @DisplayName("Should check if token is valid when not expired and not revoked")
    void testRefreshTokenIsValid() {
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        refreshToken.setRevoked(false);
        assertTrue(refreshToken.isValid());
    }

    @Test
    @DisplayName("Should check if token is invalid when expired")
    void testRefreshTokenIsInvalidWhenExpired() {
        refreshToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        refreshToken.setRevoked(false);
        assertFalse(refreshToken.isValid());
    }

    @Test
    @DisplayName("Should check if token is invalid when revoked")
    void testRefreshTokenIsInvalidWhenRevoked() {
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        refreshToken.setRevoked(true);
        assertFalse(refreshToken.isValid());
    }

    @Test
    @DisplayName("Should revoke refresh token")
    void testRevokeRefreshToken() {
        refreshToken.revoke();
        assertTrue(refreshToken.getRevoked());
    }



    @Test
    @DisplayName("Should update expiration time")
    void testUpdateExpiration() {
        LocalDateTime newExpiration = LocalDateTime.now().plusHours(1);
        refreshToken.setExpiresAt(newExpiration);
        assertEquals(newExpiration, refreshToken.getExpiresAt());
    }
}
