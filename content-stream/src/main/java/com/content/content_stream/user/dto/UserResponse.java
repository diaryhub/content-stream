package com.content.content_stream.user.dto;

import com.content.content_stream.user.AuthProvider;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        UserRole role,
        AuthProvider provider,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getProvider(),
                user.getCreatedAt()
        );
    }
}
