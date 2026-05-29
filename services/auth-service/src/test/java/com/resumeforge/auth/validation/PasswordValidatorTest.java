package com.resumeforge.auth.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for password validator
 */
@DisplayName("Password Validator Tests")
class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    @DisplayName("Should validate strong password")
    void testValidStrongPassword() {
        assertTrue(validator.isValid("StrongPassword123", null));
        assertTrue(validator.isValid("MySecurePass999", null));
    }

    @Test
    @DisplayName("Should reject password without uppercase")
    void testRejectPasswordWithoutUppercase() {
        assertFalse(validator.isValid("lowercase123", null));
    }

    @Test
    @DisplayName("Should reject password without lowercase")
    void testRejectPasswordWithoutLowercase() {
        assertFalse(validator.isValid("UPPERCASE123", null));
    }

    @Test
    @DisplayName("Should reject password without digit")
    void testRejectPasswordWithoutDigit() {
        assertFalse(validator.isValid("NoDigitHere", null));
    }

    @Test
    @DisplayName("Should reject password shorter than 8 characters")
    void testRejectShortPassword() {
        assertFalse(validator.isValid("Short12", null));
    }

    @Test
    @DisplayName("Should reject null password")
    void testRejectNullPassword() {
        assertFalse(validator.isValid(null, null));
    }
}
