-- V1__initial_schema.sql
-- Initial schema for ResumeForge Auth Service
-- This migration is idempotent (uses IF NOT EXISTS) so it is safe to run on an existing DB.

-- ─────────────────────────────────────────────────────────────
-- users table
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id            VARCHAR(36)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    active        TINYINT(1)   NOT NULL DEFAULT 1,
    email_verified TINYINT(1)  NOT NULL DEFAULT 0,
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add unique constraint on email (IF NOT EXISTS guard via procedure)
ALTER TABLE users
    MODIFY COLUMN email VARCHAR(255) NOT NULL;

-- Add updated_at column if it is missing (handles the case where the table
-- was previously created without it by the broken ddl-auto:create setup).
-- MySQL does not support ALTER TABLE ... ADD COLUMN IF NOT EXISTS in all versions,
-- so we use a stored procedure trick that is universally compatible.
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME   = 'users'
      AND COLUMN_NAME  = 'updated_at'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE users ADD COLUMN updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ─────────────────────────────────────────────────────────────
-- Indexes on users (use IF NOT EXISTS — MySQL 8.0+)
-- ─────────────────────────────────────────────────────────────
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email  ON users (email);
CREATE        INDEX IF NOT EXISTS idx_users_active ON users (active);

-- ─────────────────────────────────────────────────────────────
-- refresh_tokens table
-- ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id         VARCHAR(36)  NOT NULL,
    user_id    VARCHAR(36)  NOT NULL,
    token_hash TEXT         NOT NULL,
    expires_at DATETIME(6)  NOT NULL,
    revoked    TINYINT(1)   NOT NULL DEFAULT 0,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes on refresh_tokens
CREATE INDEX        IF NOT EXISTS idx_refresh_tokens_user_id    ON refresh_tokens (user_id);
CREATE INDEX        IF NOT EXISTS idx_refresh_tokens_expires_at ON refresh_tokens (expires_at);
CREATE INDEX        IF NOT EXISTS idx_refresh_tokens_active     ON refresh_tokens (user_id, revoked, expires_at);
-- token_hash is TEXT so we index a prefix (191 chars fits utf8mb4 B-Tree limit)
CREATE UNIQUE INDEX IF NOT EXISTS idx_refresh_tokens_token_hash ON refresh_tokens ((CAST(token_hash AS CHAR(191) CHARACTER SET utf8mb4)));
