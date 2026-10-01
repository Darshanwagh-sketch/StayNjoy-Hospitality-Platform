import React, { useState } from 'react';
import { bookingAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { X, CreditCard, CheckCircle2 } from 'lucide-react';

const BookingModal = ({ room, hotel, onClose }) => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const getLocalDateString = (offsetDays = 0) => {
    const d = new Date(Date.now() + offsetDays * 86400000);
    const tzOffset = d.getTimezoneOffset() * 60000;
    return new Date(d.getTime() - tzOffset).toISOString().split('T')[0];
  };

  const todayStr = getLocalDateString(0);
  const defaultCheckOutStr = getLocalDateString(3);

  const [checkInDate, setCheckInDate] = useState(todayStr);
  const [checkOutDate, setCheckOutDate] = useState(defaultCheckOutStr);
  const [guestsCount, setGuestsCount] = useState(2);
  const [guestName, setGuestName] = useState(user ? user.fullName : '');
  const [guestEmail, setGuestEmail] = useState(user ? user.email : '');
  const [guestPhone, setGuestPhone] = useState(user ? user.phone || '' : '');
  const [specialRequests, setSpecialRequests] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const calculateDays = () => {
    const start = new Date(checkInDate);
    const end = new Date(checkOutDate);
    const diffTime = end - start;
    const days = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return days > 0 ? days : 1;
  };

  const nights = calculateDays();
  const totalPrice = (room.pricePerNight * nights).toFixed(2);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!user) {
      navigate('/login');
      return;
    }

    setLoading(true);
    setError('');

    try {
      await bookingAPI.create({
        roomId: room.id,
        checkInDate,
        checkOutDate,
        guestsCount: parseInt(guestsCount),
        guestName,
        guestEmail,
        guestPhone,
        specialRequests,
        paymentMethod: 'UPI / CREDIT_CARD',
      });
      setSuccess(true);
      setTimeout(() => {
        onClose();
        navigate('/dashboard/customer');
      }, 1800);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to complete booking. Please verify date selections.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <button 
          onClick={onClose} 
          style={{ position: 'absolute', top: '20px', right: '20px', background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer' }}
        >
          <X className="w-5 h-5" />
        </button>

        {success ? (
          <div style={{ textAlign: 'center', padding: '32px 0' }}>
            <CheckCircle2 className="w-16 h-16 text-emerald-500" style={{ margin: '0 auto 16px' }} />
            <h2 style={{ fontSize: '1.6rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '8px' }}>Reservation Confirmed!</h2>
            <p style={{ color: 'var(--text-muted)' }}>Your stay at {hotel.name} has been successfully reserved.</p>
          </div>
        ) : (
          <>
            <h2 style={{ fontSize: '1.4rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '4px' }}>Reserve {room.roomType} Room</h2>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '24px' }}>{hotel.name} • Room #{room.roomNumber}</p>

            {error && (
              <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #FCA5A5', color: 'var(--error)', padding: '12px', borderRadius: '10px', fontSize: '0.9rem', marginBottom: '16px' }}>
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                <div className="form-group">
                  <label className="form-label">Check-in Date</label>
                  <input 
                    type="date" 
                    className="form-control" 
                    min={todayStr}
                    value={checkInDate} 
                    onChange={(e) => setCheckInDate(e.target.value)} 
                    required 
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Check-out Date</label>
                  <input 
                    type="date" 
                    className="form-control" 
                    min={checkInDate || todayStr}
                    value={checkOutDate} 
                    onChange={(e) => setCheckOutDate(e.target.value)} 
                    required 
                  />
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Guests Count</label>
                <input 
                  type="number" 
                  min="1" 
                  max={room.capacity || 4} 
                  className="form-control" 
                  value={guestsCount} 
                  onChange={(e) => setGuestsCount(e.target.value)} 
                  required 
                />
              </div>

              <div className="form-group">
                <label className="form-label">Guest Full Name</label>
                <input 
                  type="text" 
                  className="form-control" 
                  value={guestName} 
                  onChange={(e) => setGuestName(e.target.value)} 
                  required 
                />
              </div>

              <div className="form-group">
                <label className="form-label">Guest Email</label>
                <input 
                  type="email" 
                  className="form-control" 
                  value={guestEmail} 
                  onChange={(e) => setGuestEmail(e.target.value)} 
                  required 
                />
              </div>

              <div style={{ background: 'var(--bg-muted)', border: '1px solid var(--border-color)', padding: '16px', borderRadius: '12px', marginBottom: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', fontSize: '0.9rem', color: 'var(--text-muted)' }}>
                  <span>₹{Number(room.pricePerNight).toLocaleString('en-IN')} × {nights} nights</span>
                  <span>₹{Number(totalPrice).toLocaleString('en-IN')}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 800, fontSize: '1.2rem', color: 'var(--navy-primary)', paddingTop: '8px', borderTop: '1px solid var(--border-color)' }}>
                  <span>Total Payable</span>
                  <span style={{ color: 'var(--electric-blue)' }}>₹{Number(totalPrice).toLocaleString('en-IN')}</span>
                </div>
              </div>

              <button type="submit" className="btn btn-primary" style={{ width: '100%' }} disabled={loading}>
                <CreditCard className="w-5 h-5 mr-2" />
                {loading ? 'Processing Reservation...' : 'Confirm & Pay Now'}
              </button>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default BookingModal;
