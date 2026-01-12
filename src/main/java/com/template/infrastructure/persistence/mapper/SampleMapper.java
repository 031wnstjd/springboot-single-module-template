package com.template.infrastructure.persistence.mapper;

import com.template.domain.sample.Sample;
import com.template.infrastructure.persistence.entity.SampleEntity;
import org.springframework.stereotype.Component;

/**
 * 도메인 객체와 JPA 엔티티 간 매퍼
 * <p>
 * 이 클래스는 도메인 모델(Sample)과 영속성 모델(SampleEntity) 간의 변환을 담당합니다.
 * 이를 통해 도메인 레이어가 JPA에 의존하지 않도록 격리합니다.
 * <p>
 * <b>매퍼의 역할:</b>
 * <ul>
 * <li>도메인 → 엔티티: 저장 시 사용</li>
 * <li>엔티티 → 도메인: 조회 시 사용</li>
 * <li>도메인 레이어의 순수성을 보장</li>
 * </ul>
 * <p>
 * <b>대안:</b>
 * MapStruct 같은 매핑 라이브러리를 사용할 수도 있지만,
 * 간단한 변환의 경우 수동 매핑이 더 명확할 수 있습니다.
 */
@Component
public class SampleMapper {

    /**
     * 도메인 객체 → JPA 엔티티 변환
     * <p>
     * 저장/수정 시 사용됩니다.
     *
     * @param domain 도메인 객체
     * @return JPA 엔티티
     */
    public SampleEntity toEntity(Sample domain) {
        return SampleEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .content(domain.getContent())
                .build();
    }

    /**
     * JPA 엔티티 → 도메인 객체 변환
     * <p>
     * 조회 시 사용됩니다.
     *
     * @param entity JPA 엔티티
     * @return 도메인 객체
     */
    public Sample toDomain(SampleEntity entity) {
        return Sample.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
