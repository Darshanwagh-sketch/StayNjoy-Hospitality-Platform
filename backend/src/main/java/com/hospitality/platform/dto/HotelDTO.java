package com.hospitality.platform.dto;

import com.hospitality.platform.entity.Hotel;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class HotelDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private Double rating;
    private Integer reviewCount;
    private String imageUrl;
    private String checkInTime;
    private String checkOutTime;
    private String policies;
    private boolean active;
    private Long hotelAdminId;
    private String hotelAdminName;
    private Set<AmenityDTO> amenities;
    private BigDecimal startingPrice;

    public HotelDTO() {}

    public HotelDTO(Hotel hotel) {
        if (hotel != null) {
            this.id = hotel.getId();
            this.name = hotel.getName();
            this.description = hotel.getDescription();
            this.address = hotel.getAddress();
            this.city = hotel.getCity();
            this.state = hotel.getState();
            this.country = hotel.getCountry();
            this.zipCode = hotel.getZipCode();
            this.rating = hotel.getRating();
            this.reviewCount = hotel.getReviewCount();
            this.imageUrl = hotel.getImageUrl();
            this.checkInTime = hotel.getCheckInTime();
            this.checkOutTime = hotel.getCheckOutTime();
            this.policies = hotel.getPolicies();
            this.active = hotel.isActive();
            if (hotel.getHotelAdmin() != null) {
                this.hotelAdminId = hotel.getHotelAdmin().getId();
                this.hotelAdminName = hotel.getHotelAdmin().getFullName();
            }
            if (hotel.getAmenities() != null) {
                this.amenities = hotel.getAmenities().stream()
                        .map(AmenityDTO::new)
                        .collect(Collectors.toSet());
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getPolicies() { return policies; }
    public void setPolicies(String policies) { this.policies = policies; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getHotelAdminId() { return hotelAdminId; }
    public void setHotelAdminId(Long hotelAdminId) { this.hotelAdminId = hotelAdminId; }

    public String getHotelAdminName() { return hotelAdminName; }
    public void setHotelAdminName(String hotelAdminName) { this.hotelAdminName = hotelAdminName; }

    public Set<AmenityDTO> getAmenities() { return amenities; }
    public void setAmenities(Set<AmenityDTO> amenities) { this.amenities = amenities; }

    public BigDecimal getStartingPrice() { return startingPrice; }
    public void setStartingPrice(BigDecimal startingPrice) { this.startingPrice = startingPrice; }
}
