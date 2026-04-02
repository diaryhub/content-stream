package com.content.content_stream.review.dto;

import com.content.content_stream.review.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String nickname,
        int rating,
        String comment,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
