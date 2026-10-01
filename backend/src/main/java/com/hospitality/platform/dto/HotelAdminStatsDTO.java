package com.hospitality.platform.dto;

import java.math.BigDecimal;

public class HotelAdminStatsDTO {
    private long totalRooms;
    private long availableRooms;
    private long occupiedRooms;
    private long totalBookings;
    private BigDecimal revenueSummary;

    public HotelAdminStatsDTO() {}

    public HotelAdminStatsDTO(long totalRooms, long availableRooms, long occupiedRooms, long totalBookings, BigDecimal revenueSummary) {
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
        this.occupiedRooms = occupiedRooms;
        this.totalBookings = totalBookings;
        this.revenueSummary = revenueSummary != null ? revenueSummary : BigDecimal.ZERO;
    }

    public long getTotalRooms() { return totalRooms; }
    public void setTotalRooms(long totalRooms) { this.totalRooms = totalRooms; }

    public long getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(long availableRooms) { this.availableRooms = availableRooms; }

    public long getOccupiedRooms() { return occupiedRooms; }
    public void setOccupiedRooms(long occupiedRooms) { this.occupiedRooms = occupiedRooms; }

    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }

    public BigDecimal getRevenueSummary() { return revenueSummary; }
    public void setRevenueSummary(BigDecimal revenueSummary) { this.revenueSummary = revenueSummary; }
}
