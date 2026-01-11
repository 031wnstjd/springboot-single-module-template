package com.template.infrastructure.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.template.domain.sample.entity.QSample;
import com.template.domain.sample.entity.Sample;
import com.template.domain.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 도메인 리포지토리 인터페이스의 JPA 구현체
 * <p>
 * QueryDSL을 사용하여 복잡한 쿼리를 처리합니다.
 */
@Repository
@RequiredArgsConstructor
public class SampleRepositoryImpl implements SampleRepository {

    private final SampleJpaRepository sampleJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Sample save(Sample sample) {
        return sampleJpaRepository.save(sample);
    }

    @Override
    public Optional<Sample> findById(Long id) {
        return sampleJpaRepository.findById(id);
    }

    @Override
    public List<Sample> findAll() {
        return sampleJpaRepository.findAll();
    }

    @Override
    public void delete(Sample sample) {
        sampleJpaRepository.delete(sample);
    }

    @Override
    public List<Sample> findByTitleContaining(String title) {
        QSample sample = QSample.sample;

        return queryFactory
                .selectFrom(sample)
                .where(sample.title.containsIgnoreCase(title))
                .fetch();
    }
}
