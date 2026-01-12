package com.template.domain.common.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 처리 중 발생하는 예외의 기본 클래스
 * <p>
 * 이 클래스는 도메인 레이어에서 발생하는 모든 비즈니스 예외의 부모 클래스입니다.
 * 애플리케이션의 비즈니스 규칙 위반 시 이 예외(또는 하위 예외)를 발생시킵니다.
 * <p>
 * <b>설계 원칙:</b>
 * <ul>
 * <li>도메인 레이어에 위치하여 비즈니스 로직과 밀접하게 연관됩니다</li>
 * <li>에러 코드를 포함하여 클라이언트가 예외 유형을 식별할 수 있습니다</li>
 * <li>RuntimeException을 상속하여 체크 예외 선언 없이 사용할 수 있습니다</li>
 * </ul>
 * <p>
 * <b>사용 예시:</b>
 * 
 * <pre>
 * {@code
 * // 단순 사용
 * throw new BusinessException("사용자를 찾을 수 없습니다.", "USER_NOT_FOUND");
 * 
 * // 커스텀 예외 정의
 * public class UserNotFoundException extends BusinessException {
 *     public UserNotFoundException(Long userId) {
 *         super("ID가 " + userId + "인 사용자를 찾을 수 없습니다.", "USER_NOT_FOUND");
 *     }
 * }
 * }
 * </pre>
 * <p>
 * <b>예외 처리:</b>
 * GlobalExceptionHandler에서 이 예외를 캐치하여 일관된 API 응답 형식으로 변환합니다.
 *
 * @see com.template.api.support.exception.GlobalExceptionHandler
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 에러 코드
     * <p>
     * 클라이언트가 예외 유형을 식별하고 적절한 처리를 할 수 있도록
     * 고유한 에러 코드를 제공합니다.
     * <p>
     * 예시: USER_NOT_FOUND, INVALID_INPUT, DUPLICATE_EMAIL
     */
    private final String errorCode;

    /**
     * 비즈니스 예외 생성자
     *
     * @param message   사용자에게 표시할 에러 메시지
     * @param errorCode 예외 유형을 식별하는 고유 코드
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 원인 예외를 포함한 비즈니스 예외 생성자
     * <p>
     * 다른 예외를 래핑하여 비즈니스 예외로 변환할 때 사용합니다.
     *
     * @param message   사용자에게 표시할 에러 메시지
     * @param errorCode 예외 유형을 식별하는 고유 코드
     * @param cause     원인이 되는 예외
     */
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
