package com.template.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 샘플 JPA 엔티티 (인프라스트럭처 레이어)
 * <p>
 * 이 클래스는 JPA 영속성을 위한 엔티티로, 인프라스트럭처 레이어에 위치합니다.
 * 도메인 레이어는 이 클래스를 직접 참조하지 않습니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>JPA 어노테이션(@Entity, @Table 등)은 인프라 레이어에만 존재</li>
 * <li>도메인 객체(Sample)와 매퍼를 통해 변환</li>
 * <li>도메인 레이어는 JPA에 대해 전혀 알지 못함</li>
 * </ul>
 */
@Entity
@Table(name = "samples")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 엔티티 정보 업데이트
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
