package com.content.content_stream.global.kafka;

import com.content.content_stream.notification.Notification;
import com.content.content_stream.notification.NotificationRepository;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiredConsumer {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "subscription.expired", groupId = "content-stream-group")
    @Transactional
    public void consume(String message) {
        Long userId = Long.parseLong(message);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("구독 만료 이벤트 처리 실패 - 사용자 없음: {}", userId);
            return;
        }

        notificationRepository.save(Notification.builder()
                .user(user)
                .message("구독이 만료되었습니다. 계속 이용하시려면 구독을 갱신해주세요.")
                .build());

        log.info("구독 만료 알림 저장 - userId: {}", userId);
    }
}
