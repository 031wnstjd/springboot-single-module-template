package com.template.application.sample.usecase;

import com.template.application.sample.dto.SampleRequest;
import com.template.application.sample.dto.SampleResponse;

import java.util.List;

/**
 * 샘플 관리를 위한 유스케이스 인터페이스 (인바운드 포트)
 */
public interface SampleUseCase {

    SampleResponse create(SampleRequest request);

    SampleResponse update(Long id, SampleRequest request);

    SampleResponse getById(Long id);

    List<SampleResponse> getAll();

    void delete(Long id);
}
