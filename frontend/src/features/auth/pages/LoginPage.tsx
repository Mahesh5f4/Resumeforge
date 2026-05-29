import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AuthLayout } from '../../../layouts/AuthLayout';
import { useLogin } from '../hooks/useAuthHooks';
import { loginSchema, LoginFormData } from '../schemas';

const EyeIcon = ({ open }: { open: boolean }) =>
  open ? (
    <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
      <path strokeLinecap="round" strokeLinejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
      <path strokeLinecap="round" strokeLinejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
    </svg>
  ) : (
    <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
      <path strokeLinecap="round" strokeLinejoin="round" d="M13.875 18.825A10.05 10.05 0 0 1 12 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 0 1 4.5-5.58M9.88 9.88A3 3 0 1 0 14.12 14.12M3 3l18 18"/>
    </svg>
  );

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { login, isLoading, error, clearError } = useLogin();
  const [showPassword, setShowPassword] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: LoginFormData) => {
    clearError();
    try {
      await login(data.email, data.password);
      navigate('/dashboard', { replace: true });
    } catch {
      // error is in store
    }
  };

  return (
    <AuthLayout>
      <div className="glass-card animate-fade-in-up rounded-[20px] p-8 md:p-10">
        {/* Header */}
        <div className="mb-8 text-center">
          <div className="mx-auto mb-5 flex h-14 w-14 items-center justify-center rounded-2xl bg-cyan-500/10 ring-1 ring-cyan-500/30 animate-pulse-glow shadow-[0_0_20px_rgba(6,182,212,0.15)]">
            <svg width="24" height="24" fill="none" viewBox="0 0 24 24">
              <path d="M12 2a5 5 0 1 0 0 10A5 5 0 0 0 12 2zm0 12c-5.33 0-8 2.67-8 4v2h16v-2c0-1.33-2.67-4-8-4z" fill="#06b6d4"/>
            </svg>
          </div>
          <h1 className="animate-fade-in-up delay-100 text-2xl font-bold tracking-tight text-zinc-100">Welcome back</h1>
          <p className="animate-fade-in-up delay-200 mt-2 text-sm text-zinc-400">Sign in to your ResumeForge account</p>
        </div>

        {/* Error Banner */}
        {error && (
          <div className="animate-fade-in mb-6 flex items-center gap-3 rounded-xl border border-red-500/20 bg-red-500/5 px-4 py-3">
            <svg width="16" height="16" fill="none" viewBox="0 0 24 24" className="shrink-0 text-red-500">
              <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth={1.5}/>
              <path d="M12 8v4M12 16h.01" stroke="currentColor" strokeWidth={2} strokeLinecap="round"/>
            </svg>
            <p className="text-sm font-medium text-red-400">{error}</p>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
          {/* Email */}
          <div className="animate-fade-in-up delay-200">
            <label className="mb-2.5 block text-xs font-semibold uppercase tracking-wider text-zinc-400">Email Address</label>
            <input
              id="login-email"
              type="email"
              autoComplete="email"
              placeholder="you@example.com"
              className={`auth-input ${errors.email ? 'error' : ''}`}
              {...register('email')}
            />
            {errors.email && (
              <p className="mt-2 text-xs font-medium text-red-400">{errors.email.message}</p>
            )}
          </div>

          {/* Password */}
          <div className="animate-fade-in-up delay-300">
            <div className="mb-2.5 flex items-center justify-between">
              <label className="text-xs font-semibold uppercase tracking-wider text-zinc-400">Password</label>
              <a href="#" className="text-xs font-medium text-cyan-500 hover:text-cyan-400 transition-colors">Forgot password?</a>
            </div>
            <div className="relative">
              <input
                id="login-password"
                type={showPassword ? 'text' : 'password'}
                autoComplete="current-password"
                placeholder="••••••••"
                className={`auth-input pr-12 ${errors.password ? 'error' : ''}`}
                {...register('password')}
              />
              <button
                type="button"
                className="absolute right-4 top-1/2 -translate-y-1/2 text-zinc-500 transition-colors hover:text-zinc-300"
                onClick={() => setShowPassword((v) => !v)}
                tabIndex={-1}
              >
                <EyeIcon open={showPassword} />
              </button>
            </div>
            {errors.password && (
              <p className="mt-2 text-xs font-medium text-red-400">{errors.password.message}</p>
            )}
          </div>

          {/* Submit */}
          <div className="animate-fade-in-up delay-400 pt-3">
            <button
              id="login-submit"
              type="submit"
              disabled={isLoading}
              className="btn-primary flex items-center justify-center gap-2.5 w-full"
            >
              {isLoading ? (
                <>
                  <svg className="h-4 w-4 animate-spin text-zinc-900" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 0 1 8-8V0C5.373 0 0 5.373 0 12h4z"/>
                  </svg>
                  Authenticating...
                </>
              ) : (
                'Sign in to Dashboard'
              )}
            </button>
          </div>
        </form>

        {/* Footer */}
        <p className="animate-fade-in-up delay-500 mt-8 text-center text-sm text-zinc-400">
          Don&apos;t have an account?{' '}
          <Link to="/register" className="font-semibold text-zinc-200 transition-colors hover:text-cyan-400">
            Create one
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
};

export default LoginPage;
