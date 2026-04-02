package com.content.content_stream.subscription;

import com.content.content_stream.global.exception.CustomException;
import com.content.content_stream.global.exception.ErrorCode;
import com.content.content_stream.global.kafka.SubscriptionEventProducer;
import com.content.content_stream.subscription.dto.PlanResponse;
import com.content.content_stream.subscription.dto.SubscribeRequest;
import com.content.content_stream.subscription.dto.SubscriptionResponse;
import com.content.content_stream.user.User;
import com.content.content_stream.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionEventProducer subscriptionEventProducer;

    public List<PlanResponse> getPlans() {
        return planRepository.findAll().stream()
                .map(PlanResponse::from)
                .toList();
    }

    @Transactional
    public SubscriptionResponse subscribe(String email, SubscribeRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 이미 활성 구독이 있으면 취소 후 재구독
        subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .ifPresent(existing -> existing.cancel());

        Plan plan = planRepository.findByPlanType(request.planType())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        LocalDateTime expiredAt = request.planType() == PlanType.FREE
                ? LocalDateTime.now().plusYears(100)  // 무료는 사실상 무제한
                : LocalDateTime.now().plusMonths(1);

        Subscription subscription = Subscription.create(user, plan, expiredAt);
        return SubscriptionResponse.from(subscriptionRepository.save(subscription));
    }

    public SubscriptionResponse getMySubscription(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Subscription subscription = subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return SubscriptionResponse.from(subscription);
    }

    @Transactional
    public void cancel(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Subscription subscription = subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        subscription.cancel();
    }

    // 매일 자정 만료된 구독 처리
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireSubscriptions() {
        List<Subscription> expired = subscriptionRepository
                .findAllByStatusAndExpiredAtBefore(SubscriptionStatus.ACTIVE, LocalDateTime.now());

        expired.forEach(subscription -> {
            subscription.expire();
            subscriptionEventProducer.sendExpiredEvent(subscription.getUser().getId());
        });
    }
}
