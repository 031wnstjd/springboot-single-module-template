package com.template.infrastructure.persistence.mapper;

import com.template.domain.analytics.AnalyticsData;
import com.template.infrastructure.persistence.entity.AnalyticsDataEntity;
import org.springframework.stereotype.Component;

/**
 * 분석 데이터 도메인-엔티티 매퍼
 */
@Component
public class AnalyticsDataMapper {

    public AnalyticsDataEntity toEntity(AnalyticsData domain) {
        return AnalyticsDataEntity.builder()
                .id(domain.getId())
                .eventType(domain.getEventType())
                .eventData(domain.getEventData())
                .occurredAt(domain.getOccurredAt())
                .build();
    }

    public AnalyticsData toDomain(AnalyticsDataEntity entity) {
        return AnalyticsData.builder()
                .id(entity.getId())
                .eventType(entity.getEventType())
                .eventData(entity.getEventData())
                .occurredAt(entity.getOccurredAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
