package com.template.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 분석 데이터 JPA 엔티티 (GPDB용)
 * <p>
 * GPDB1, GPDB2에서 사용되는 분석 데이터 엔티티입니다.
 */
@Entity
@Table(name = "analytics_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AnalyticsDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String eventData;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
