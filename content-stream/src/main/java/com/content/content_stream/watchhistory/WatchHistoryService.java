package com.content.content_stream.watchhistory;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.ContentRepository;
import com.content.content_stream.global.exception.CustomException;
import com.content.content_stream.global.exception.ErrorCode;
import com.content.content_stream.global.kafka.WatchHistoryEvent;
import com.content.content_stream.global.kafka.WatchHistoryEventProducer;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRepository;
import com.content.content_stream.watchhistory.dto.WatchRequest;
import com.content.content_stream.watchhistory.dto.WatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final WatchHistoryEventProducer producer;

    public void record(String email, WatchRequest request) {
        producer.send(new WatchHistoryEvent(
                email,
                request.contentId(),
                request.episodeId(),
                request.watchedSeconds()
        ));
    }

    public List<WatchResponse> getMyHistory(String email) {
        User user = findUser(email);
        return watchHistoryRepository.findByUserOrderByUpdatedAtDesc(user)
                .stream().map(WatchResponse::from).toList();
    }

    public List<WatchResponse> getContentHistory(String email, Long contentId) {
        User user = findUser(email);
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        return watchHistoryRepository.findByUserAndContent(user, content)
                .stream().map(WatchResponse::from).toList();
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
