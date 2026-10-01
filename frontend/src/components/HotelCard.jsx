import React from 'react';
import { Link } from 'react-router-dom';
import { Star, MapPin, ArrowRight } from 'lucide-react';

const HotelCard = ({ hotel }) => {
  return (
    <div className="saas-card hotel-card">
      <div className="hotel-image-wrapper">
        <img 
          src={hotel.imageUrl || "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80"} 
          alt={hotel.name} 
          className="hotel-image" 
        />
        <div className="hotel-rating-tag">
          <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
          <span>{hotel.rating ? hotel.rating.toFixed(1) : '4.8'}</span>
          <span style={{ color: '#94A3B8', fontSize: '0.75rem' }}>({hotel.reviewCount || 0})</span>
        </div>
      </div>

      <div className="hotel-card-body">
        <h3 className="hotel-card-title">{hotel.name}</h3>
        <p className="hotel-card-location">
          <MapPin className="w-4 h-4 text-blue-600 shrink-0" />
          <span>{hotel.city}, {hotel.state || hotel.country}</span>
        </p>
        
        <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', lineClamp: 2, display: '-webkit-box', WebkitBoxOrient: 'vertical', overflow: 'hidden', marginBottom: '18px', lineHeight: '1.5' }}>
          {hotel.description}
        </p>

        <div className="hotel-card-footer">
          <div>
            <div className="price-text">
              ₹{hotel.startingPrice ? Number(hotel.startingPrice).toLocaleString('en-IN') : '18,500'}
            </div>
            <div className="price-unit">per night</div>
          </div>

          <Link to={`/hotels/${hotel.id}`} className="btn btn-primary btn-sm">
            View Rooms <ArrowRight className="w-4 h-4 ml-1" />
          </Link>
        </div>
      </div>
    </div>
  );
};

export default HotelCard;
