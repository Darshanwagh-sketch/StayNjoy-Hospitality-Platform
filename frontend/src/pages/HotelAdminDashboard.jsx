import React, { useState, useEffect } from 'react';
import { hotelAPI, roomAPI, bookingAPI, dashboardAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Plus } from 'lucide-react';

const HotelAdminDashboard = () => {
  const { user } = useAuth();

  const [hotels, setHotels] = useState([]);
  const [selectedHotelId, setSelectedHotelId] = useState(null);
  const [rooms, setRooms] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  // New room form state
  const [showAddRoom, setShowAddRoom] = useState(false);
  const [roomNumber, setRoomNumber] = useState('');
  const [roomType, setRoomType] = useState('DELUXE');
  const [pricePerNight, setPricePerNight] = useState('18500');
  const [capacity, setCapacity] = useState('2');
  const [description, setDescription] = useState('');
  const [imageUrl, setImageUrl] = useState('');

  const fetchData = async () => {
    try {
      const hotelsRes = await hotelAPI.getAll();
      setHotels(hotelsRes.data);
      
      const currentHotelId = hotelsRes.data[0]?.id;
      if (currentHotelId) {
        setSelectedHotelId(currentHotelId);
        const [roomsRes, bookingsRes, statsRes] = await Promise.all([
          roomAPI.getByHotel(currentHotelId),
          bookingAPI.getHotelBookings(currentHotelId),
          dashboardAPI.getHotelAdminStats(user.id),
        ]);
        setRooms(roomsRes.data);
        setBookings(bookingsRes.data);
        setStats(statsRes.data);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleHotelSelect = async (id) => {
    setSelectedHotelId(id);
    setLoading(true);
    try {
      const [roomsRes, bookingsRes] = await Promise.all([
        roomAPI.getByHotel(id),
        bookingAPI.getHotelBookings(id),
      ]);
      setRooms(roomsRes.data);
      setBookings(bookingsRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateRoom = async (e) => {
    e.preventDefault();
    if (!selectedHotelId) return;

    try {
      await roomAPI.create(selectedHotelId, {
        roomNumber,
        roomType,
        pricePerNight: parseFloat(pricePerNight),
        capacity: parseInt(capacity),
        description,
        imageUrl: imageUrl || "https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80",
      });
      setShowAddRoom(false);
      setRoomNumber('');
      setDescription('');
      handleHotelSelect(selectedHotelId);
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to add room');
    }
  };

  const handleToggleRoom = async (id) => {
    try {
      await roomAPI.toggleAvailability(id);
      handleHotelSelect(selectedHotelId);
    } catch (err) {
      alert('Failed to update room status');
    }
  };

  if (loading && hotels.length === 0) {
    return <div className="container" style={{ padding: '80px 0', textAlign: 'center', color: 'var(--text-muted)' }}>Loading Hotel Admin Portal...</div>;
  }

  return (
    <div className="container" style={{ paddingTop: '40px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1 style={{ fontSize: '2.2rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '4px' }}>Hotel Management Suite</h1>
          <p style={{ color: 'var(--text-muted)' }}>Manage room inventory, rates, and guest bookings</p>
        </div>

        {hotels.length > 0 && (
          <select 
            className="form-control" 
            style={{ width: 'auto', minWidth: '240px' }}
            value={selectedHotelId || ''}
            onChange={(e) => handleHotelSelect(Number(e.target.value))}
          >
            {hotels.map((h) => (
              <option key={h.id} value={h.id}>{h.name} ({h.city})</option>
            ))}
          </select>
        )}
      </div>

      {/* Stats Header */}
      {stats && (
        <div className="grid-stats">
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Total Rooms</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginTop: '6px' }}>{stats.totalRooms}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Available Rooms</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--success)', marginTop: '6px' }}>{stats.availableRooms}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Total Guest Bookings</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--electric-blue)', marginTop: '6px' }}>{stats.totalBookings}</div>
          </div>
          <div className="saas-card" style={{ padding: '20px' }}>
            <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem', fontWeight: 600 }}>Total Revenue</span>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginTop: '6px' }}>₹{Number(stats.revenueSummary).toLocaleString('en-IN')}</div>
          </div>
        </div>
      )}

      {/* Room Inventory Management */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--navy-primary)' }}>Room Inventory</h2>
        <button onClick={() => setShowAddRoom(!showAddRoom)} className="btn btn-primary btn-sm">
          <Plus className="w-4 h-4 mr-1" /> Add New Room
        </button>
      </div>

      {/* Add Room Modal / Collapsible Form */}
      {showAddRoom && (
        <div className="saas-card" style={{ padding: '28px', marginBottom: '32px', border: '1px solid var(--electric-blue)' }}>
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '20px' }}>Add Room to Property</h3>
          <form onSubmit={handleCreateRoom}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
              <div className="form-group">
                <label className="form-label">Room Number</label>
                <input type="text" className="form-control" placeholder="104" value={roomNumber} onChange={(e) => setRoomNumber(e.target.value)} required />
              </div>
              <div className="form-group">
                <label className="form-label">Room Type</label>
                <select className="form-control" value={roomType} onChange={(e) => setRoomType(e.target.value)}>
                  <option value="STANDARD">STANDARD</option>
                  <option value="DELUXE">DELUXE</option>
                  <option value="SUITE">SUITE</option>
                  <option value="EXECUTIVE">EXECUTIVE</option>
                  <option value="PRESIDENTIAL">PRESIDENTIAL</option>
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Price / Night (₹)</label>
                <input type="number" className="form-control" value={pricePerNight} onChange={(e) => setPricePerNight(e.target.value)} required />
              </div>
              <div className="form-group">
                <label className="form-label">Capacity (Guests)</label>
                <input type="number" className="form-control" value={capacity} onChange={(e) => setCapacity(e.target.value)} required />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Room Description</label>
              <input type="text" className="form-control" placeholder="King bed, lake view, private balcony..." value={description} onChange={(e) => setDescription(e.target.value)} required />
            </div>

            <div style={{ display: 'flex', gap: '12px' }}>
              <button type="submit" className="btn btn-primary btn-sm">Save Room</button>
              <button type="button" onClick={() => setShowAddRoom(false)} className="btn btn-secondary btn-sm">Cancel</button>
            </div>
          </form>
        </div>
      )}

      {/* Rooms Table */}
      <div className="saas-card" style={{ padding: '24px', overflowX: 'auto', marginBottom: '40px' }}>
        <table className="custom-table">
          <thead>
            <tr>
              <th>Room #</th>
              <th>Type</th>
              <th>Nightly Rate</th>
              <th>Capacity</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {rooms.map((r) => (
              <tr key={r.id}>
                <td style={{ fontWeight: 700, color: 'var(--navy-primary)' }}>#{r.roomNumber}</td>
                <td><span className="badge badge-blue">{r.roomType}</span></td>
                <td style={{ color: 'var(--electric-blue)', fontWeight: 700 }}>₹{Number(r.pricePerNight).toLocaleString('en-IN')}</td>
                <td>{r.capacity} Guests</td>
                <td>
                  <span className={`badge ${r.available ? 'badge-success' : 'badge-danger'}`}>
                    {r.available ? 'AVAILABLE' : 'BLOCKED'}
                  </span>
                </td>
                <td>
                  <button onClick={() => handleToggleRoom(r.id)} className="btn btn-secondary btn-sm">
                    {r.available ? 'Block Room' : 'Enable Room'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Incoming Guest Bookings */}
      <h2 style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '20px' }}>Incoming Guest Reservations ({bookings.length})</h2>
      <div className="saas-card" style={{ padding: '24px', overflowX: 'auto' }}>
        <table className="custom-table">
          <thead>
            <tr>
              <th>Code</th>
              <th>Guest Name</th>
              <th>Room #</th>
              <th>Dates</th>
              <th>Total</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((b) => (
              <tr key={b.id}>
                <td style={{ fontWeight: 700, color: 'var(--electric-blue)' }}>{b.bookingCode}</td>
                <td>{b.guestName} <br /><span style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{b.guestEmail}</span></td>
                <td>#{b.roomNumber} ({b.roomType})</td>
                <td style={{ fontSize: '0.85rem' }}>{b.checkInDate} ➔ {b.checkOutDate}</td>
                <td style={{ fontWeight: 700, color: 'var(--navy-primary)' }}>₹{Number(b.totalPrice).toLocaleString('en-IN')}</td>
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

export default HotelAdminDashboard;
