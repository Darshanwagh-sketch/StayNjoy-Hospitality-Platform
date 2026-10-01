package com.hospitality.platform.service;

import com.hospitality.platform.dto.CreateReviewRequest;
import com.hospitality.platform.dto.ReviewDTO;
import com.hospitality.platform.entity.Hotel;
import com.hospitality.platform.entity.Review;
import com.hospitality.platform.entity.User;
import com.hospitality.platform.exception.ResourceNotFoundException;
import com.hospitality.platform.repository.HotelRepository;
import com.hospitality.platform.repository.ReviewRepository;
import com.hospitality.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByHotelId(Long hotelId) {
        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId).stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request, String userEmail) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", request.getHotelId()));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Review review = new Review();
        review.setHotel(hotel);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);

        // Update hotel average rating & review count
        Double avgRating = reviewRepository.getAverageRatingForHotel(hotel.getId());
        hotel.setRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        hotel.setReviewCount(reviewRepository.countByHotelId(hotel.getId()));
        hotelRepository.save(hotel);

        return new ReviewDTO(savedReview);
    }
}
