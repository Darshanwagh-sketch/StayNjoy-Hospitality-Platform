package com.hospitality.platform.dto;

import com.hospitality.platform.entity.Booking;
import com.hospitality.platform.entity.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private String bookingCode;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long hotelId;
    private String hotelName;
    private String hotelCity;
    private String hotelAddress;
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestsCount;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String specialRequests;
    private LocalDateTime createdAt;

    public BookingDTO() {}

    public BookingDTO(Booking booking) {
        if (booking != null) {
            this.id = booking.getId();
            this.bookingCode = booking.getBookingCode();
            if (booking.getUser() != null) {
                this.userId = booking.getUser().getId();
                this.userName = booking.getUser().getFullName();
                this.userEmail = booking.getUser().getEmail();
            }
            if (booking.getRoom() != null) {
                this.roomId = booking.getRoom().getId();
                this.roomNumber = booking.getRoom().getRoomNumber();
                this.roomType = booking.getRoom().getRoomType().name();
                if (booking.getRoom().getHotel() != null) {
                    this.hotelId = booking.getRoom().getHotel().getId();
                    this.hotelName = booking.getRoom().getHotel().getName();
                    this.hotelCity = booking.getRoom().getHotel().getCity();
                    this.hotelAddress = booking.getRoom().getHotel().getAddress();
                }
            }
            this.checkInDate = booking.getCheckInDate();
            this.checkOutDate = booking.getCheckOutDate();
            this.guestsCount = booking.getGuestsCount();
            this.totalPrice = booking.getTotalPrice();
            this.status = booking.getStatus();
            this.guestName = booking.getGuestName();
            this.guestEmail = booking.getGuestEmail();
            this.guestPhone = booking.getGuestPhone();
            this.specialRequests = booking.getSpecialRequests();
            this.createdAt = booking.getCreatedAt();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getHotelCity() { return hotelCity; }
    public void setHotelCity(String hotelCity) { this.hotelCity = hotelCity; }

    public String getHotelAddress() { return hotelAddress; }
    public void setHotelAddress(String hotelAddress) { this.hotelAddress = hotelAddress; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public Integer getGuestsCount() { return guestsCount; }
    public void setGuestsCount(Integer guestsCount) { this.guestsCount = guestsCount; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getGuestEmail() { return guestEmail; }
    public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }

    public String getGuestPhone() { return guestPhone; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
