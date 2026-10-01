import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { hotelAPI, roomAPI, reviewAPI } from '../services/api';
import BookingModal from '../components/BookingModal';
import { Star, MapPin, CheckCircle, Calendar, MessageSquare } from 'lucide-react';

const HotelDetail = () => {
  const { id } = useParams();

  const [hotel, setHotel] = useState(null);
  const [rooms, setRooms] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [loading, setLoading] = useState(true);

  // Review submission state
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [reviewSubmitting, setReviewSubmitting] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const hotelRes = await hotelAPI.getById(id);
        setHotel(hotelRes.data);

        try {
          const roomsRes = await roomAPI.getByHotel(id);
          setRooms(roomsRes.data || []);
        } catch (rErr) {
          console.error("Failed to load rooms:", rErr);
        }

        try {
          const reviewsRes = await reviewAPI.getByHotel(id);
          setReviews(reviewsRes.data || []);
        } catch (revErr) {
          console.error("Failed to load reviews:", revErr);
        }
      } catch (err) {
        console.error("Failed to load hotel details:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleAddReview = async (e) => {
    e.preventDefault();
    if (!comment.trim()) return;
    setReviewSubmitting(true);
    try {
      const res = await reviewAPI.create({ hotelId: parseInt(id), rating: parseInt(rating), comment });
      setReviews([res.data, ...reviews]);
      setComment('');
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to submit review');
    } finally {
      setReviewSubmitting(false);
    }
  };

  if (loading) {
    return <div className="container" style={{ padding: '80px 0', textAlign: 'center', color: 'var(--text-muted)' }}>Loading hotel details...</div>;
  }

  if (!hotel) {
    return <div className="container" style={{ padding: '80px 0', textAlign: 'center', color: 'var(--text-muted)' }}>Hotel not found.</div>;
  }

  return (
    <div className="container" style={{ paddingTop: '40px' }}>
      {/* Hotel Banner Header */}
      <div className="saas-card" style={{ overflow: 'hidden', marginBottom: '40px' }}>
        <div style={{ position: 'relative', height: '360px' }}>
          <img 
            src={hotel.imageUrl || "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80"} 
            alt={hotel.name} 
            style={{ width: '100%', height: '100%', objectFit: 'cover' }}
          />
          <div style={{ position: 'absolute', inset: 0, background: 'linear-gradient(to top, rgba(15, 23, 42, 0.9) 0%, transparent 60%)' }} />
          
          <div style={{ position: 'absolute', bottom: '24px', left: '32px', right: '32px', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', flexWrap: 'wrap', gap: '16px' }}>
            <div>
              <div style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'var(--navy-primary)', color: '#FFFFFF', padding: '4px 12px', borderRadius: '20px', fontSize: '0.85rem', fontWeight: 700, marginBottom: '12px', boxShadow: 'var(--shadow-sm)' }}>
                <Star className="w-4 h-4 fill-amber-400 text-amber-400" /> {hotel.rating ? hotel.rating.toFixed(1) : '4.8'} ({hotel.reviewCount} reviews)
              </div>
              <h1 style={{ fontSize: '2.5rem', fontWeight: 800, color: '#FFFFFF', marginBottom: '6px' }}>{hotel.name}</h1>
              <p style={{ color: '#CBD5E1', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <MapPin className="w-4 h-4" style={{ color: 'var(--cyan-accent)' }} /> {hotel.address}, {hotel.city}, {hotel.state} {hotel.zipCode}
              </p>
            </div>
          </div>
        </div>

        <div style={{ padding: '32px' }}>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '12px' }}>About Property</h3>
          <p style={{ color: 'var(--text-muted)', lineHeight: '1.7', marginBottom: '24px' }}>{hotel.description}</p>

          {hotel.amenities && hotel.amenities.length > 0 && (
            <div>
              <h4 style={{ fontSize: '1rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '14px' }}>Included Amenities</h4>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px' }}>
                {hotel.amenities.map((amenity) => (
                  <span key={amenity.id} style={{ display: 'inline-flex', alignItems: 'center', gap: '8px', padding: '8px 14px', background: 'var(--bg-muted)', border: '1px solid var(--border-color)', borderRadius: '10px', fontSize: '0.88rem', color: 'var(--navy-primary)', fontWeight: 600 }}>
                    <CheckCircle className="w-4 h-4 text-emerald-500" /> {amenity.name}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Available Rooms Section */}
      <h2 style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '24px' }}>Available Accommodations</h2>
      <div className="grid-2" style={{ marginBottom: '60px' }}>
        {rooms.length === 0 ? (
          <div className="saas-card" style={{ padding: '32px', textAlign: 'center', color: 'var(--text-muted)' }}>
            No rooms listed for this property currently.
          </div>
        ) : (
          rooms.map((room) => (
            <div key={room.id} className="saas-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column' }}>
              <div style={{ position: 'relative', height: '180px', borderRadius: '12px', overflow: 'hidden', marginBottom: '16px' }}>
                <img src={room.imageUrl || "https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80"} alt={room.roomType} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                <span className="badge badge-blue" style={{ position: 'absolute', top: '12px', right: '12px' }}>
                  {room.roomType}
                </span>
              </div>

              <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
                  <h3 style={{ fontSize: '1.2rem', fontWeight: 700, color: 'var(--navy-primary)' }}>Room #{room.roomNumber}</h3>
                  <div style={{ textAlign: 'right' }}>
                    <span style={{ fontSize: '1.4rem', fontWeight: 800, color: 'var(--electric-blue)' }}>₹{Number(room.pricePerNight).toLocaleString('en-IN')}</span>
                    <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>/night</span>
                  </div>
                </div>

                <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', marginBottom: '16px', flex: 1 }}>{room.description}</p>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginBottom: '16px', fontWeight: 500 }}>Capacity: Up to {room.capacity} guests</p>

                <button 
                  onClick={() => setSelectedRoom(room)} 
                  className="btn btn-primary" 
                  style={{ width: '100%' }}
                  disabled={!room.available}
                >
                  <Calendar className="w-4 h-4 mr-1" />
                  {room.available ? 'Book Room Now' : 'Currently Unavailable'}
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Guest Reviews Section */}
      <h2 style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '24px' }}>Guest Reviews ({reviews.length})</h2>
      <div className="grid-2">
        <div className="saas-card" style={{ padding: '28px' }}>
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700, color: 'var(--navy-primary)', marginBottom: '16px' }}>Leave a Review</h3>
          <form onSubmit={handleAddReview}>
            <div className="form-group">
              <label className="form-label">Rating</label>
              <select className="form-control" value={rating} onChange={(e) => setRating(e.target.value)}>
                <option value="5">5 Stars - Exceptional</option>
                <option value="4">4 Stars - Very Good</option>
                <option value="3">3 Stars - Average</option>
                <option value="2">2 Stars - Poor</option>
                <option value="1">1 Star - Terrible</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Review Comment</label>
              <textarea 
                className="form-control" 
                rows="4" 
                placeholder="Share your experience regarding service, ambiance, hospitality..." 
                value={comment} 
                onChange={(e) => setComment(e.target.value)} 
                required 
              />
            </div>

            <button type="submit" className="btn btn-primary" disabled={reviewSubmitting}>
              <MessageSquare className="w-4 h-4 mr-1" />
              {reviewSubmitting ? 'Submitting...' : 'Post Review'}
            </button>
          </form>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {reviews.length === 0 ? (
            <div className="saas-card" style={{ padding: '28px', color: 'var(--text-muted)' }}>
              No reviews posted yet for this property. Be the first to share your experience!
            </div>
          ) : (
            reviews.map((rev) => (
              <div key={rev.id} className="saas-card" style={{ padding: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                  <span style={{ fontWeight: 700, color: 'var(--navy-primary)' }}>{rev.userName || 'Verified Guest'}</span>
                  <div style={{ display: 'flex', gap: '2px' }}>
                    {[...Array(5)].map((_, i) => (
                      <Star key={i} className={`w-4 h-4 ${i < rev.rating ? 'fill-amber-400 text-amber-400' : 'text-slate-300'}`} />
                    ))}
                  </div>
                </div>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: '1.5' }}>{rev.comment}</p>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Reservation Modal */}
      {selectedRoom && (
        <BookingModal 
          room={selectedRoom} 
          hotel={hotel} 
          onClose={() => setSelectedRoom(null)} 
        />
      )}
    </div>
  );
};

export default HotelDetail;
