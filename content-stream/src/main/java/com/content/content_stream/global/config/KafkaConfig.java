package com.content.content_stream.global.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic watchHistoryTopic() {
        return TopicBuilder.name("watch.history")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic subscriptionExpiredTopic() {
        return TopicBuilder.name("subscription.expired")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
