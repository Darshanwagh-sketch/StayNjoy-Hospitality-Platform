import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import AiChatWidget from './components/AiChatWidget';

import Home from './pages/Home';
import HotelDetail from './pages/HotelDetail';
import Login from './pages/Login';
import Register from './pages/Register';
import CustomerDashboard from './pages/CustomerDashboard';
import HotelAdminDashboard from './pages/HotelAdminDashboard';
import SuperAdminDashboard from './pages/SuperAdminDashboard';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user, loading } = useAuth();

  if (loading) return null;
  if (!user) return <Navigate to="/login" replace />;
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" replace />;
  }

  return children;
};

function AppContent() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Navbar />
      <main style={{ flex: 1 }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/hotels/:id" element={<HotelDetail />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          <Route 
            path="/dashboard/customer" 
            element={
              <ProtectedRoute allowedRoles={['CUSTOMER', 'HOTEL_ADMIN', 'SUPER_ADMIN']}>
                <CustomerDashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/dashboard/hotel-admin" 
            element={
              <ProtectedRoute allowedRoles={['HOTEL_ADMIN', 'SUPER_ADMIN']}>
                <HotelAdminDashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/dashboard/admin" 
            element={
              <ProtectedRoute allowedRoles={['SUPER_ADMIN']}>
                <SuperAdminDashboard />
              </ProtectedRoute>
            } 
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <Footer />
      <AiChatWidget />
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;
