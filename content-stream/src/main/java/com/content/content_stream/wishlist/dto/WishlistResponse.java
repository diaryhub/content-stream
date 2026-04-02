package com.content.content_stream.wishlist.dto;

import com.content.content_stream.content.ContentType;
import com.content.content_stream.content.Genre;
import com.content.content_stream.wishlist.Wishlist;

import java.time.LocalDateTime;

public record WishlistResponse(
        Long contentId,
        String contentTitle,
        String thumbnailUrl,
        ContentType contentType,
        Genre genre,
        LocalDateTime addedAt
) {
    public static WishlistResponse from(Wishlist wishlist) {
        return new WishlistResponse(
                wishlist.getContent().getId(),
                wishlist.getContent().getTitle(),
                wishlist.getContent().getThumbnailUrl(),
                wishlist.getContent().getContentType(),
                wishlist.getContent().getGenre(),
                wishlist.getCreatedAt()
        );
    }
}
