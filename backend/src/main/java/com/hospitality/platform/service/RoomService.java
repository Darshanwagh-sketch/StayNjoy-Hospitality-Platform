package com.hospitality.platform.service;

import com.hospitality.platform.dto.CreateRoomRequest;
import com.hospitality.platform.dto.RoomDTO;
import com.hospitality.platform.entity.Hotel;
import com.hospitality.platform.entity.Room;
import com.hospitality.platform.exception.ResourceNotFoundException;
import com.hospitality.platform.repository.HotelRepository;
import com.hospitality.platform.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Transactional(readOnly = true)
    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream()
                .map(RoomDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> getAvailableRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelIdAndAvailableTrue(hotelId).stream()
                .map(RoomDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        return new RoomDTO(room);
    }

    @Transactional
    public RoomDTO createRoom(Long hotelId, CreateRoomRequest request) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
        room.setImageUrl(request.getImageUrl());
        room.setAvailable(true);

        Room saved = roomRepository.save(room);
        return new RoomDTO(saved);
    }

    @Transactional
    public RoomDTO updateRoom(Long id, CreateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
        if (request.getImageUrl() != null) room.setImageUrl(request.getImageUrl());

        Room saved = roomRepository.save(room);
        return new RoomDTO(saved);
    }

    @Transactional
    public void toggleRoomAvailability(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        room.setAvailable(!room.isAvailable());
        roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room", "id", id);
        }
        roomRepository.deleteById(id);
    }
}
