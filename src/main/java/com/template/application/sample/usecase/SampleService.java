package com.template.application.sample.usecase;

import com.template.application.sample.dto.SampleRequest;
import com.template.application.sample.dto.SampleResponse;
import com.template.domain.common.exception.BusinessException;
import com.template.domain.sample.entity.Sample;
import com.template.domain.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 샘플 유스케이스 구현체
 * <p>
 * @Service: 애플리케이션 서비스를 나타내는 어노테이션
 * @Transactional: 트랜잭션 관리를 수행
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleService implements SampleUseCase {

    private final SampleRepository sampleRepository;

    @Override
    @Transactional
    public SampleResponse create(SampleRequest request) {
        Sample sample = Sample.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        
        return new SampleResponse(sampleRepository.save(sample));
    }

    @Override
    @Transactional
    public SampleResponse update(Long id, SampleRequest request) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND"));
        
        sample.update(request.getTitle(), request.getContent());
        return new SampleResponse(sample);
    }

    @Override
    public SampleResponse getById(Long id) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND"));
        
        return new SampleResponse(sample);
    }

    @Override
    public List<SampleResponse> getAll() {
        return sampleRepository.findAll().stream()
                .map(SampleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND"));
        
        sampleRepository.delete(sample);
    }
}
