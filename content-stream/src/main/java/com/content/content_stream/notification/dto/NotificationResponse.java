package com.content.content_stream.notification.dto;

import com.content.content_stream.notification.Notification;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String message,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
