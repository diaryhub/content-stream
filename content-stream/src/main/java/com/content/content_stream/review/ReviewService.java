package com.content.content_stream.review;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.ContentRepository;
import com.content.content_stream.global.exception.CustomException;
import com.content.content_stream.global.exception.ErrorCode;
import com.content.content_stream.review.dto.ReviewRequest;
import com.content.content_stream.review.dto.ReviewResponse;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public ReviewResponse save(String email, Long contentId, ReviewRequest request) {
        User user = findUser(email);
        Content content = findContent(contentId);

        Review review = reviewRepository.findByUserAndContent(user, content)
                .orElse(null);

        if (review == null) {
            review = Review.builder()
                    .user(user).content(content)
                    .rating(request.rating()).comment(request.comment())
                    .build();
            reviewRepository.save(review);
        } else {
            review.update(request.rating(), request.comment());
        }
        return ReviewResponse.from(review);
    }

    public List<ReviewResponse> getReviews(Long contentId) {
        Content content = findContent(contentId);
        return reviewRepository.findByContentOrderByCreatedAtDesc(content)
                .stream().map(ReviewResponse::from).toList();
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Content findContent(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }
}
