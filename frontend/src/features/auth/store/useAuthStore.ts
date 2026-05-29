/**
 * Zustand authentication store
 *
 * Manages:
 * - Authentication state
 * - User session
 * - Token lifecycle
 * - Auth persistence
 */

import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { User, AuthResponse } from '../types';
import {
  setAccessToken,
  setRefreshToken,
  clearAllTokens,
  hasRefreshToken,
} from '../../../lib/tokenManager';
import authApi from '../../../api/auth';

interface AuthStore {
  // State
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isInitialized: boolean;
  error: string | null;

  // Actions
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  initializeAuth: () => Promise<void>;
  clearError: () => void;
  setLoading: (loading: boolean) => void;
}

/**
 * Create authentication store
 */
export const useAuthStore = create<AuthStore>()(
  persist(
    (set, get) => ({
      // Initial state
      user: null,
      isAuthenticated: false,
      isLoading: false,
      isInitialized: false,
      error: null,

      /**
       * Login action
       */
      login: async (email: string, password: string) => {
        set({ isLoading: true, error: null });
        try {
          const response = await authApi.login({ email, password });
          handleAuthResponse(response);
          set({
            user: response.user,
            isAuthenticated: true,
            isLoading: false,
          });
        } catch (error) {
          const errorMessage =
            error instanceof Error ? error.message : 'Login failed';
          set({
            error: errorMessage,
            isLoading: false,
            isAuthenticated: false,
            user: null,
          });
          throw error;
        }
      },

      /**
       * Register action
       */
      register: async (email: string, password: string) => {
        set({ isLoading: true, error: null });
        try {
          const response = await authApi.register({
            email,
            password,
            passwordConfirm: password,
          });
          handleAuthResponse(response);
          set({
            user: response.user,
            isAuthenticated: true,
            isLoading: false,
          });
        } catch (error) {
          const errorMessage =
            error instanceof Error ? error.message : 'Registration failed';
          set({
            error: errorMessage,
            isLoading: false,
            isAuthenticated: false,
            user: null,
          });
          throw error;
        }
      },

      /**
       * Logout action
       */
      logout: async () => {
        set({ isLoading: true });
        try {
          await authApi.logout();
        } catch (error) {
          console.error('Logout error:', error);
        } finally {
          clearAllTokens();
          set({
            user: null,
            isAuthenticated: false,
            isLoading: false,
            error: null,
          });
        }
      },

      /**
       * Initialize authentication on app load
       * Recovers session from persisted state and validates refresh token
       */
      initializeAuth: async () => {
        set({ isLoading: true });
        try {
          // Check if refresh token exists
          if (hasRefreshToken()) {
            try {
              const response = await authApi.getCurrentUser();
              handleAuthResponse(response);
              set({
                user: response.user,
                isAuthenticated: true,
                isInitialized: true,
                isLoading: false,
              });
              return;
            } catch (error) {
              // Refresh token invalid, clear and logout
              clearAllTokens();
            }
          }

          set({
            user: null,
            isAuthenticated: false,
            isInitialized: true,
            isLoading: false,
          });
        } catch (error) {
          set({
            user: null,
            isAuthenticated: false,
            isInitialized: true,
            isLoading: false,
          });
        }
      },

      /**
       * Clear error message
       */
      clearError: () => {
        set({ error: null });
      },

      /**
       * Set loading state
       */
      setLoading: (loading: boolean) => {
        set({ isLoading: loading });
      },
    }),
    {
      name: 'auth-store',
      // Only persist user and isAuthenticated for recovery on reload
      partialize: (state) => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);

/**
 * Handle authentication response
 * Extract and store tokens from response
 */
function handleAuthResponse(response: AuthResponse): void {
  setAccessToken(response.access_token);
  setRefreshToken(response.refresh_token);
}
