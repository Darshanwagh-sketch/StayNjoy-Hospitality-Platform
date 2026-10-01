package com.hospitality.platform.dto;

import com.hospitality.platform.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateBookingStatusRequest {

    @NotNull(message = "Status is required")
    private BookingStatus status;

    public UpdateBookingStatusRequest() {}

    public UpdateBookingStatusRequest(BookingStatus status) {
        this.status = status;
    }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
