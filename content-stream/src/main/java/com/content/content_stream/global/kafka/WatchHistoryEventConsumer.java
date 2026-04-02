package com.content.content_stream.global.kafka;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.ContentRepository;
import com.content.content_stream.content.Episode;
import com.content.content_stream.content.EpisodeRepository;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRepository;
import com.content.content_stream.watchhistory.WatchHistory;
import com.content.content_stream.watchhistory.WatchHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WatchHistoryEventConsumer {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final EpisodeRepository episodeRepository;
    private final WatchHistoryRepository watchHistoryRepository;

    @KafkaListener(topics = "watch.history", groupId = "content-stream-group")
    @Transactional
    public void consume(String message) {
        try {
            WatchHistoryEvent event = objectMapper.readValue(message, WatchHistoryEvent.class);

            User user = userRepository.findByEmail(event.userEmail()).orElse(null);
            if (user == null) {
                log.warn("시청 이벤트 처리 실패 - 사용자 없음: {}", event.userEmail());
                return;
            }

            Content content = contentRepository.findById(event.contentId()).orElse(null);
            if (content == null) {
                log.warn("시청 이벤트 처리 실패 - 콘텐츠 없음: {}", event.contentId());
                return;
            }

            if (event.episodeId() != null) {
                // 시리즈 에피소드
                Episode episode = episodeRepository.findById(event.episodeId()).orElse(null);
                if (episode == null) return;

                watchHistoryRepository.findByUserAndEpisode(user, episode)
                        .ifPresentOrElse(
                                wh -> wh.updateProgress(event.watchedSeconds()),
                                () -> watchHistoryRepository.save(WatchHistory.builder()
                                        .user(user).content(content).episode(episode)
                                        .watchedSeconds(event.watchedSeconds()).build())
                        );
            } else {
                // 영화
                watchHistoryRepository.findByUserAndContentAndEpisodeIsNull(user, content)
                        .ifPresentOrElse(
                                wh -> wh.updateProgress(event.watchedSeconds()),
                                () -> watchHistoryRepository.save(WatchHistory.builder()
                                        .user(user).content(content).episode(null)
                                        .watchedSeconds(event.watchedSeconds()).build())
                        );
            }
        } catch (JsonProcessingException e) {
            log.error("시청 이벤트 역직렬화 실패: {}", e.getMessage());
        }
    }
}
