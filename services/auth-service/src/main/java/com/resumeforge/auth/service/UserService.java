package com.resumeforge.auth.service;

import com.resumeforge.auth.entity.User;
import com.resumeforge.auth.exception.UserNotFoundException;

import java.util.Optional;
import java.util.UUID;

/**
 * User service interface for user management operations.
 */
public interface UserService {

    /**
     * Find user by ID
     *
     * @param id the user ID
     * @return user if found
     */
    Optional<User> findById(UUID id);

    /**
     * Find user by email
     *
     * @param email the user email
     * @return user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by email
     *
     * @param email the user email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Get user by ID or throw exception
     *
     * @param id the user ID
     * @return the user
     * @throws UserNotFoundException if user not found
     */
    User getUserByIdOrThrow(UUID id) throws UserNotFoundException;
}
