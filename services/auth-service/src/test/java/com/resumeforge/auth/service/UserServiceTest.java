package com.resumeforge.auth.service;

import com.resumeforge.auth.entity.User;
import com.resumeforge.auth.exception.UserNotFoundException;
import com.resumeforge.auth.repository.UserRepository;
import com.resumeforge.auth.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for UserService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Should find user by ID")
    void testFindUserById() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.create("user@example.com", "hashedPassword");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty optional when user ID not found")
    void testFindUserByIdNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(userId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindUserByEmail() {
        // Arrange
        User user = User.create("test@example.com", "hashedPassword");
        user.setId(UUID.randomUUID());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByEmail("test@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty optional when email not found")
    void testFindUserByEmailNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should check if email exists")
    void testEmailExists() {
        // Arrange
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act
        boolean exists = userService.existsByEmail("existing@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should check if email does not exist")
    void testEmailNotExists() {
        // Arrange
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        // Act
        boolean exists = userService.existsByEmail("nonexistent@example.com");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should get user by ID or throw exception")
    void testGetUserByIdOrThrow() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.create("user@example.com", "hashedPassword");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByIdOrThrow(userId);

        // Assert
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user ID not found")
    void testGetUserByIdOrThrowNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByIdOrThrow(userId);
        });
    }
}
