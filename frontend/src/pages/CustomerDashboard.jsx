import React, { useState, useEffect } from 'react';
import { bookingAPI, dashboardAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { MapPin } from 'lucide-react';

const CustomerDashboard = () => {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const [bookingsRes, statsRes] = await Promise.all([
        bookingAPI.getMyBookings(),
        dashboardAPI.getCustomerStats(),
      ]);
      setBookings(bookingsRes.data);
      setStats(statsRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleCancelBooking = async (id) => {
    if (!window.confirm('Are you sure you want to cancel this reservation?')) return;
    try {
      await bookingAPI.cancel(id);
      fetchData();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to cancel booking');
    }
  };

  if (loading) {
    return <div className="container" style={{ padding: '80px 0', textAlign: 'center', color: 'var(--text-muted)' }}>Loading reservation history...</div>;
  }

  return (
    <div className="container" style={{ paddingTop: '40px' }}>
      <div style={{ marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2.2rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '4px' }}>Welcome Back, {user?.fullName}</h1>
        <p style={{ color: 'var(--text-muted)' }}>Manage your hotel reservations and stay activity</p>
      </div>

      {/* Stats Cards */}
      {stats && (
        <div className="grid-stats">
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Total Reservations</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginTop: '6px' }}>{stats.totalBookings}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Upcoming Stays</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--electric-blue)', marginTop: '6px' }}>{stats.upcomingBookings}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Completed Stays</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--success)', marginTop: '6px' }}>{stats.completedBookings}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Cancelled</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--error)', marginTop: '6px' }}>{stats.cancelledBookings}</div>
          </div>
        </div>
      )}

      {/* Bookings List */}
      <h2 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '20px' }}>My Reservations</h2>
      
      {bookings.length === 0 ? (
        <div className="saas-card" style={{ padding: '40px', textAlign: 'center', color: 'var(--text-muted)' }}>
          You don't have any bookings yet. Browse our hotels to reserve your next stay!
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {bookings.map((booking) => (
            <div key={booking.id} className="saas-card" style={{ padding: '28px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
              <div style={{ flex: 1, minWidth: '280px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '8px' }}>
                  <span className="badge badge-blue">{booking.bookingCode}</span>
                  <span className={`badge ${booking.status === 'CONFIRMED' ? 'badge-success' : booking.status === 'CANCELLED' ? 'badge-danger' : 'badge-warning'}`}>
                    {booking.status}
                  </span>
                </div>

                <h3 style={{ fontSize: '1.3rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '4px' }}>{booking.hotelName}</h3>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '12px' }}>
                  <MapPin className="w-4 h-4 text-blue-600" /> {booking.hotelCity} • Room #{booking.roomNumber} ({booking.roomType})
                </p>

                <div style={{ display: 'flex', gap: '24px', fontSize: '0.9rem', color: 'var(--text-main)' }}>
                  <span>📅 Check-in: <strong>{booking.checkInDate}</strong></span>
                  <span>📅 Check-out: <strong>{booking.checkOutDate}</strong></span>
                </div>
              </div>

              <div style={{ textAlign: 'right', display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '12px' }}>
                <div>
                  <span style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--electric-blue)' }}>₹{Number(booking.totalPrice).toLocaleString('en-IN')}</span>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Payment Complete</div>
                </div>

                {booking.status === 'CONFIRMED' && (
                  <button onClick={() => handleCancelBooking(booking.id)} className="btn btn-danger btn-sm">
                    Cancel Stay
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default CustomerDashboard;
