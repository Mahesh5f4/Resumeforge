/**
 * Protected route component
 * Redirects unauthenticated users to login page
 */

import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../features/auth/store/useAuthStore';

export const ProtectedRoute = () => {
  const { isAuthenticated, isInitialized } = useAuthStore();

  // Show nothing while initializing auth state
  if (!isInitialized) {
    return null;
  }

  // Redirect to login if not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};
