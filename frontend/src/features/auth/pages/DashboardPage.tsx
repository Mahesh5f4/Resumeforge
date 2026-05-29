import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth, useLogout } from '../../auth/hooks/useAuthHooks';

const StatCard = ({ label, value, icon }: { label: string; value: string; icon: React.ReactNode }) => (
  <div className="glass-card rounded-[16px] p-5 flex items-center gap-4 transition-transform hover:-translate-y-0.5">
    <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-[12px] bg-cyan-500/10 text-cyan-400 shadow-[inset_0_0_12px_rgba(6,182,212,0.1)] ring-1 ring-cyan-500/20">
      {icon}
    </div>
    <div>
      <p className="text-[11px] font-semibold uppercase tracking-wider text-zinc-400">{label}</p>
      <p className="mt-1 text-lg font-bold tracking-tight text-zinc-100">{value}</p>
    </div>
  </div>
);

export const DashboardPage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const { logout, isLoading } = useLogout();

  const handleLogout = async () => {
    await logout();
    navigate('/login', { replace: true });
  };

  const initials = user?.email
    ? user.email.slice(0, 2).toUpperCase()
    : 'RF';

  const memberSince = user?.created_at
    ? new Date(user.created_at).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })
    : '—';

  return (
    <div className="auth-bg min-h-screen">
      {/* Nav */}
      <header className="border-b border-white/[0.04] bg-zinc-950/40 backdrop-blur-xl">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-6 py-4">
          <div className="flex items-center gap-2.5">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-white/5 border border-white/10 shadow-[0_0_15px_rgba(6,182,212,0.15)]">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M9 12h6M9 16h6M9 8h3M5 3h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z" stroke="#06b6d4" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </div>
            <span className="font-semibold tracking-wide text-zinc-100">ResumeForge</span>
          </div>
          <button
            id="logout-btn"
            onClick={handleLogout}
            disabled={isLoading}
            className="flex items-center gap-2 rounded-lg border border-white/10 bg-white/5 px-4 py-2 text-sm font-medium text-zinc-300 transition-all hover:border-white/20 hover:bg-white/10 hover:text-white disabled:opacity-50"
          >
            {isLoading ? (
              <svg className="h-4 w-4 animate-spin text-zinc-300" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 0 1 8-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
            ) : (
              <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 0 1-3 3H6a3 3 0 0 1-3-3V7a3 3 0 0 1 3-3h4a3 3 0 0 1 3 3v1"/>
              </svg>
            )}
            Sign out
          </button>
        </div>
      </header>

      {/* Main content */}
      <main className="mx-auto max-w-5xl px-6 py-10 space-y-8">
        {/* Welcome banner */}
        <div className="animate-fade-in-up glass-card rounded-[24px] p-8 flex items-center gap-6">
          {/* Avatar */}
          <div className="relative flex h-16 w-16 shrink-0 items-center justify-center rounded-[16px] bg-gradient-to-br from-cyan-600 to-cyan-900 text-xl font-bold text-white shadow-[0_0_30px_rgba(6,182,212,0.3)] ring-1 ring-white/20">
            {initials}
            <div className="absolute -bottom-1.5 -right-1.5 h-4 w-4 rounded-full bg-emerald-500 ring-4 ring-zinc-900 shadow-[0_0_10px_rgba(16,185,129,0.5)]"></div>
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium text-zinc-400 mb-1">Welcome back 👋</p>
            <h1 className="text-2xl font-bold tracking-tight text-zinc-100 truncate">{user?.email ?? 'User'}</h1>
            <div className="mt-2.5 flex items-center gap-4 flex-wrap">
              <span className="flex items-center gap-2 text-xs font-medium text-zinc-400">
                <span className="h-1.5 w-1.5 rounded-full bg-emerald-400 shadow-[0_0_8px_#34d399]"/>
                Session active
              </span>
              <span className="text-xs font-medium text-zinc-500">Member since {memberSince}</span>
              {user?.email_verified && (
                <span className="flex items-center gap-1.5 rounded-full bg-emerald-500/10 px-2.5 py-1 text-[11px] font-semibold tracking-wide text-emerald-400 ring-1 ring-emerald-500/20">
                  <svg width="12" height="12" fill="none" viewBox="0 0 24 24">
                    <path d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" stroke="#34d399" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round"/>
                  </svg>
                  Verified
                </span>
              )}
            </div>
          </div>
        </div>

        {/* Stats row */}
        <div className="animate-fade-in-up delay-100 grid grid-cols-1 gap-5 sm:grid-cols-3">
          <StatCard
            label="Account Status"
            value={user?.active ? 'Active' : 'Inactive'}
            icon={<svg width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><circle cx="12" cy="12" r="10"/><path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4l3 3"/></svg>}
          />
          <StatCard
            label="User ID"
            value={user?.id ? `${user.id.slice(0, 8)}…` : '—'}
            icon={<svg width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><rect x="2" y="7" width="20" height="14" rx="3"/><path strokeLinecap="round" strokeLinejoin="round" d="M16 7V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2"/></svg>}
          />
          <StatCard
            label="Email Verification"
            value={user?.email_verified ? 'Verified' : 'Pending'}
            icon={<svg width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}><path strokeLinecap="round" strokeLinejoin="round" d="M3 8l7.89 5.26a2 2 0 0 0 2.22 0L21 8M5 19h14a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2z"/></svg>}
          />
        </div>

        {/* Coming soon card */}
        <div className="animate-fade-in-up delay-200 glass-card rounded-[24px] p-10 text-center relative overflow-hidden">
          <div className="absolute inset-0 bg-gradient-to-br from-cyan-500/5 to-emerald-500/5"></div>
          <div className="relative z-10">
            <div className="mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-[16px] bg-cyan-500/10 ring-1 ring-cyan-500/30 animate-pulse-glow shadow-[0_0_25px_rgba(6,182,212,0.2)]">
              <svg width="28" height="28" fill="none" viewBox="0 0 24 24">
                <path d="M9 12h6M9 16h6M9 8h3M5 3h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z" stroke="#06b6d4" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </div>
            <h2 className="text-xl font-bold tracking-tight text-zinc-100">Precision ATS Optimization</h2>
            <p className="mt-3 text-sm text-zinc-400 max-w-md mx-auto leading-relaxed">
              Upload your resume and get AI-powered ATS analysis, precise keyword optimization, and professional rewrite suggestions.
            </p>
            <div className="mt-8 flex justify-center gap-3">
              <span className="rounded-full bg-cyan-500/10 px-4 py-1.5 text-xs font-semibold tracking-wide text-cyan-400 ring-1 ring-cyan-500/20 shadow-[0_0_10px_rgba(6,182,212,0.1)]">ATS Analysis</span>
              <span className="rounded-full bg-emerald-500/10 px-4 py-1.5 text-xs font-semibold tracking-wide text-emerald-400 ring-1 ring-emerald-500/20 shadow-[0_0_10px_rgba(16,185,129,0.1)]">AI Rewrites</span>
              <span className="rounded-full bg-zinc-500/10 px-4 py-1.5 text-xs font-semibold tracking-wide text-zinc-300 ring-1 ring-zinc-500/20 shadow-[0_0_10px_rgba(161,161,170,0.1)]">Score Reports</span>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default DashboardPage;
