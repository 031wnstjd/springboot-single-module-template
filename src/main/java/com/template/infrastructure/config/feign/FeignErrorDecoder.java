package com.template.infrastructure.config.feign;

import com.template.domain.common.exception.BusinessException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 에러 디코더
 * <p>
 * 외부 API 호출 시 발생하는 HTTP 에러 응답을 애플리케이션 예외로 변환합니다.
 * <p>
 * <b>에러 분류:</b>
 * <ul>
 * <li>4xx (클라이언트 에러): 요청 데이터 오류, 인증 실패 등</li>
 * <li>5xx (서버 에러): 외부 서비스 장애</li>
 * </ul>
 * <p>
 * 실무에서는 외부 API별로 세분화된 에러 처리가 필요할 수 있습니다.
 * 이 경우 각 Feign 클라이언트별로 별도의 ErrorDecoder를 지정할 수 있습니다.
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    /**
     * HTTP 에러 응답을 예외로 변환
     *
     * @param methodKey Feign 메서드 식별자 (예: ExternalApiClient#getData())
     * @param response  HTTP 응답 객체
     * @return 변환된 예외
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String reason = response.reason();

        log.error("[Feign Error] 외부 API 호출 실패 - method: {}, status: {}, reason: {}",
                methodKey, status, reason);

        // HTTP 상태 코드별 예외 분기
        return switch (status) {
            // 400 Bad Request: 잘못된 요청
            case 400 -> new BusinessException(
                    "외부 API 요청이 잘못되었습니다: " + reason,
                    "EXTERNAL_API_BAD_REQUEST");

            // 401 Unauthorized: 인증 실패
            case 401 -> new BusinessException(
                    "외부 API 인증에 실패했습니다.",
                    "EXTERNAL_API_UNAUTHORIZED");

            // 403 Forbidden: 권한 없음
            case 403 -> new BusinessException(
                    "외부 API 접근 권한이 없습니다.",
                    "EXTERNAL_API_FORBIDDEN");

            // 404 Not Found: 리소스 없음
            case 404 -> new BusinessException(
                    "외부 API에서 요청한 리소스를 찾을 수 없습니다.",
                    "EXTERNAL_API_NOT_FOUND");

            // 408 Request Timeout: 요청 타임아웃
            case 408 -> new BusinessException(
                    "외부 API 요청 시간이 초과되었습니다.",
                    "EXTERNAL_API_TIMEOUT");

            // 429 Too Many Requests: 요청 제한 초과
            case 429 -> new BusinessException(
                    "외부 API 요청 제한을 초과했습니다. 잠시 후 다시 시도해주세요.",
                    "EXTERNAL_API_RATE_LIMIT");

            // 500, 502, 503, 504: 서버 오류
            case 500, 502, 503, 504 -> new BusinessException(
                    "외부 API 서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                    "EXTERNAL_API_SERVER_ERROR");

            // 기타 에러: 기본 디코더 사용
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
