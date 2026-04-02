package com.content.content_stream.content.dto;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.ContentType;
import com.content.content_stream.content.Genre;
import com.content.content_stream.subscription.PlanType;

public record ContentResponse(
        Long id,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        Genre genre,
        int releaseYear,
        PlanType minimumPlanType
) {
    public static ContentResponse from(Content content) {
        return new ContentResponse(
                content.getId(),
                content.getTitle(),
                content.getThumbnailUrl(),
                content.getContentType(),
                content.getGenre(),
                content.getReleaseYear(),
                content.getMinimumPlanType()
        );
    }
}
