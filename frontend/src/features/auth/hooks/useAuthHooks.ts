/**
 * Custom hooks for authentication features
 */

import { useCallback } from 'react';
import { useAuthStore } from '../store/useAuthStore';

/**
 * Hook for accessing authentication state and actions
 */
export const useAuth = () => {
  return useAuthStore();
};

/**
 * Hook for login functionality
 */
export const useLogin = () => {
  const login = useAuthStore((state) => state.login);
  const isLoading = useAuthStore((state) => state.isLoading);
  const error = useAuthStore((state) => state.error);
  const clearError = useAuthStore((state) => state.clearError);

  const handleLogin = useCallback(
    async (email: string, password: string) => {
      clearError();
      await login(email, password);
    },
    [login, clearError]
  );

  return {
    login: handleLogin,
    isLoading,
    error,
    clearError,
  };
};

/**
 * Hook for register functionality
 */
export const useRegister = () => {
  const register = useAuthStore((state) => state.register);
  const isLoading = useAuthStore((state) => state.isLoading);
  const error = useAuthStore((state) => state.error);
  const clearError = useAuthStore((state) => state.clearError);

  const handleRegister = useCallback(
    async (email: string, password: string) => {
      clearError();
      await register(email, password);
    },
    [register, clearError]
  );

  return {
    register: handleRegister,
    isLoading,
    error,
    clearError,
  };
};

/**
 * Hook for logout functionality
 */
export const useLogout = () => {
  const logout = useAuthStore((state) => state.logout);
  const isLoading = useAuthStore((state) => state.isLoading);

  const handleLogout = useCallback(async () => {
    await logout();
  }, [logout]);

  return {
    logout: handleLogout,
    isLoading,
  };
};

/**
 * Hook for checking authentication status
 */
export const useIsAuthenticated = () => {
  return useAuthStore((state) => state.isAuthenticated);
};

/**
 * Hook for getting current user
 */
export const useCurrentUser = () => {
  return useAuthStore((state) => state.user);
};
