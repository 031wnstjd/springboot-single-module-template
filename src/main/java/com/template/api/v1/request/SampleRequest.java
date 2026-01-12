package com.template.api.v1.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 샘플 생성/수정 요청 DTO (프레젠테이션 레이어)
 * <p>
 * API 요청 데이터를 담는 DTO입니다.
 * 프레젠테이션 레이어에 위치하여 컨트롤러에서 직접 사용됩니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>요청/응답 DTO는 프레젠테이션 레이어에 위치</li>
 * <li>도메인 레이어는 이 DTO를 알지 못함</li>
 * <li>애플리케이션 서비스에서 도메인 객체로 변환하여 처리</li>
 * </ul>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String content;

    public SampleRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
