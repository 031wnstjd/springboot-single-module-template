package com.template.infrastructure.persistence.jpa.primary;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.template.domain.sample.Sample;
import com.template.domain.sample.SampleRepository;
import com.template.infrastructure.persistence.entity.QSampleEntity;
import com.template.infrastructure.persistence.entity.SampleEntity;
import com.template.infrastructure.persistence.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 도메인 리포지토리 구현체 (Primary 데이터소스 - Oracle)
 * <p>
 * 도메인 레이어의 {@link SampleRepository} 인터페이스를 구현합니다.
 * 도메인 객체(Sample)와 JPA 엔티티(SampleEntity) 간의 변환을 담당합니다.
 * <p>
 * <b>의존성 역전 원칙 (DIP) 적용:</b>
 * 
 * <pre>
 * Domain Layer                  Infrastructure Layer
 * ┌─────────────────┐          ┌─────────────────────────┐
 * │ SampleRepository│ ◀─────── │ SampleRepositoryImpl    │
 * │   (인터페이스)    │          │   (구현체)              │
 * └─────────────────┘          └─────────────────────────┘
 * </pre>
 *
 * @see com.template.domain.sample.SampleRepository
 */
@Repository
@RequiredArgsConstructor
public class SampleRepositoryImpl implements SampleRepository {

    private final SampleJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;
    private final SampleMapper mapper;

    @Override
    public Sample save(Sample domain) {
        SampleEntity entity = mapper.toEntity(domain);
        SampleEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Sample> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Sample> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Sample> findByTitleContaining(String title) {
        QSampleEntity entity = QSampleEntity.sampleEntity;

        return queryFactory
                .selectFrom(entity)
                .where(entity.title.containsIgnoreCase(title))
                .fetch()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
