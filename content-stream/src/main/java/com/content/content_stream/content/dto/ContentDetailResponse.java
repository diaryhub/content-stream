package com.content.content_stream.content.dto;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.ContentType;
import com.content.content_stream.content.Genre;
import com.content.content_stream.subscription.PlanType;

import java.util.List;

public record ContentDetailResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        ContentType contentType,
        Genre genre,
        int releaseYear,
        PlanType minimumPlanType,
        List<EpisodeResponse> episodes
) {
    public static ContentDetailResponse from(Content content) {
        return new ContentDetailResponse(
                content.getId(),
                content.getTitle(),
                content.getDescription(),
                content.getThumbnailUrl(),
                content.getContentType(),
                content.getGenre(),
                content.getReleaseYear(),
                content.getMinimumPlanType(),
                content.getEpisodes().stream().map(EpisodeResponse::from).toList()
        );
    }
}
