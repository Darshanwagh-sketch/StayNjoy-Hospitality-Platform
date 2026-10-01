import React, { useState, useEffect } from 'react';
import { hotelAPI } from '../services/api';
import HotelCard from '../components/HotelCard';
import { Search, MapPin, Sparkles, Shield, Compass, Star } from 'lucide-react';

const Home = () => {
  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [cityFilter, setCityFilter] = useState('');

  const fetchHotels = async () => {
    setLoading(true);
    try {
      const res = await hotelAPI.getAll(cityFilter, searchQuery);
      setHotels(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHotels();
  }, [cityFilter]);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchHotels();
  };

  return (
    <div>
      {/* Hero Section */}
      <section className="hero-section">
        <div className="container">
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '8px', padding: '6px 16px', background: 'rgba(37, 99, 235, 0.08)', borderRadius: '30px', border: '1px solid rgba(37, 99, 235, 0.2)', color: 'var(--electric-blue)', fontSize: '0.88rem', fontWeight: 700, marginBottom: '24px' }}>
            <Sparkles className="w-4 h-4 text-blue-600" /> India's Finest Luxury Hospitality
          </div>
          <h1 className="hero-title">
            Discover Regal Palaces &<br />Tropical Beach Sanctuaries
          </h1>
          <p className="hero-subtitle">
            Book hand-picked heritage palace hotels, coastal beach resorts, and skyline luxury suites across India with instant confirmation and 24/7 AI concierge.
          </p>

          {/* Search Box */}
          <form onSubmit={handleSearch} className="search-box">
            <div className="search-input-group">
              <Search className="w-5 h-5 text-blue-600 shrink-0" />
              <input 
                type="text" 
                className="search-input" 
                placeholder="Search Taj, Oberoi, Leela, or palace resort..." 
                value={searchQuery} 
                onChange={(e) => setSearchQuery(e.target.value)} 
              />
            </div>
            <div className="search-input-group">
              <MapPin className="w-5 h-5 text-blue-600 shrink-0" />
              <select 
                className="search-input" 
                style={{ cursor: 'pointer' }}
                value={cityFilter}
                onChange={(e) => setCityFilter(e.target.value)}
              >
                <option value="">All Indian Destinations</option>
                <option value="Udaipur">Udaipur (Rajasthan)</option>
                <option value="Goa">Goa (Beach Resorts)</option>
                <option value="Jaipur">Jaipur (Pink City Palaces)</option>
                <option value="Mumbai">Mumbai (Financial Capital)</option>
                <option value="Bengaluru">Bengaluru (Silicon Valley)</option>
                <option value="Kerala">Kerala (Backwaters)</option>
              </select>
            </div>
            <button type="submit" className="btn btn-primary" style={{ borderRadius: 'var(--radius-full)' }}>
              Explore Stays
            </button>
          </form>
        </div>
      </section>

      {/* Featured Hotels Grid */}
      <section className="container" style={{ margin: '40px auto' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '32px' }}>
          <div>
            <h2 style={{ fontSize: '1.8rem', fontWeight: 800, color: 'var(--navy-primary)', marginBottom: '6px' }}>Premier Indian Residences</h2>
            <p style={{ color: 'var(--text-muted)' }}>Handpicked luxury properties across India</p>
          </div>
          <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem', fontWeight: 600 }}>
            Showing {hotels.length} luxury stays
          </div>
        </div>

        {loading ? (
          <div style={{ textAlign: 'center', padding: '60px', color: 'var(--text-muted)' }}>
            Loading luxury properties...
          </div>
        ) : hotels.length === 0 ? (
          <div className="saas-card" style={{ textAlign: 'center', padding: '60px' }}>
            <Compass className="w-12 h-12 text-blue-600" style={{ margin: '0 auto 16px' }} />
            <h3 style={{ fontSize: '1.2rem', color: 'var(--navy-primary)', marginBottom: '8px' }}>No Hotels Found</h3>
            <p style={{ color: 'var(--text-muted)' }}>Try selecting a different Indian destination or search term.</p>
          </div>
        ) : (
          <div className="grid-3">
            {hotels.map((hotel) => (
              <HotelCard key={hotel.id} hotel={hotel} />
            ))}
          </div>
        )}
      </section>

      {/* Value Propositions */}
      <section className="container" style={{ marginTop: '80px' }}>
        <div className="grid-3">
          <div className="saas-card" style={{ padding: '32px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'rgba(37, 99, 235, 0.1)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '20px' }}>
              <Shield className="w-6 h-6 text-blue-600" />
            </div>
            <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '10px', color: 'var(--navy-primary)' }}>Best Rate Guarantee</h3>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: '1.6' }}>
              Transparent pricing with zero hidden convenience fees for top luxury properties across India.
            </p>
          </div>

          <div className="saas-card" style={{ padding: '32px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'var(--cyan-light)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '20px' }}>
              <Sparkles className="w-6 h-6" style={{ color: 'var(--cyan-accent)' }} />
            </div>
            <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '10px', color: 'var(--navy-primary)' }}>24/7 AI Concierge</h3>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: '1.6' }}>
              Instant recommendations for Indian destinations, heritage itineraries, and room availability.
            </p>
          </div>

          <div className="saas-card" style={{ padding: '32px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'rgba(34, 197, 94, 0.1)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '20px' }}>
              <Star className="w-6 h-6 text-emerald-600" />
            </div>
            <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '10px', color: 'var(--navy-primary)' }}>Authentic Guest Reviews</h3>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: '1.6' }}>
              Genuine star ratings & verified reviews submitted by guests who completed their stay.
            </p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
