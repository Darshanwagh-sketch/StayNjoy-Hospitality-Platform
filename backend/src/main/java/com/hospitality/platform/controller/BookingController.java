package com.hospitality.platform.controller;

import com.hospitality.platform.dto.BookingDTO;
import com.hospitality.platform.dto.CreateBookingRequest;
import com.hospitality.platform.dto.UpdateBookingStatusRequest;
import com.hospitality.platform.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingDTO> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication) {
        BookingDTO booking = bookingService.createBooking(request, authentication.getName());
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingDTO>> getMyBookings(Authentication authentication) {
        List<BookingDTO> bookings = bookingService.getBookingsByUser(authentication.getName());
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<BookingDTO>> getHotelBookings(@PathVariable Long hotelId) {
        List<BookingDTO> bookings = bookingService.getBookingsByHotel(hotelId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<BookingDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingStatusRequest request) {
        BookingDTO booking = bookingService.updateBookingStatus(id, request);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingDTO> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {
        BookingDTO booking = bookingService.cancelBooking(id, authentication.getName());
        return ResponseEntity.ok(booking);
    }
}
