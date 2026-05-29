import React from 'react';

interface AuthLayoutProps {
  children: React.ReactNode;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({ children }) => {
  return (
    <div className="auth-bg flex min-h-screen items-center justify-center p-4">
      {/* Animated background orbs */}
      <div
        className="pointer-events-none absolute top-1/4 left-1/4 h-[400px] w-[400px] rounded-full opacity-20 blur-[80px]"
        style={{ background: 'radial-gradient(circle, #06b6d4 0%, transparent 70%)', animation: 'float 8s ease-in-out infinite' }}
      />
      <div
        className="pointer-events-none absolute bottom-1/4 right-1/4 h-[500px] w-[500px] rounded-full opacity-[0.15] blur-[100px]"
        style={{ background: 'radial-gradient(circle, #10b981 0%, transparent 70%)', animation: 'float 12s ease-in-out infinite reverse' }}
      />

      {/* Brand top-left */}
      <div className="animate-fade-in absolute left-8 top-8 flex items-center gap-2.5">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-white/5 border border-white/10 shadow-[0_0_15px_rgba(6,182,212,0.15)]">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M9 12h6M9 16h6M9 8h3M5 3h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z" stroke="#06b6d4" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </div>
        <span className="text-sm font-semibold tracking-wide text-zinc-100">ResumeForge</span>
      </div>

      {/* Main content */}
      <div className="relative w-full max-w-md">
        {children}
      </div>
    </div>
  );
};

export default AuthLayout;
