import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import { UserPlus, Hotel } from 'lucide-react';

const Register = () => {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('CUSTOMER');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const user = await register({ fullName, email, phone, password, role });
      if (user.role === 'SUPER_ADMIN') navigate('/dashboard/admin');
      else if (user.role === 'HOTEL_ADMIN') navigate('/dashboard/hotel-admin');
      else navigate('/dashboard/customer');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed. Email may already exist.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ minHeight: 'calc(100vh - 160px)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '60px 0' }}>
      <div className="saas-card" style={{ width: '100%', maxWidth: '500px', padding: '40px' }}>
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{ width: '56px', height: '56px', borderRadius: '16px', background: 'var(--navy-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px', boxShadow: 'var(--shadow-navy)' }}>
            <Hotel className="w-7 h-7" style={{ color: 'var(--cyan-accent)' }} />
          </div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '6px' }}>Create Account</h1>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Join StaynJoy luxury booking ecosystem</p>
        </div>

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #FCA5A5', color: 'var(--error)', padding: '12px', borderRadius: '10px', fontSize: '0.9rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Full Name</label>
            <input 
              type="text" 
              className="form-control" 
              placeholder="Aarav Sharma" 
              value={fullName} 
              onChange={(e) => setFullName(e.target.value)} 
              required 
            />
          </div>

          <div className="form-group">
            <label className="form-label">Email Address</label>
            <input 
              type="email" 
              className="form-control" 
              placeholder="aarav@example.com" 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              required 
            />
          </div>

          <div className="form-group">
            <label className="form-label">Phone Number</label>
            <input 
              type="text" 
              className="form-control" 
              placeholder="+91-9876543210" 
              value={phone} 
              onChange={(e) => setPhone(e.target.value)} 
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password</label>
            <input 
              type="password" 
              className="form-control" 
              placeholder="At least 6 characters" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              required 
              minLength="6"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Account Role</label>
            <select className="form-control" value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="CUSTOMER">Guest / Customer (Book Rooms)</option>
              <option value="HOTEL_ADMIN">Hotel Manager / Property Owner</option>
            </select>
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '8px' }} disabled={loading}>
            <UserPlus className="w-4 h-4 mr-1" />
            {loading ? 'Creating Account...' : 'Register Account'}
          </button>
        </form>

        <p style={{ marginTop: '24px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
          Already registered? <Link to="/login" style={{ color: 'var(--electric-blue)', fontWeight: 600 }}>Sign In Here</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;
