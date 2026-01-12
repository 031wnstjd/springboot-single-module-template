package com.template.infrastructure.persistence.jpa.gpdb2;

import com.template.infrastructure.persistence.entity.AnalyticsDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 분석 데이터 JPA 리포지토리 (GPDB2)
 */
public interface AnalyticsDataGpdb2JpaRepository extends JpaRepository<AnalyticsDataEntity, Long> {
    List<AnalyticsDataEntity> findByEventType(String eventType);
}
