package com.hospitality.platform.dto;

import com.hospitality.platform.entity.Room;
import com.hospitality.platform.entity.RoomType;
import java.math.BigDecimal;

public class RoomDTO {
    private Long id;
    private Long hotelId;
    private String hotelName;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private String description;
    private String imageUrl;
    private boolean available;

    public RoomDTO() {}

    public RoomDTO(Room room) {
        if (room != null) {
            this.id = room.getId();
            if (room.getHotel() != null) {
                this.hotelId = room.getHotel().getId();
                this.hotelName = room.getHotel().getName();
            }
            this.roomNumber = room.getRoomNumber();
            this.roomType = room.getRoomType();
            this.pricePerNight = room.getPricePerNight();
            this.capacity = room.getCapacity();
            this.description = room.getDescription();
            this.imageUrl = room.getImageUrl();
            this.available = room.isAvailable();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
