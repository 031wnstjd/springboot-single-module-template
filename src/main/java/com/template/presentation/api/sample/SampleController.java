package com.template.presentation.api.sample;

import com.template.application.sample.dto.SampleRequest;
import com.template.application.sample.dto.SampleResponse;
import com.template.application.sample.usecase.SampleUseCase;
import com.template.presentation.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 샘플 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleUseCase sampleUseCase;

    @PostMapping
    public ApiResponse<SampleResponse> create(@RequestBody @Valid SampleRequest request) {
        return ApiResponse.success(sampleUseCase.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<SampleResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid SampleRequest request) {
        return ApiResponse.success(sampleUseCase.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<SampleResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(sampleUseCase.getById(id));
    }

    @GetMapping
    public ApiResponse<List<SampleResponse>> getAll() {
        return ApiResponse.success(sampleUseCase.getAll());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sampleUseCase.delete(id);
        return ApiResponse.success();
    }
}
