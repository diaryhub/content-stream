package com.content.content_stream.review;

import com.content.content_stream.content.Content;
import com.content.content_stream.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndContent(User user, Content content);

    List<Review> findByContentOrderByCreatedAtDesc(Content content);
}
