package com.hospitality.platform.controller;

import com.hospitality.platform.dto.ApiResponse;
import com.hospitality.platform.dto.CreateHotelRequest;
import com.hospitality.platform.dto.HotelDTO;
import com.hospitality.platform.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String search) {
        List<HotelDTO> hotels = hotelService.getAllHotels(city, search);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) {
        HotelDTO hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/my-hotels")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<HotelDTO>> getMyHotels(Authentication authentication) {
        // Find admin user hotels or all if Super Admin
        List<HotelDTO> hotels = hotelService.getAllHotels(null, null);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<HotelDTO> createHotel(
            @Valid @RequestBody CreateHotelRequest request,
            Authentication authentication) {
        HotelDTO created = hotelService.createHotel(request, authentication.getName());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<HotelDTO> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody CreateHotelRequest request) {
        HotelDTO updated = hotelService.updateHotel(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(new ApiResponse(true, "Hotel deactivated successfully"));
    }
}
