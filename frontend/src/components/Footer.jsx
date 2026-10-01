import React from 'react';
import { Hotel, Shield, Globe } from 'lucide-react';

const Footer = () => {
  return (
    <footer className="footer-navy">
      <div className="container">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '40px', marginBottom: '40px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: '1.35rem', fontWeight: 800, color: '#FFFFFF', marginBottom: '16px' }}>
              <Hotel style={{ color: 'var(--cyan-accent)' }} />
              <span>StayNjoy Resorts</span>
            </div>
            <p style={{ color: '#94A3B8', fontSize: '0.9rem', lineHeight: '1.6' }}>
              India's premier luxury hospitality platform providing curated heritage palaces, beach sanctuaries, and 24/7 AI concierge experience.
            </p>
          </div>

          <div>
            <h4 style={{ color: '#FFFFFF', marginBottom: '16px', fontSize: '1rem', fontWeight: 700 }}>Top Destinations</h4>
            <ul style={{ listStyle: 'none', color: '#94A3B8', fontSize: '0.9rem', display: 'flex', flexDirection: 'column', gap: '10px' }}>
              <li>Udaipur, Rajasthan</li>
              <li>Goa Beach Resorts</li>
              <li>Jaipur, Rajasthan</li>
              <li>Mumbai, Maharashtra</li>
              <li>Kumarakom, Kerala</li>
            </ul>
          </div>

          <div>
            <h4 style={{ color: '#FFFFFF', marginBottom: '16px', fontSize: '1rem', fontWeight: 700 }}>Platform Solutions</h4>
            <ul style={{ listStyle: 'none', color: '#94A3B8', fontSize: '0.9rem', display: 'flex', flexDirection: 'column', gap: '10px' }}>
              <li>Instant Palace & Villa Booking</li>
              <li>AI Travel Concierge Assistant</li>
              <li>Verified Guest Reviews</li>
              <li>Hotel Admin Property Suite</li>
            </ul>
          </div>

          <div>
            <h4 style={{ color: '#FFFFFF', marginBottom: '16px', fontSize: '1rem', fontWeight: 700 }}>Trust & Security</h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', color: '#94A3B8', fontSize: '0.9rem' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Shield className="w-4 h-4 text-emerald-400" /> Encrypted Payments
              </span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Globe className="w-4 h-4" style={{ color: 'var(--cyan-accent)' }} /> 24/7 Concierge Support
              </span>
            </div>
          </div>
        </div>

        <div style={{ borderTop: '1px solid rgba(255, 255, 255, 0.1)', paddingTop: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px', color: '#64748B', fontSize: '0.85rem' }}>
          <p>© {new Date().getFullYear()} StayNjoy Resorts. All rights reserved.</p>
          <p style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
            Built for luxury hospitality
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
