import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { AuthLayout } from '../../../layouts/AuthLayout';
import { useRegister } from '../hooks/useAuthHooks';
import { registerSchema, RegisterFormData } from '../schemas';

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

const CheckIcon = ({ ok }: { ok: boolean }) => (
  <svg width="14" height="14" fill="none" viewBox="0 0 24 24" className={ok ? 'text-emerald-500' : 'text-zinc-600'}>
    <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth={1.5}/>
    {ok && <path d="M9 12l2 2 4-4" stroke="currentColor" strokeWidth={1.5} strokeLinecap="round" strokeLinejoin="round"/>}
  </svg>
);

const getStrength = (password: string) => {
  let score = 0;
  if (password.length >= 8) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[a-z]/.test(password)) score++;
  if (/\d/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;
  return score;
};

const strengthConfig = [
  { label: 'Weak', color: '#ef4444' },
  { label: 'Fair', color: '#f97316' },
  { label: 'Good', color: '#eab308' },
  { label: 'Strong', color: '#10b981' },
  { label: 'Very Strong', color: '#059669' },
];

export const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const { register: registerUser, isLoading, error, clearError } = useRegister();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [passwordValue, setPasswordValue] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const strength = getStrength(passwordValue);
  const strengthInfo = strengthConfig[Math.max(0, strength - 1)] ?? strengthConfig[0];

  const requirements = [
    { label: '8+ characters', ok: passwordValue.length >= 8 },
    { label: 'Uppercase', ok: /[A-Z]/.test(passwordValue) },
    { label: 'Lowercase', ok: /[a-z]/.test(passwordValue) },
    { label: 'Number', ok: /\d/.test(passwordValue) },
  ];

  const onSubmit = async (data: RegisterFormData) => {
    clearError();
    try {
      await registerUser(data.email, data.password);
      navigate('/dashboard', { replace: true });
    } catch {
      // error is in store
    }
  };

  return (
    <AuthLayout>
      <div className="glass-card animate-fade-in-up rounded-[20px] p-8 md:p-10">
        {/* Header */}
        <div className="mb-7 text-center">
          <div className="mx-auto mb-5 flex h-14 w-14 items-center justify-center rounded-2xl bg-cyan-500/10 ring-1 ring-cyan-500/30 animate-pulse-glow shadow-[0_0_20px_rgba(6,182,212,0.15)]">
            <svg width="24" height="24" fill="none" viewBox="0 0 24 24">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14H9V8h2v8zm4 0h-2V8h2v8z" fill="#06b6d4"/>
            </svg>
          </div>
          <h1 className="animate-fade-in-up delay-100 text-2xl font-bold tracking-tight text-zinc-100">Create account</h1>
          <p className="animate-fade-in-up delay-200 mt-2 text-sm text-zinc-400">Start optimizing your resume today</p>
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
              id="register-email"
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
            <label className="mb-2.5 block text-xs font-semibold uppercase tracking-wider text-zinc-400">Password</label>
            <div className="relative">
              <input
                id="register-password"
                type={showPassword ? 'text' : 'password'}
                autoComplete="new-password"
                placeholder="Choose a strong password"
                className={`auth-input pr-12 ${errors.password ? 'error' : ''}`}
                {...register('password', {
                  onChange: (e) => setPasswordValue(e.target.value),
                })}
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

            {/* Strength meter */}
            {passwordValue.length > 0 && (
              <div className="mt-4 space-y-3">
                <div className="flex gap-2">
                  {[1, 2, 3, 4, 5].map((level) => (
                    <div key={level} className="h-1.5 flex-1 rounded-full bg-zinc-800 overflow-hidden">
                      <div
                        className="strength-bar h-full rounded-full"
                        style={{
                          width: strength >= level ? '100%' : '0%',
                          background: strength >= level ? strengthInfo.color : 'transparent',
                        }}
                      />
                    </div>
                  ))}
                </div>
                <div className="flex items-center justify-between">
                  <div className="flex flex-wrap gap-x-4 gap-y-2">
                    {requirements.map((req) => (
                      <div key={req.label} className="flex items-center gap-1.5">
                        <CheckIcon ok={req.ok} />
                        <span className={`text-[11px] font-medium tracking-wide ${req.ok ? 'text-zinc-300' : 'text-zinc-500'}`}>{req.label}</span>
                      </div>
                    ))}
                  </div>
                  <span className="ml-3 shrink-0 text-[11px] font-bold uppercase tracking-wider" style={{ color: strengthInfo.color }}>
                    {strengthInfo.label}
                  </span>
                </div>
              </div>
            )}
          </div>

          {/* Confirm Password */}
          <div className="animate-fade-in-up delay-400">
            <label className="mb-2.5 block text-xs font-semibold uppercase tracking-wider text-zinc-400">Confirm Password</label>
            <div className="relative">
              <input
                id="register-confirm-password"
                type={showConfirm ? 'text' : 'password'}
                autoComplete="new-password"
                placeholder="Repeat your password"
                className={`auth-input pr-12 ${errors.passwordConfirm ? 'error' : ''}`}
                {...register('passwordConfirm')}
              />
              <button
                type="button"
                className="absolute right-4 top-1/2 -translate-y-1/2 text-zinc-500 transition-colors hover:text-zinc-300"
                onClick={() => setShowConfirm((v) => !v)}
                tabIndex={-1}
              >
                <EyeIcon open={showConfirm} />
              </button>
            </div>
            {errors.passwordConfirm && (
              <p className="mt-2 text-xs font-medium text-red-400">{errors.passwordConfirm.message}</p>
            )}
          </div>

          {/* Terms */}
          <p className="animate-fade-in-up delay-400 pt-1 text-[11px] font-medium text-zinc-500 leading-relaxed">
            By creating an account, you agree to our{' '}
            <a href="#" className="text-cyan-500 hover:text-cyan-400 transition-colors">Terms of Service</a>{' '}
            and{' '}
            <a href="#" className="text-cyan-500 hover:text-cyan-400 transition-colors">Privacy Policy</a>.
          </p>

          {/* Submit */}
          <div className="animate-fade-in-up delay-500 pt-2">
            <button
              id="register-submit"
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
                  Creating account...
                </>
              ) : (
                'Create account'
              )}
            </button>
          </div>
        </form>

        {/* Footer */}
        <p className="animate-fade-in-up delay-500 mt-8 text-center text-sm text-zinc-400">
          Already have an account?{' '}
          <Link to="/login" className="font-semibold text-zinc-200 transition-colors hover:text-cyan-400">
            Sign in
          </Link>
        </p>
      </div>
    </AuthLayout>
  );
};

export default RegisterPage;
