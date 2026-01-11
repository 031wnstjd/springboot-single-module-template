package com.template.application.sample.dto;

import com.template.domain.sample.entity.Sample;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 샘플 정보 응답 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SampleResponse(Sample sample) {
        this.id = sample.getId();
        this.title = sample.getTitle();
        this.content = sample.getContent();
        this.createdAt = sample.getCreatedAt();
        this.updatedAt = sample.getUpdatedAt();
    }
}
