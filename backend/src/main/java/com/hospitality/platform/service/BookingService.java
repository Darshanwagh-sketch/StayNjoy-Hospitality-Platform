package com.hospitality.platform.service;

import com.hospitality.platform.dto.BookingDTO;
import com.hospitality.platform.dto.CreateBookingRequest;
import com.hospitality.platform.dto.UpdateBookingStatusRequest;
import com.hospitality.platform.entity.*;
import com.hospitality.platform.exception.BadRequestException;
import com.hospitality.platform.exception.ConflictException;
import com.hospitality.platform.exception.ResourceNotFoundException;
import com.hospitality.platform.repository.BookingRepository;
import com.hospitality.platform.repository.PaymentRepository;
import com.hospitality.platform.repository.RoomRepository;
import com.hospitality.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public BookingDTO createBooking(CreateBookingRequest request, String currentUserEmail) {
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", request.getRoomId()));

        if (!room.isAvailable()) {
            throw new BadRequestException("Room is currently unavailable");
        }

        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                room.getId(), request.getCheckInDate(), request.getCheckOutDate());

        if (!conflictingBookings.isEmpty()) {
            throw new ConflictException("Room is already booked for the selected dates");
        }

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (nights <= 0) nights = 1;

        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setBookingCode("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setGuestsCount(request.getGuestsCount());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setGuestPhone(request.getGuestPhone());
        booking.setSpecialRequests(request.getSpecialRequests());

        Booking savedBooking = bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(savedBooking);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        payment.setAmount(totalPrice);
        payment.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "CREDIT_CARD");
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Dispatch confirmation email after transaction commit
        Long newBookingId = savedBooking.getId();
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        emailService.sendBookingConfirmation(newBookingId);
                    } catch (Exception e) {
                        // Log but don't fail transaction if mail fails
                    }
                }
            });
        } else {
            try {
                emailService.sendBookingConfirmation(newBookingId);
            } catch (Exception e) {
                // Log but don't fail transaction if mail fails
            }
        }

        return new BookingDTO(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByRoomHotelIdOrderByCreatedAtDesc(hotelId).stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByHotelAdmin(Long adminId) {
        return bookingRepository.findByHotelAdminId(adminId).stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        return new BookingDTO(booking);
    }

    @Transactional
    public BookingDTO updateBookingStatus(Long id, UpdateBookingStatusRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        booking.setStatus(request.getStatus());
        Booking saved = bookingRepository.save(booking);
        return new BookingDTO(saved);
    }

    @Transactional
    public BookingDTO cancelBooking(Long id, String email) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!booking.getUser().getId().equals(user.getId()) && user.getRole() == Role.CUSTOMER) {
            throw new BadRequestException("You are not authorized to cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return new BookingDTO(saved);
    }
}
