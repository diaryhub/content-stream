package com.content.content_stream.content.dto;

import com.content.content_stream.content.Episode;

public record EpisodeResponse(
        Long id,
        int episodeNumber,
        String title,
        int durationMinutes
) {
    public static EpisodeResponse from(Episode episode) {
        return new EpisodeResponse(
                episode.getId(),
                episode.getEpisodeNumber(),
                episode.getTitle(),
                episode.getDurationMinutes()
        );
    }
}
