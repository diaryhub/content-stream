package com.content.content_stream.wishlist;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.ContentRepository;
import com.content.content_stream.global.exception.CustomException;
import com.content.content_stream.global.exception.ErrorCode;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRepository;
import com.content.content_stream.wishlist.dto.WishlistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public boolean toggle(String email, Long contentId) {
        User user = findUser(email);
        Content content = findContent(contentId);

        Optional<Wishlist> existing = wishlistRepository.findByUserAndContent(user, content);
        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return false; // 찜 취소
        }
        wishlistRepository.save(Wishlist.builder().user(user).content(content).build());
        return true; // 찜 추가
    }

    public List<WishlistResponse> getMyWishlist(String email) {
        User user = findUser(email);
        return wishlistRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(WishlistResponse::from).toList();
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
