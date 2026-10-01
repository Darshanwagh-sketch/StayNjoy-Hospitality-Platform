import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import { LogIn, Hotel } from 'lucide-react';

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const user = await login({ email, password });
      if (user.role === 'SUPER_ADMIN') navigate('/dashboard/admin');
      else if (user.role === 'HOTEL_ADMIN') navigate('/dashboard/hotel-admin');
      else navigate('/dashboard/customer');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid email or password');
    } finally {
      setLoading(false);
    }
  };

  const handleQuickLogin = (demoEmail, demoPassword) => {
    setEmail(demoEmail);
    setPassword(demoPassword);
  };

  return (
    <div className="container" style={{ minHeight: 'calc(100vh - 160px)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '60px 0' }}>
      <div className="saas-card" style={{ width: '100%', maxWidth: '460px', padding: '40px' }}>
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '16px', background: 'var(--navy-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px', boxShadow: 'var(--shadow-navy)' }}>
            <Hotel className="w-7 h-7" style={{ color: 'var(--cyan-accent)' }} />
          </div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '6px' }}>Welcome Back</h1>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Sign in to manage your luxury stay & reservations</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #FCA5A5', color: 'var(--error)', padding: '12px', borderRadius: '10px', fontSize: '0.9rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Email Address</label>
            <input 
              type="email" 
              className="form-control" 
              placeholder="john@example.com" 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              required 
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password</label>
            <input 
              type="password" 
              className="form-control" 
              placeholder="••••••••" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              required 
            />
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '8px' }} disabled={loading}>
            <LogIn className="w-4 h-4 mr-1" />
            {loading ? 'Authenticating...' : 'Sign In'}
          </button>
        </form>

        <div style={{ marginTop: '28px', paddingTop: '20px', borderTop: '1px solid var(--border-color)' }}>
          <p style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.5px', marginBottom: '12px', textAlign: 'center' }}>
            Quick Demo Auto-Fill:
          </p>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '8px' }}>
            <button type="button" onClick={() => handleQuickLogin('john@example.com', 'user123')} className="btn btn-secondary btn-sm" style={{ fontSize: '0.75rem' }}>
              Customer
            </button>
            <button type="button" onClick={() => handleQuickLogin('hoteladmin@tajlake.com', 'admin123')} className="btn btn-secondary btn-sm" style={{ fontSize: '0.75rem' }}>
              Hotel Admin
            </button>
            <button type="button" onClick={() => handleQuickLogin('admin@hospitality.com', 'admin123')} className="btn btn-secondary btn-sm" style={{ fontSize: '0.75rem' }}>
              Super Admin
            </button>
          </div>
        </div>

        <p style={{ marginTop: '24px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
          Don't have an account? <Link to="/register" style={{ color: 'var(--electric-blue)', fontWeight: 600 }}>Create One</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
