package com.resumeforge.auth.entity;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Refresh token entity for JWT refresh token persistence.
 *
 * Stores refresh tokens with expiration timestamps and revocation support.
 * Enables token rotation and revocation strategies.
 */
@Entity
@Table(name = "refresh_tokens", indexes = {
    @Index(name = "idx_refresh_tokens_user_id", columnList = "user_id"),
    @Index(name = "idx_refresh_tokens_token_hash", columnList = "token_hash", unique = true),
    @Index(name = "idx_refresh_tokens_expires_at", columnList = "expires_at"),
    @Index(name = "idx_refresh_tokens_active", columnList = "user_id, revoked, expires_at")
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "token_hash", nullable = false, unique = true, length = 512)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean revoked = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Factory method to create a new refresh token
     */
    public static RefreshToken create(UUID userId, String tokenHash, LocalDateTime expiresAt) {
        return RefreshToken.builder()
            .id(Generators.timeBasedEpochGenerator().generate())
            .userId(userId)
            .tokenHash(tokenHash)
            .expiresAt(expiresAt)
            .revoked(false)
            .build();
    }

    /**
     * Check if token is valid (not revoked and not expired)
     */
    public boolean isValid() {
        return !revoked && expiresAt.isAfter(LocalDateTime.now());
    }

    /**
     * Revoke the token
     */
    public void revoke() {
        this.revoked = true;
    }
}
