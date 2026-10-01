import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (data) => api.post('/auth/register', data),
  me: () => api.get('/auth/me'),
};

export const hotelAPI = {
  getAll: (city, search) => api.get('/hotels', { params: { city, search } }),
  getById: (id) => api.get(`/hotels/${id}`),
  create: (data) => api.post('/hotels', data),
  update: (id, data) => api.put(`/hotels/${id}`, data),
  delete: (id) => api.delete(`/hotels/${id}`),
};

export const roomAPI = {
  getByHotel: (hotelId) => api.get(`/rooms/hotel/${hotelId}`),
  create: (hotelId, data) => api.post(`/rooms/hotel/${hotelId}`, data),
  update: (id, data) => api.put(`/rooms/${id}`, data),
  toggleAvailability: (id) => api.patch(`/rooms/${id}/toggle-availability`),
  delete: (id) => api.delete(`/rooms/${id}`),
};

export const bookingAPI = {
  create: (data) => api.post('/bookings', data),
  getMyBookings: () => api.get('/bookings/my'),
  getHotelBookings: (hotelId) => api.get(`/bookings/hotel/${hotelId}`),
  getAll: () => api.get('/bookings/all'),
  updateStatus: (id, status) => api.patch(`/bookings/${id}/status`, { status }),
  cancel: (id) => api.patch(`/bookings/${id}/cancel`),
};

export const reviewAPI = {
  getByHotel: (hotelId) => api.get(`/reviews/hotel/${hotelId}`),
  create: (data) => api.post('/reviews', data),
};

export const dashboardAPI = {
  getAdminStats: () => api.get('/dashboard/admin'),
  getHotelAdminStats: (adminId) => api.get(`/dashboard/hotel-admin/${adminId}`),
  getCustomerStats: () => api.get('/dashboard/customer'),
};

export const chatAPI = {
  sendMessage: (message, conversationId) => api.post('/chat', { message, conversationId }),
};

export default api;
