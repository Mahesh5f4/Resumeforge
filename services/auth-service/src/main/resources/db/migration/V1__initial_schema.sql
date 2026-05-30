-- V1__initial_schema.sql
-- Initial schema for ResumeForge Auth Service
-- Safe to run on a fresh DB or one previously touched by ddl-auto:create.
-- All statements use IF NOT EXISTS so this migration is fully idempotent.

-- ─────────────────────────────────────────────────────────────
-- users
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id             VARCHAR(36)  NOT NULL,
    email          VARCHAR(255) NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    active         TINYINT(1)   NOT NULL DEFAULT 1,
    email_verified TINYINT(1)   NOT NULL DEFAULT 0,
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY idx_users_email (email),
    KEY idx_users_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add updated_at if it was missing from an older schema (ddl-auto:create without it)
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6);

-- ─────────────────────────────────────────────────────────────
-- refresh_tokens
-- Using VARCHAR(512) for token_hash so a normal B-Tree index works
-- without needing a functional / prefix expression.
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id         VARCHAR(36)  NOT NULL,
    user_id    VARCHAR(36)  NOT NULL,
    token_hash VARCHAR(512) NOT NULL,
    expires_at DATETIME(6)  NOT NULL,
    revoked    TINYINT(1)   NOT NULL DEFAULT 0,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY idx_refresh_tokens_token_hash (token_hash(191)),
    KEY idx_refresh_tokens_user_id    (user_id),
    KEY idx_refresh_tokens_expires_at (expires_at),
    KEY idx_refresh_tokens_active     (user_id, revoked, expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
