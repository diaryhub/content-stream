package com.content.content_stream.watchhistory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WatchRequest(
        @NotNull Long contentId,
        Long episodeId,         // 영화는 null
        @Min(0) int watchedSeconds
) {
}
