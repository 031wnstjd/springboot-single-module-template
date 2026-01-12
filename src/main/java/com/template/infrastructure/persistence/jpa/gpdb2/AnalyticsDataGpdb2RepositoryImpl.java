package com.template.infrastructure.persistence.jpa.gpdb2;

import com.template.domain.analytics.AnalyticsData;
import com.template.domain.analytics.AnalyticsDataRepository;
import com.template.infrastructure.persistence.mapper.AnalyticsDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 분석 데이터 리포지토리 구현체 (GPDB2)
 * <p>
 * GPDB2(Greenplum Database #2)에 연결된 분석 데이터 리포지토리입니다.
 * <p>
 * <b>사용 시 주의:</b>
 * 트랜잭션 매니저를 명시적으로 지정해야 합니다.
 * 
 * <pre>
 * {@code
 * &#64;Transactional("gpdb2TransactionManager")
 * public void saveAnalyticsData(...) {
 *     gpdb2Repository.save(data);
 * }
 * }
 * </pre>
 */
@Repository("gpdb2AnalyticsDataRepository")
@RequiredArgsConstructor
public class AnalyticsDataGpdb2RepositoryImpl implements AnalyticsDataRepository {

    private final AnalyticsDataGpdb2JpaRepository jpaRepository;
    private final AnalyticsDataMapper mapper;

    @Override
    public AnalyticsData save(AnalyticsData data) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(data)));
    }

    @Override
    public Optional<AnalyticsData> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AnalyticsData> findByEventType(String eventType) {
        return jpaRepository.findByEventType(eventType).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<AnalyticsData> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
