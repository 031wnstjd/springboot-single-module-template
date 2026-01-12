package com.template.domain.analytics;

import java.util.List;
import java.util.Optional;

/**
 * 분석 데이터 리포지토리 인터페이스 (GPDB용)
 * <p>
 * GPDB1 또는 GPDB2의 분석 데이터에 접근하는 포트입니다.
 * 각 GPDB별로 별도의 구현체가 필요합니다.
 */
public interface AnalyticsDataRepository {

    AnalyticsData save(AnalyticsData data);

    Optional<AnalyticsData> findById(Long id);

    List<AnalyticsData> findByEventType(String eventType);

    List<AnalyticsData> findAll();
}
