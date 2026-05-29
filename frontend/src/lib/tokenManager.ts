/**
 * Token storage and management utilities
 *
 * Handles secure storage of access and refresh tokens.
 * - Access tokens: stored in memory only
 * - Refresh tokens: stored in localStorage for persistence
 */

const ACCESS_TOKEN_KEY = 'access_token_memory';
const REFRESH_TOKEN_KEY = 'refresh_token_storage';

interface TokenMemory {
  accessToken: string | null;
  timestamp: number;
}

// In-memory storage for access tokens (not persisted)
let tokenMemory: TokenMemory = {
  accessToken: null,
  timestamp: 0,
};

/**
 * Set access token in memory
 */
export const setAccessToken = (token: string): void => {
  tokenMemory = {
    accessToken: token,
    timestamp: Date.now(),
  };
};

/**
 * Get access token from memory
 */
export const getAccessToken = (): string | null => {
  return tokenMemory.accessToken;
};

/**
 * Clear access token from memory
 */
export const clearAccessToken = (): void => {
  tokenMemory = {
    accessToken: null,
    timestamp: 0,
  };
};

/**
 * Set refresh token in localStorage
 */
export const setRefreshToken = (token: string): void => {
  try {
    localStorage.setItem(REFRESH_TOKEN_KEY, token);
  } catch (error) {
    console.error('Failed to set refresh token:', error);
  }
};

/**
 * Get refresh token from localStorage
 */
export const getRefreshToken = (): string | null => {
  try {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
  } catch (error) {
    console.error('Failed to retrieve refresh token:', error);
    return null;
  }
};

/**
 * Clear refresh token from localStorage
 */
export const clearRefreshToken = (): void => {
  try {
    localStorage.removeItem(REFRESH_TOKEN_KEY);
  } catch (error) {
    console.error('Failed to clear refresh token:', error);
  }
};

/**
 * Clear all tokens
 */
export const clearAllTokens = (): void => {
  clearAccessToken();
  clearRefreshToken();
};

/**
 * Check if refresh token exists
 */
export const hasRefreshToken = (): boolean => {
  return getRefreshToken() !== null;
};

/**
 * Check if access token exists
 */
export const hasAccessToken = (): boolean => {
  return getAccessToken() !== null;
};
