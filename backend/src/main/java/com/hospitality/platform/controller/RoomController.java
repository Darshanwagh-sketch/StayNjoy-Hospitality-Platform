package com.hospitality.platform.controller;

import com.hospitality.platform.dto.ApiResponse;
import com.hospitality.platform.dto.CreateRoomRequest;
import com.hospitality.platform.dto.RoomDTO;
import com.hospitality.platform.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<RoomDTO> rooms = roomService.getRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        RoomDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<RoomDTO> createRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody CreateRoomRequest request) {
        RoomDTO created = roomService.createRoom(hotelId, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<RoomDTO> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody CreateRoomRequest request) {
        RoomDTO updated = roomService.updateRoom(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> toggleAvailability(@PathVariable Long id) {
        roomService.toggleRoomAvailability(id);
        return ResponseEntity.ok(new ApiResponse(true, "Room availability toggled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HOTEL_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(new ApiResponse(true, "Room deleted successfully"));
    }
}
