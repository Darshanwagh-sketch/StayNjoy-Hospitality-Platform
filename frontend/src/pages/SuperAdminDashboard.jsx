import React, { useState, useEffect } from 'react';
import { hotelAPI, bookingAPI, dashboardAPI } from '../services/api';
import { Plus } from 'lucide-react';

const SuperAdminDashboard = () => {
  const [stats, setStats] = useState(null);
  const [hotels, setHotels] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  // New Hotel Form State
  const [showAddHotel, setShowAddHotel] = useState(false);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [address, setAddress] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [country, setCountry] = useState('India');
  const [imageUrl, setImageUrl] = useState('');

  const fetchData = async () => {
    try {
      const [statsRes, hotelsRes, bookingsRes] = await Promise.all([
        dashboardAPI.getAdminStats(),
        hotelAPI.getAll(),
        bookingAPI.getAll(),
      ]);
      setStats(statsRes.data);
      setHotels(hotelsRes.data);
      setBookings(bookingsRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleCreateHotel = async (e) => {
    e.preventDefault();
    try {
      await hotelAPI.create({
        name,
        description,
        address,
        city,
        state,
        country,
        imageUrl: imageUrl || "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80",
      });
      setShowAddHotel(false);
      setName('');
      setDescription('');
      setAddress('');
      setCity('');
      fetchData();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to create hotel property');
    }
  };

  const handleDeactivateHotel = async (id) => {
    if (!window.confirm('Are you sure you want to deactivate this property?')) return;
    try {
      await hotelAPI.delete(id);
      fetchData();
    } catch (err) {
      alert('Failed to deactivate hotel');
    }
  };

  if (loading) {
    return <div className="container" style={{ padding: '80px 0', textAlign: 'center', color: 'var(--text-muted)' }}>Loading Platform Super Admin Suite...</div>;
  }

  return (
    <div className="container" style={{ paddingTop: '40px' }}>
      <div style={{ marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2.2rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '4px' }}>Platform Super Admin Overview</h1>
        <p style={{ color: 'var(--text-muted)' }}>System-wide operations, property onboarding & platform revenues</p>
      </div>

      {/* Global Platform Metrics */}
      {stats && (
        <div className="grid-stats">
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Total Registered Users</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginTop: '6px' }}>{stats.totalUsers}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Active Hotel Properties</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--electric-blue)', marginTop: '6px' }}>{stats.activeHotels} / {stats.totalHotels}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Total System Bookings</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--success)', marginTop: '6px' }}>{stats.totalBookings}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Platform System Revenue</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginTop: '6px' }}>₹{Number(stats.totalSystemRevenue).toLocaleString('en-IN')}</div>
          </div>
        </div>
      )}

      {/* Property Onboarding Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--navy-primary)' }}>Global Hotel Properties</h2>
        <button onClick={() => setShowAddHotel(!showAddHotel)} className="btn btn-primary btn-sm">
          <Plus className="w-4 h-4 mr-1" /> Onboard New Hotel
        </button>
      </div>

      {/* Add Hotel Collapsible Form */}
      {showAddHotel && (
        <div className="saas-card" style={{ padding: '28px', marginBottom: '32px', border: '1px solid var(--electric-blue)' }}>
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '20px' }}>Onboard New Resort / Hotel</h3>
          <form onSubmit={handleCreateHotel}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px' }}>
              <div className="form-group">
                <label className="form-label">Hotel Name</label>
                <input type="text" className="form-control" placeholder="The Grand Palace" value={name} onChange={(e) => setName(e.target.value)} required />
              </div>
              <div className="form-group">
                <label className="form-label">City</label>
                <input type="text" className="form-control" placeholder="Udaipur" value={city} onChange={(e) => setCity(e.target.value)} required />
              </div>
              <div className="form-group">
                <label className="form-label">State / Region</label>
                <input type="text" className="form-control" placeholder="Rajasthan" value={state} onChange={(e) => setState(e.target.value)} required />
              </div>
              <div className="form-group">
                <label className="form-label">Address</label>
                <input type="text" className="form-control" placeholder="Lake Pichola" value={address} onChange={(e) => setAddress(e.target.value)} required />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Description</label>
              <input type="text" className="form-control" placeholder="Heritage lake palace resort with royal butler service..." value={description} onChange={(e) => setDescription(e.target.value)} required />
            </div>

            <div style={{ display: 'flex', gap: '12px' }}>
              <button type="submit" className="btn btn-primary btn-sm">Create Property</button>
              <button type="button" onClick={() => setShowAddHotel(false)} className="btn btn-secondary btn-sm">Cancel</button>
            </div>
          </form>
        </div>
      )}

      {/* Hotels Table */}
      <div className="saas-card" style={{ padding: '24px', overflowX: 'auto', marginBottom: '40px' }}>
        <table className="custom-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Property Name</th>
              <th>Location</th>
              <th>Manager</th>
              <th>Rating</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {hotels.map((h) => (
              <tr key={h.id}>
                <td style={{ fontWeight: 700, color: 'var(--electric-blue)' }}>#{h.id}</td>
                <td style={{ fontWeight: 700, color: 'var(--navy-primary)' }}>{h.name}</td>
                <td>{h.city}, {h.state}</td>
                <td>{h.hotelAdminName || 'Platform Admin'}</td>
                <td style={{ color: '#D97706', fontWeight: 700 }}>★ {h.rating || '4.8'}</td>
                <td>
                  <span className={`badge ${h.active ? 'badge-success' : 'badge-danger'}`}>
                    {h.active ? 'ACTIVE' : 'INACTIVE'}
                  </span>
                </td>
                <td>
                  <button onClick={() => handleDeactivateHotel(h.id)} className="btn btn-danger btn-sm">
                    Deactivate
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* System Bookings Table */}
      <h2 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '20px' }}>Global System Bookings ({bookings.length})</h2>
      <div className="saas-card" style={{ padding: '24px', overflowX: 'auto' }}>
        <table className="custom-table">
          <thead>
            <tr>
              <th>Booking Code</th>
              <th>Property</th>
              <th>Guest</th>
              <th>Check In / Out</th>
              <th>Amount</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((b) => (
              <tr key={b.id}>
                <td style={{ fontWeight: 700, color: 'var(--electric-blue)' }}>{b.bookingCode}</td>
                <td>{b.hotelName} (Room #{b.roomNumber})</td>
                <td>{b.guestName} <br /><span style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{b.guestEmail}</span></td>
                <td style={{ fontSize: '0.85rem' }}>{b.checkInDate} ➔ {b.checkOutDate}</td>
                <td style={{ fontWeight: 700, color: 'var(--success)' }}>₹{Number(b.totalPrice).toLocaleString('en-IN')}</td>
                <td>
                  <span className={`badge ${b.status === 'CONFIRMED' ? 'badge-success' : b.status === 'CANCELLED' ? 'badge-danger' : 'badge-warning'}`}>
                    {b.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default SuperAdminDashboard;
