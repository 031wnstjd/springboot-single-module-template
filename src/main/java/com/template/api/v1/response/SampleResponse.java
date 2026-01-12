package com.template.api.v1.response;

import com.template.domain.sample.Sample;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 샘플 응답 DTO (프레젠테이션 레이어)
 * <p>
 * API 응답 데이터를 담는 DTO입니다.
 * 도메인 객체를 API에 적합한 형태로 변환합니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>응답 DTO는 프레젠테이션 레이어에 위치</li>
 * <li>도메인 객체(Sample)로부터 생성</li>
 * <li>API 스펙 변경 시 도메인에 영향 없음</li>
 * </ul>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 도메인 객체로부터 응답 DTO 생성
     *
     * @param domain 도메인 객체
     */
    public SampleResponse(Sample domain) {
        this.id = domain.getId();
        this.title = domain.getTitle();
        this.content = domain.getContent();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    /**
     * 팩토리 메서드
     */
    public static SampleResponse from(Sample domain) {
        return new SampleResponse(domain);
    }
}
