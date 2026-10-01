package com.hospitality.platform.service;

import com.hospitality.platform.dto.CreateHotelRequest;
import com.hospitality.platform.dto.HotelDTO;
import com.hospitality.platform.entity.Amenity;
import com.hospitality.platform.entity.Hotel;
import com.hospitality.platform.entity.User;
import com.hospitality.platform.exception.ResourceNotFoundException;
import com.hospitality.platform.repository.AmenityRepository;
import com.hospitality.platform.repository.HotelRepository;
import com.hospitality.platform.repository.RoomRepository;
import com.hospitality.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<HotelDTO> getAllHotels(String city, String searchKey) {
        List<Hotel> hotels;
        if (city != null && !city.trim().isEmpty()) {
            hotels = hotelRepository.findByCityContainingIgnoreCaseAndActiveTrue(city.trim());
        } else if (searchKey != null && !searchKey.trim().isEmpty()) {
            hotels = hotelRepository.findActiveHotelsBySearch(searchKey.trim());
        } else {
            hotels = hotelRepository.findByActiveTrue();
        }

        return hotels.stream().map(hotel -> {
            HotelDTO dto = new HotelDTO(hotel);
            BigDecimal startingPrice = roomRepository.findMinPriceByHotelId(hotel.getId());
            dto.setStartingPrice(startingPrice);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));

        HotelDTO dto = new HotelDTO(hotel);
        BigDecimal startingPrice = roomRepository.findMinPriceByHotelId(hotel.getId());
        dto.setStartingPrice(startingPrice);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> getHotelsByAdmin(Long adminId) {
        List<Hotel> hotels = hotelRepository.findByHotelAdminId(adminId);
        return hotels.stream().map(hotel -> {
            HotelDTO dto = new HotelDTO(hotel);
            BigDecimal startingPrice = roomRepository.findMinPriceByHotelId(hotel.getId());
            dto.setStartingPrice(startingPrice);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public HotelDTO createHotel(CreateHotelRequest request, String currentUserEmail) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setState(request.getState());
        hotel.setCountry(request.getCountry());
        hotel.setZipCode(request.getZipCode());
        hotel.setImageUrl(request.getImageUrl());
        hotel.setCheckInTime(request.getCheckInTime() != null ? request.getCheckInTime() : "14:00");
        hotel.setCheckOutTime(request.getCheckOutTime() != null ? request.getCheckOutTime() : "11:00");
        hotel.setPolicies(request.getPolicies());
        hotel.setActive(true);

        if (request.getHotelAdminId() != null) {
            User admin = userRepository.findById(request.getHotelAdminId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getHotelAdminId()));
            hotel.setHotelAdmin(admin);
        } else {
            User admin = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));
            hotel.setHotelAdmin(admin);
        }

        if (request.getAmenityIds() != null && !request.getAmenityIds().isEmpty()) {
            Set<Amenity> amenities = new HashSet<>(amenityRepository.findAllById(request.getAmenityIds()));
            hotel.setAmenities(amenities);
        }

        Hotel saved = hotelRepository.save(hotel);
        return new HotelDTO(saved);
    }

    @Transactional
    public HotelDTO updateHotel(Long id, CreateHotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));

        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setState(request.getState());
        hotel.setCountry(request.getCountry());
        hotel.setZipCode(request.getZipCode());
        if (request.getImageUrl() != null) hotel.setImageUrl(request.getImageUrl());
        if (request.getCheckInTime() != null) hotel.setCheckInTime(request.getCheckInTime());
        if (request.getCheckOutTime() != null) hotel.setCheckOutTime(request.getCheckOutTime());
        if (request.getPolicies() != null) hotel.setPolicies(request.getPolicies());

        if (request.getAmenityIds() != null) {
            Set<Amenity> amenities = new HashSet<>(amenityRepository.findAllById(request.getAmenityIds()));
            hotel.setAmenities(amenities);
        }

        Hotel saved = hotelRepository.save(hotel);
        return new HotelDTO(saved);
    }

    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));
        hotel.setActive(false);
        hotelRepository.save(hotel);
    }
}
