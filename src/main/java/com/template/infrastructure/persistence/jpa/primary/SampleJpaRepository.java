package com.template.infrastructure.persistence.jpa.primary;

import com.template.infrastructure.persistence.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 샘플 JPA 리포지토리 (Spring Data JPA)
 * <p>
 * Primary 데이터소스(Oracle)에 연결된 JPA 리포지토리입니다.
 * SampleEntity(JPA 엔티티)를 직접 다루며, 도메인 리포지토리 구현체에서 사용됩니다.
 */
public interface SampleJpaRepository extends JpaRepository<SampleEntity, Long> {
}
