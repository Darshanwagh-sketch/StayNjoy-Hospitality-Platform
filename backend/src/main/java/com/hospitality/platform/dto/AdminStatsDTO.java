package com.hospitality.platform.dto;

import java.math.BigDecimal;

public class AdminStatsDTO {
    private long totalUsers;
    private long totalHotels;
    private long totalRooms;
    private long totalBookings;
    private long activeHotels;
    private BigDecimal totalSystemRevenue;

    public AdminStatsDTO() {}

    public AdminStatsDTO(long totalUsers, long totalHotels, long totalRooms, long totalBookings, long activeHotels, BigDecimal totalSystemRevenue) {
        this.totalUsers = totalUsers;
        this.totalHotels = totalHotels;
        this.totalRooms = totalRooms;
        this.totalBookings = totalBookings;
        this.activeHotels = activeHotels;
        this.totalSystemRevenue = totalSystemRevenue != null ? totalSystemRevenue : BigDecimal.ZERO;
    }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalHotels() { return totalHotels; }
    public void setTotalHotels(long totalHotels) { this.totalHotels = totalHotels; }

    public long getTotalRooms() { return totalRooms; }
    public void setTotalRooms(long totalRooms) { this.totalRooms = totalRooms; }

    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }

    public long getActiveHotels() { return activeHotels; }
    public void setActiveHotels(long activeHotels) { this.activeHotels = activeHotels; }

    public BigDecimal getTotalSystemRevenue() { return totalSystemRevenue; }
    public void setTotalSystemRevenue(BigDecimal totalSystemRevenue) { this.totalSystemRevenue = totalSystemRevenue; }
}
