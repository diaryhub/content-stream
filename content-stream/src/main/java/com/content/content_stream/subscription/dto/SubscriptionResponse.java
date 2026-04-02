package com.content.content_stream.subscription.dto;

import com.content.content_stream.subscription.Subscription;
import com.content.content_stream.subscription.SubscriptionStatus;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        PlanResponse plan,
        SubscriptionStatus status,
        boolean isActive,
        LocalDateTime startedAt,
        LocalDateTime expiredAt
) {
    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                PlanResponse.from(subscription.getPlan()),
                subscription.getStatus(),
                subscription.isActive(),
                subscription.getStartedAt(),
                subscription.getExpiredAt()
        );
    }
}
