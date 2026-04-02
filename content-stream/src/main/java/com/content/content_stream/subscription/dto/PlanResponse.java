package com.content.content_stream.subscription.dto;

import com.content.content_stream.subscription.Plan;
import com.content.content_stream.subscription.PlanType;

public record PlanResponse(
        Long id,
        PlanType planType,
        String displayName,
        int price,
        String description,
        int maxDevices
) {
    public static PlanResponse from(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getPlanType(),
                plan.getPlanType().getDisplayName(),
                plan.getPlanType().getPrice(),
                plan.getDescription(),
                plan.getMaxDevices()
        );
    }
}
