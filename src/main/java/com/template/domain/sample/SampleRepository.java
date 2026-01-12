package com.template.domain.sample;

import java.util.List;
import java.util.Optional;

/**
 * 샘플 리포지토리 인터페이스 (포트)
 * <p>
 * 이 인터페이스는 도메인 레이어에 정의되어 있으며,
 * 인프라스트럭처 레이어에서 구현됩니다 (의존성 역전 원칙).
 * <p>
 * <b>클린 아키텍처에서의 역할:</b>
 * <ul>
 * <li>도메인 레이어는 이 인터페이스에만 의존합니다</li>
 * <li>JPA, MyBatis 등의 구현 기술은 인프라 레이어에 숨겨집니다</li>
 * <li>테스트 시 Mock 구현체로 쉽게 대체할 수 있습니다</li>
 * </ul>
 * <p>
 * <b>입출력 타입:</b>
 * 순수 도메인 객체(Sample)만 사용하며, JPA Entity나 DTO를 사용하지 않습니다.
 */
public interface SampleRepository {

    /**
     * 샘플 저장
     *
     * @param sample 저장할 도메인 객체
     * @return 저장된 도메인 객체 (ID 할당됨)
     */
    Sample save(Sample sample);

    /**
     * ID로 샘플 조회
     *
     * @param id 샘플 ID
     * @return 조회된 샘플 (Optional)
     */
    Optional<Sample> findById(Long id);

    /**
     * 전체 샘플 조회
     *
     * @return 모든 샘플 목록
     */
    List<Sample> findAll();

    /**
     * 샘플 삭제
     *
     * @param id 삭제할 샘플 ID
     */
    void deleteById(Long id);

    /**
     * 제목으로 샘플 검색
     *
     * @param title 검색할 제목 키워드
     * @return 제목에 키워드를 포함하는 샘플 목록
     */
    List<Sample> findByTitleContaining(String title);
}
