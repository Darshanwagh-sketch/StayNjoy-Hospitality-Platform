package com.hospitality.platform.controller;

import com.hospitality.platform.dto.AdminStatsDTO;
import com.hospitality.platform.dto.CustomerStatsDTO;
import com.hospitality.platform.dto.HotelAdminStatsDTO;
import com.hospitality.platform.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AdminStatsDTO> getAdminStats() {
        AdminStatsDTO stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/hotel-admin/{adminId}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<HotelAdminStatsDTO> getHotelAdminStats(@PathVariable Long adminId) {
        HotelAdminStatsDTO stats = dashboardService.getHotelAdminStats(adminId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/customer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerStatsDTO> getCustomerStats(Authentication authentication) {
        CustomerStatsDTO stats = dashboardService.getCustomerStats(authentication.getName());
        return ResponseEntity.ok(stats);
    }
}
