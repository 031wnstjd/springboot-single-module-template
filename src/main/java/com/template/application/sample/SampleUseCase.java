package com.template.application.sample;

import com.template.domain.sample.Sample;

import java.util.List;

/**
 * 샘플 유스케이스 인터페이스
 * <p>
 * 애플리케이션 레이어의 인바운드 포트입니다.
 * 프레젠테이션 레이어는 이 인터페이스를 통해 비즈니스 로직에 접근합니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>입출력은 도메인 객체(Sample)를 사용</li>
 * <li>DTO 변환은 프레젠테이션 레이어에서 수행</li>
 * <li>인터페이스를 통해 구현체와 분리</li>
 * </ul>
 */
public interface SampleUseCase {

    /**
     * 샘플 생성
     *
     * @param title   제목
     * @param content 내용
     * @return 생성된 샘플
     */
    Sample create(String title, String content);

    /**
     * 샘플 수정
     *
     * @param id      샘플 ID
     * @param title   새 제목
     * @param content 새 내용
     * @return 수정된 샘플
     */
    Sample update(Long id, String title, String content);

    /**
     * 샘플 상세 조회
     *
     * @param id 샘플 ID
     * @return 조회된 샘플
     */
    Sample getById(Long id);

    /**
     * 전체 샘플 조회
     *
     * @return 모든 샘플 목록
     */
    List<Sample> getAll();

    /**
     * 샘플 삭제
     *
     * @param id 샘플 ID
     */
    void delete(Long id);
}
