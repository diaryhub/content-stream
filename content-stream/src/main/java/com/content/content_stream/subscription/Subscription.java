package com.content.content_stream.subscription;

import com.content.content_stream.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime startedAt;

    private LocalDateTime expiredAt;

    @Builder
    private Subscription(User user, Plan plan, SubscriptionStatus status, LocalDateTime expiredAt) {
        this.user = user;
        this.plan = plan;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public static Subscription create(User user, Plan plan, LocalDateTime expiredAt) {
        return Subscription.builder()
                .user(user)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .expiredAt(expiredAt)
                .build();
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE
                && LocalDateTime.now().isBefore(this.expiredAt);
    }
}
