package com.content.content_stream.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionEventProducer {

    private static final String TOPIC_SUBSCRIPTION_EXPIRED = "subscription.expired";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendExpiredEvent(Long userId) {
        kafkaTemplate.send(TOPIC_SUBSCRIPTION_EXPIRED, String.valueOf(userId));
        log.info("구독 만료 이벤트 발행 - userId: {}", userId);
    }
}
