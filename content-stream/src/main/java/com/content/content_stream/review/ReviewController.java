package com.content.content_stream.review;

import com.content.content_stream.global.common.ApiResponse;
import com.content.content_stream.review.dto.ReviewRequest;
import com.content.content_stream.review.dto.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{contentId}")
    public ApiResponse<ReviewResponse> save(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long contentId,
            @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.ok(reviewService.save(userDetails.getUsername(), contentId, request));
    }

    @GetMapping("/{contentId}")
    public ApiResponse<List<ReviewResponse>> getReviews(@PathVariable Long contentId) {
        return ApiResponse.ok(reviewService.getReviews(contentId));
    }
}
