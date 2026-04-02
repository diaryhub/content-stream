package com.content.content_stream.global.kafka;

public record WatchHistoryEvent(
        String userEmail,
        Long contentId,
        Long episodeId,       // 영화는 null
        int watchedSeconds
) {
}
