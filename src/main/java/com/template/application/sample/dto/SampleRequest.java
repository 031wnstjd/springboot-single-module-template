package com.template.application.sample.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 샘플 생성/수정 요청 DTO
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
