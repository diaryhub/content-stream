package com.content.content_stream.subscription.dto;

import com.content.content_stream.subscription.PlanType;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(

        @NotNull(message = "플랜을 선택해주세요.")
        PlanType planType
) {
}
