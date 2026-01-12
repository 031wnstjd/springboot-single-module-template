package com.template.domain.sample;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 샘플 도메인 모델 (POJO)
 * <p>
 * 이 클래스는 순수한 도메인 객체로, 어떠한 인프라스트럭처 의존성도 갖지 않습니다.
 * JPA 어노테이션(@Entity, @Table 등)이 없으며, 오직 비즈니스 로직만 포함합니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>도메인 레이어는 가장 안쪽에 위치하며 외부 의존성이 없습니다</li>
 * <li>JPA Entity는 인프라스트럭처 레이어에서 정의됩니다</li>
 * <li>매퍼(Mapper)를 통해 도메인 객체와 엔티티 간 변환합니다</li>
 * </ul>
 * <p>
 * <b>불변 객체 설계:</b>
 * Setter를 제공하지 않고, 상태 변경이 필요한 경우 새로운 객체를 반환하거나
 * 명시적인 비즈니스 메서드를 통해 변경합니다.
 */
@Getter
@Builder
public class Sample {

    /**
     * 샘플 고유 식별자
     */
    private final Long id;

    /**
     * 샘플 제목 (필수)
     */
    private final String title;

    /**
     * 샘플 내용
     */
    private final String content;

    /**
     * 생성 시간
     */
    private final LocalDateTime createdAt;

    /**
     * 수정 시간
     */
    private final LocalDateTime updatedAt;

    /**
     * 팩토리 메서드: 새로운 샘플 생성
     * <p>
     * 신규 샘플 생성 시 사용합니다. ID와 시간 정보는 리포지토리에서 설정됩니다.
     *
     * @param title   샘플 제목
     * @param content 샘플 내용
     * @return 새로운 Sample 도메인 객체
     */
    public static Sample create(String title, String content) {
        validateTitle(title);
        return Sample.builder()
                .title(title)
                .content(content)
                .build();
    }

    /**
     * 샘플 정보 업데이트
     * <p>
     * 불변 객체이므로 새로운 객체를 반환합니다.
     *
     * @param newTitle   새 제목
     * @param newContent 새 내용
     * @return 업데이트된 Sample 도메인 객체
     */
    public Sample update(String newTitle, String newContent) {
        validateTitle(newTitle);
        return Sample.builder()
                .id(this.id)
                .title(newTitle)
                .content(newContent)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 제목 유효성 검증
     */
    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
    }
}
