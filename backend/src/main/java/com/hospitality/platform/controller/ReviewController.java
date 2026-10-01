package com.hospitality.platform.controller;

import com.hospitality.platform.dto.CreateReviewRequest;
import com.hospitality.platform.dto.ReviewDTO;
import com.hospitality.platform.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReviewDTO>> getHotelReviews(@PathVariable Long hotelId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByHotelId(hotelId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDTO> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        ReviewDTO review = reviewService.createReview(request, authentication.getName());
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
}
