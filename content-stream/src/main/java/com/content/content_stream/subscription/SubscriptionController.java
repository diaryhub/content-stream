package com.content.content_stream.subscription;

import com.content.content_stream.global.common.ApiResponse;
import com.content.content_stream.subscription.dto.SubscribeRequest;
import com.content.content_stream.subscription.dto.SubscriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SubscriptionResponse> subscribe(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody SubscribeRequest request) {
        return ApiResponse.ok("구독이 시작되었습니다.", subscriptionService.subscribe(email, request));
    }

    @GetMapping("/me")
    public ApiResponse<SubscriptionResponse> getMySubscription(@AuthenticationPrincipal String email) {
        return ApiResponse.ok(subscriptionService.getMySubscription(email));
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> cancel(@AuthenticationPrincipal String email) {
        subscriptionService.cancel(email);
        return ApiResponse.ok("구독이 취소되었습니다.");
    }
}
