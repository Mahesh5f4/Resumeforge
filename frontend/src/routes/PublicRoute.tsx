/**
 * Public route component
 * Redirects authenticated users away from auth pages
 */

import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../features/auth/store/useAuthStore';

export const PublicRoute = () => {
  const { isAuthenticated, isInitialized } = useAuthStore();

  // Show nothing while initializing auth state
  if (!isInitialized) {
    return null;
  }

  // Redirect to dashboard if already authenticated
  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};
