package com.hospitality.platform.dto;

public class CustomerStatsDTO {
    private long totalBookings;
    private long upcomingBookings;
    private long completedBookings;
    private long cancelledBookings;

    public CustomerStatsDTO() {}

    public CustomerStatsDTO(long totalBookings, long upcomingBookings, long completedBookings, long cancelledBookings) {
        this.totalBookings = totalBookings;
        this.upcomingBookings = upcomingBookings;
        this.completedBookings = completedBookings;
        this.cancelledBookings = cancelledBookings;
    }

    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }

    public long getUpcomingBookings() { return upcomingBookings; }
    public void setUpcomingBookings(long upcomingBookings) { this.upcomingBookings = upcomingBookings; }

    public long getCompletedBookings() { return completedBookings; }
    public void setCompletedBookings(long completedBookings) { this.completedBookings = completedBookings; }

    public long getCancelledBookings() { return cancelledBookings; }
    public void setCancelledBookings(long cancelledBookings) { this.cancelledBookings = cancelledBookings; }
}
