package com.resumeforge.auth.repository;

import com.resumeforge.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for RefreshToken entity.
 *
 * Provides database access for refresh token management.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Find a refresh token by token hash
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * Find all refresh tokens for a user
     */
    java.util.List<RefreshToken> findByUserId(UUID userId);

    /**
     * Delete all refresh tokens for a user
     */
    void deleteByUserId(UUID userId);
}
