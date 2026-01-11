package com.template.domain.sample.repository;

import com.template.domain.sample.entity.Sample;

import java.util.List;
import java.util.Optional;

/**
 * 샘플 도메인을 위한 리포지토리 인터페이스
 * <p>
 * 이 인터페이스는 도메인 레이어에 위치하며, 인프라 레이어에서 구현됩니다 (의존성 역전).
 */
public interface SampleRepository {

    Sample save(Sample sample);

    Optional<Sample> findById(Long id);

    List<Sample> findAll();

    void delete(Sample sample);

    /**
     * 특정 제목을 포함하는 샘플 목록 조회 (QueryDSL 사용 예시를 위함)
     */
    List<Sample> findByTitleContaining(String title);
}
