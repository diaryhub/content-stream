package com.content.content_stream.watchhistory.dto;

import com.content.content_stream.watchhistory.WatchHistory;

import java.time.LocalDateTime;

public record WatchResponse(
        Long id,
        Long contentId,
        String contentTitle,
        Long episodeId,
        Integer episodeNumber,
        int watchedSeconds,
        LocalDateTime updatedAt
) {
    public static WatchResponse from(WatchHistory wh) {
        return new WatchResponse(
                wh.getId(),
                wh.getContent().getId(),
                wh.getContent().getTitle(),
                wh.getEpisode() != null ? wh.getEpisode().getId() : null,
                wh.getEpisode() != null ? wh.getEpisode().getEpisodeNumber() : null,
                wh.getWatchedSeconds(),
                wh.getUpdatedAt()
        );
    }
}
