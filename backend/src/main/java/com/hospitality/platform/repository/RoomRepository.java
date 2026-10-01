package com.hospitality.platform.repository;

import com.hospitality.platform.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelIdAndAvailableTrue(Long hotelId);
    List<Room> findByHotelId(Long hotelId);

    @Query("SELECT MIN(r.pricePerNight) FROM Room r WHERE r.hotel.id = :hotelId AND r.available = true")
    BigDecimal findMinPriceByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.available = true AND r.capacity >= :guests AND r.id NOT IN (" +
           "SELECT b.room.id FROM Booking b WHERE b.room.hotel.id = :hotelId AND b.status <> 'CANCELLED' AND " +
           "((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate))" +
           ")")
    List<Room> findAvailableRooms(
        @Param("hotelId") Long hotelId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate,
        @Param("guests") Integer guests
    );

    @Query("SELECT COUNT(r) FROM Room r WHERE r.hotel.id = :hotelId")
    long countByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.hotel.hotelAdmin.id = :adminId")
    long countByHotelAdminId(@Param("adminId") Long adminId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.hotel.hotelAdmin.id = :adminId AND r.available = true")
    long countByHotelAdminIdAndAvailableTrue(@Param("adminId") Long adminId);
}
