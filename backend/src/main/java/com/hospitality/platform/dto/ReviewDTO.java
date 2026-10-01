package com.hospitality.platform.dto;

import com.hospitality.platform.entity.Review;
import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private Long hotelId;
    private String hotelName;
    private Long userId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewDTO() {}

    public ReviewDTO(Review review) {
        if (review != null) {
            this.id = review.getId();
            if (review.getHotel() != null) {
                this.hotelId = review.getHotel().getId();
                this.hotelName = review.getHotel().getName();
            }
            if (review.getUser() != null) {
                this.userId = review.getUser().getId();
                this.userName = review.getUser().getFullName();
            }
            this.rating = review.getRating();
            this.comment = review.getComment();
            this.createdAt = review.getCreatedAt();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
