package com.template.infrastructure.persistence.jpa;

import com.template.domain.sample.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA 리포지토리 인터페이스
 */
public interface SampleJpaRepository extends JpaRepository<Sample, Long> {
}
