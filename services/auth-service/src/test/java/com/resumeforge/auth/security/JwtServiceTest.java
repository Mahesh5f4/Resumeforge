package com.resumeforge.auth.security;

import com.resumeforge.auth.exception.InvalidJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JWT Service.
 *
 * Tests token generation, validation, and extraction functionality.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=test-secret-key-for-testing-purpose-only-must-be-at-least-32-characters",
    "jwt.expiration=86400000",
    "jwt.refresh-expiration=604800000"
})
@DisplayName("JWT Service Tests")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String testToken;
    private String testSubject;

    @BeforeEach
    void setUp() {
        testSubject = "test-user@example.com";
        testToken = jwtService.generateAccessToken(testSubject);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        assertNotNull(testToken);
        assertFalse(testToken.isBlank());
        assertTrue(testToken.contains("."));
    }

    @Test
    @DisplayName("Should validate generated token")
    void testValidateToken() {
        assertTrue(jwtService.validateToken(testToken));
    }

    @Test
    @DisplayName("Should extract subject from token")
    void testExtractSubject() throws InvalidJwtException {
        String subject = jwtService.extractSubject(testToken);
        assertEquals(testSubject, subject);
    }

    @Test
    @DisplayName("Should throw exception for invalid token")
    void testExtractSubjectFromInvalidToken() {
        String invalidToken = "invalid.token.here";
        assertThrows(InvalidJwtException.class, () -> jwtService.extractSubject(invalidToken));
    }

    @Test
    @DisplayName("Should generate refresh token")
    void testGenerateRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(testSubject);
        assertNotNull(refreshToken);
        assertTrue(jwtService.validateToken(refreshToken));
    }

    @Test
    @DisplayName("Token should not be expired immediately after generation")
    void testTokenNotExpired() {
        assertFalse(jwtService.isTokenExpired(testToken));
    }
}
