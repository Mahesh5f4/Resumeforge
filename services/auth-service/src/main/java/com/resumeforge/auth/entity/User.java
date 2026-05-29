package com.resumeforge.auth.entity;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User entity representing a ResumeForge user account.
 *
 * Contains user authentication credentials and account status information.
 * Uses UUID for primary key for security and performance.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email", unique = true),
    @Index(name = "idx_users_active", columnList = "active")
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Factory method to create a new user
     */
    public static User create(String email, String passwordHash) {
        return User.builder()
            .id(Generators.timeBasedEpochGenerator().generate())
            .email(email)
            .passwordHash(passwordHash)
            .active(true)
            .emailVerified(false)
            .build();
    }

    /**
     * Check if user is active and verified
     */
    public boolean isEnabled() {
        return this.active && this.emailVerified;
    }
}
