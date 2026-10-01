package com.hospitality.platform.repository;

import com.hospitality.platform.entity.Booking;
import com.hospitality.platform.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Booking> findByBookingCode(String bookingCode);
    List<Booking> findByRoomHotelIdOrderByCreatedAtDesc(Long hotelId);

    @Query("SELECT b FROM Booking b WHERE b.room.hotel.hotelAdmin.id = :adminId ORDER BY b.createdAt DESC")
    List<Booking> findByHotelAdminId(@Param("adminId") Long adminId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.hotel.hotelAdmin.id = :adminId")
    long countByHotelAdminId(@Param("adminId") Long adminId);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.status <> com.hospitality.platform.entity.BookingStatus.CANCELLED AND " +
           "((b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate))")
    List<Booking> findConflictingBookings(
        @Param("roomId") Long roomId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate
    );

    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.hotel.id = :hotelId")
    long countByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.room.hotel.id = :hotelId AND b.status <> com.hospitality.platform.entity.BookingStatus.CANCELLED")
    BigDecimal calculateTotalRevenueByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status <> com.hospitality.platform.entity.BookingStatus.CANCELLED")
    BigDecimal calculateTotalSystemRevenue();
}
