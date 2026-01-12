package com.template.domain.analytics;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 분석 데이터 도메인 모델 (GPDB용)
 * <p>
 * Greenplum Database(GPDB)에 저장되는 분석 데이터를 표현하는 도메인 객체입니다.
 * GPDB1, GPDB2 모두에서 사용할 수 있는 공통 도메인 모델입니다.
 */
@Getter
@Builder
public class AnalyticsData {

    private final Long id;
    private final String eventType;
    private final String eventData;
    private final LocalDateTime occurredAt;
    private final LocalDateTime createdAt;

    public static AnalyticsData create(String eventType, String eventData) {
        return AnalyticsData.builder()
                .eventType(eventType)
                .eventData(eventData)
                .occurredAt(LocalDateTime.now())
                .build();
    }
}
