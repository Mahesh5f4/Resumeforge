/**
 * Authentication API service
 */

import { apiClient } from './client';
import { AuthResponse, LoginCredentials, RegisterCredentials } from '../features/auth/types';

/**
 * Register a new user
 */
export const authApi = {
  register: async (credentials: RegisterCredentials): Promise<AuthResponse> => {
    const response = await apiClient.post<{ data: AuthResponse }>(
      '/register',
      {
        email: credentials.email,
        password: credentials.password,
      }
    );
    return response.data.data;
  },

  /**
   * Login user
   */
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    const response = await apiClient.post<{ data: AuthResponse }>(
      '/login',
      credentials
    );
    return response.data.data;
  },

  /**
   * Get current user
   */
  getCurrentUser: async (): Promise<AuthResponse> => {
    const response = await apiClient.get<{ data: AuthResponse }>('/me');
    return response.data.data;
  },

  /**
   * Refresh token
   */
  refreshToken: async (refreshToken: string): Promise<AuthResponse> => {
    const response = await apiClient.post<{ data: AuthResponse }>(
      '/refresh',
      { refreshToken }
    );
    return response.data.data;
  },

  /**
   * Logout
   */
  logout: async (): Promise<void> => {
    // Optional: call backend logout endpoint if needed
    // await apiClient.post('/logout');
  },
};

export default authApi;
