package com.hospitality.platform.service;

import com.hospitality.platform.dto.AdminStatsDTO;
import com.hospitality.platform.dto.CustomerStatsDTO;
import com.hospitality.platform.dto.HotelAdminStatsDTO;
import com.hospitality.platform.entity.Booking;
import com.hospitality.platform.entity.BookingStatus;
import com.hospitality.platform.entity.User;
import com.hospitality.platform.exception.ResourceNotFoundException;
import com.hospitality.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public AdminStatsDTO getAdminStats() {
        long totalUsers = userRepository.count();
        long totalHotels = hotelRepository.count();
        long totalRooms = roomRepository.count();
        long totalBookings = bookingRepository.count();
        long activeHotels = hotelRepository.findByActiveTrue().size();
        BigDecimal totalRevenue = paymentRepository.calculateTotalCompletedRevenue();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        return new AdminStatsDTO(totalUsers, totalHotels, totalRooms, totalBookings, activeHotels, totalRevenue);
    }

    @Transactional(readOnly = true)
    public HotelAdminStatsDTO getHotelAdminStats(Long adminId) {
        long totalRooms = roomRepository.countByHotelAdminId(adminId);
        long totalBookings = bookingRepository.countByHotelAdminId(adminId);
        BigDecimal revenue = paymentRepository.calculateRevenueByHotelAdminId(adminId);
        if (revenue == null) revenue = BigDecimal.ZERO;

        long availableRooms = roomRepository.countByHotelAdminIdAndAvailableTrue(adminId);
        long occupiedRooms = totalRooms - availableRooms;
        if (occupiedRooms < 0) occupiedRooms = 0;

        return new HotelAdminStatsDTO(totalRooms, availableRooms, occupiedRooms, totalBookings, revenue);
    }

    @Transactional(readOnly = true)
    public CustomerStatsDTO getCustomerStats(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        long total = bookings.size();

        LocalDate today = LocalDate.now();
        long upcoming = bookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED && b.getCheckInDate().isAfter(today.minusDays(1))).count();
        long completed = bookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED || (b.getStatus() == BookingStatus.CONFIRMED && b.getCheckOutDate().isBefore(today))).count();
        long cancelled = bookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count();

        return new CustomerStatsDTO(total, upcoming, completed, cancelled);
    }
}
