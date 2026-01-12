package com.template.application.sample;

import com.template.domain.common.exception.BusinessException;
import com.template.domain.sample.Sample;
import com.template.domain.sample.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 샘플 유스케이스 구현체
 * <p>
 * 비즈니스 유스케이스를 구현합니다.
 * 도메인 객체와 도메인 리포지토리만 사용하며, 프레젠테이션 DTO를 알지 못합니다.
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>도메인 레이어의 객체와 인터페이스만 의존</li>
 * <li>프레젠테이션 DTO(Request/Response)에 의존하지 않음</li>
 * <li>트랜잭션 경계를 정의</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleService implements SampleUseCase {

    private final SampleRepository sampleRepository;

    @Override
    @Transactional
    public Sample create(String title, String content) {
        Sample sample = Sample.create(title, content);
        return sampleRepository.save(sample);
    }

    @Override
    @Transactional
    public Sample update(Long id, String title, String content) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND"));

        Sample updated = sample.update(title, content);
        return sampleRepository.save(updated);
    }

    @Override
    public Sample getById(Long id) {
        return sampleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND"));
    }

    @Override
    public List<Sample> getAll() {
        return sampleRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (sampleRepository.findById(id).isEmpty()) {
            throw new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND");
        }
        sampleRepository.deleteById(id);
    }
}
