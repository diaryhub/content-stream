package com.content.content_stream.wishlist;

import com.content.content_stream.global.common.ApiResponse;
import com.content.content_stream.wishlist.dto.WishlistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{contentId}")
    public ApiResponse<Void> toggle(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long contentId) {
        boolean added = wishlistService.toggle(userDetails.getUsername(), contentId);
        return ApiResponse.ok(added ? "찜 추가됨" : "찜 취소됨");
    }

    @GetMapping
    public ApiResponse<List<WishlistResponse>> getMyWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(wishlistService.getMyWishlist(userDetails.getUsername()));
    }
}
