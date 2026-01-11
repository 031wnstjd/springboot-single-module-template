package com.template.domain.common.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 처리 중 발생하는 예외의 기본 클래스
 * <p>
 * 모든 도메인 예외는 이 클래스를 상속받아 구현합니다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
