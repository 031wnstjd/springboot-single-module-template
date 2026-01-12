package com.template.infrastructure.persistence.jpa.gpdb1;

import com.template.domain.analytics.AnalyticsData;
import com.template.domain.analytics.AnalyticsDataRepository;
import com.template.infrastructure.persistence.mapper.AnalyticsDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 분석 데이터 리포지토리 구현체 (GPDB1)
 * <p>
 * GPDB1(Greenplum Database #1)에 연결된 분석 데이터 리포지토리입니다.
 * <p>
 * <b>사용 시 주의:</b>
 * 트랜잭션 매니저를 명시적으로 지정해야 합니다.
 * 
 * <pre>
 * {@code
 * &#64;Transactional("gpdb1TransactionManager")
 * public void saveAnalyticsData(...) {
 *     gpdb1Repository.save(data);
 * }
 * }
 * </pre>
 */
@Repository("gpdb1AnalyticsDataRepository")
@RequiredArgsConstructor
public class AnalyticsDataGpdb1RepositoryImpl implements AnalyticsDataRepository {

    private final AnalyticsDataGpdb1JpaRepository jpaRepository;
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
