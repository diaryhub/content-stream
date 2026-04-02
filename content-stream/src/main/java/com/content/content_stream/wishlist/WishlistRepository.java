package com.content.content_stream.wishlist;

import com.content.content_stream.content.Content;
import com.content.content_stream.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserAndContent(User user, Content content);

    List<Wishlist> findByUserOrderByCreatedAtDesc(User user);

    boolean existsByUserAndContent(User user, Content content);
}
