package com.content.content_stream.subscription;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PlanType planType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int maxDevices;

    @Builder
    private Plan(PlanType planType, String description, int maxDevices) {
        this.planType = planType;
        this.description = description;
        this.maxDevices = maxDevices;
    }
}
