package com.content.content_stream.subscription;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlanType {
    FREE("무료", 0),
    BASIC("베이직", 9900),
    PREMIUM("프리미엄", 14900);

    private final String displayName;
    private final int price;
}
