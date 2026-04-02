package com.content.content_stream.subscription;

import com.content.content_stream.global.common.ApiResponse;
import com.content.content_stream.subscription.dto.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ApiResponse<List<PlanResponse>> getPlans() {
        return ApiResponse.ok(subscriptionService.getPlans());
    }
}
