package com.template.api.v1.controller.sample;

import com.template.application.sample.SampleUseCase;
import com.template.domain.sample.Sample;
import com.template.api.support.response.ApiResponse;
import com.template.api.v1.request.SampleRequest;
import com.template.api.v1.response.SampleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 샘플 REST API 컨트롤러
 * <p>
 * 프레젠테이션 레이어에서 HTTP 요청을 처리합니다.
 * DTO ↔ 도메인 객체 변환을 담당합니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>컨트롤러는 요청 DTO를 도메인 객체로 변환</li>
 * <li>UseCase는 도메인 객체만 다룸</li>
 * <li>응답 DTO로 변환하여 반환</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleUseCase sampleUseCase;

    /**
     * 샘플 생성
     */
    @PostMapping
    public ApiResponse<SampleResponse> create(@RequestBody @Valid SampleRequest request) {
        Sample sample = sampleUseCase.create(request.getTitle(), request.getContent());
        return ApiResponse.success(SampleResponse.from(sample));
    }

    /**
     * 샘플 수정
     */
    @PutMapping("/{id}")
    public ApiResponse<SampleResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid SampleRequest request) {
        Sample sample = sampleUseCase.update(id, request.getTitle(), request.getContent());
        return ApiResponse.success(SampleResponse.from(sample));
    }

    /**
     * 샘플 상세 조회
     */
    @GetMapping("/{id}")
    public ApiResponse<SampleResponse> getById(@PathVariable Long id) {
        Sample sample = sampleUseCase.getById(id);
        return ApiResponse.success(SampleResponse.from(sample));
    }

    /**
     * 전체 샘플 조회
     */
    @GetMapping
    public ApiResponse<List<SampleResponse>> getAll() {
        List<SampleResponse> responses = sampleUseCase.getAll().stream()
                .map(SampleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }

    /**
     * 샘플 삭제
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sampleUseCase.delete(id);
        return ApiResponse.success();
    }
}
