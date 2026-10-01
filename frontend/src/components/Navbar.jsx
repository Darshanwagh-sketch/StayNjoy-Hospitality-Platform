import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Hotel, LogOut, LayoutDashboard, Calendar } from 'lucide-react';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getDashboardPath = () => {
    if (!user) return '/login';
    if (user.role === 'SUPER_ADMIN') return '/dashboard/admin';
    if (user.role === 'HOTEL_ADMIN') return '/dashboard/hotel-admin';
    return '/dashboard/customer';
  };

  return (
    <nav className="navbar">
      <div className="container nav-container">
        <Link to="/" className="nav-logo">
          <Hotel className="w-8 h-8 logo-icon" />
          <span>StayNjoy</span>
        </Link>

        <div className="nav-links">
          <Link to="/" className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}>
            Explore Stays
          </Link>

          {user ? (
            <>
              <Link to={getDashboardPath()} className={`nav-link ${location.pathname.startsWith('/dashboard') ? 'active' : ''}`}>
                <LayoutDashboard className="w-4 h-4 inline mr-1.5" />
                Dashboard
              </Link>
              
              {user.role === 'CUSTOMER' && (
                <Link to="/dashboard/customer" className="nav-link">
                  <Calendar className="w-4 h-4 inline mr-1.5" />
                  My Bookings
                </Link>
              )}

              <div style={{ display: 'flex', alignItems: 'center', gap: '16px', paddingLeft: '16px', borderLeft: '1px solid rgba(255, 255, 255, 0.15)' }}>
                <div style={{ textAlign: 'right' }}>
                  <span style={{ fontSize: '0.9rem', fontWeight: 700, color: '#FFFFFF', display: 'block' }}>
                    {user.fullName}
                  </span>
                  <span style={{ fontSize: '0.72rem', fontWeight: 600, color: 'var(--cyan-accent)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                    {user.role}
                  </span>
                </div>
                <button onClick={handleLogout} className="btn btn-secondary btn-sm" style={{ background: 'rgba(255, 255, 255, 0.1)', color: '#FFFFFF', borderColor: 'rgba(255, 255, 255, 0.2)' }} title="Logout">
                  <LogOut className="w-4 h-4" />
                </button>
              </div>
            </>
          ) : (
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
              <Link to="/login" className="btn btn-secondary btn-sm" style={{ background: 'rgba(255, 255, 255, 0.08)', color: '#FFFFFF', borderColor: 'rgba(255, 255, 255, 0.2)' }}>
                Log In
              </Link>
              <Link to="/register" className="btn btn-primary btn-sm">
                Get Started
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
