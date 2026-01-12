package com.template.infrastructure.config.feign;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OpenFeign 클라이언트 설정
 * <p>
 * OpenFeign은 선언적 HTTP 클라이언트로, 인터페이스와 어노테이션만으로
 * REST API 호출을 간편하게 구현할 수 있습니다.
 * <p>
 * <b>주요 설정 항목:</b>
 * <ul>
 * <li>Logger.Level: Feign 요청/응답 로깅 레벨</li>
 * <li>Request.Options: 연결/읽기 타임아웃 설정</li>
 * <li>Retryer: 실패 시 재시도 정책</li>
 * <li>ErrorDecoder: HTTP 에러 응답 처리</li>
 * </ul>
 * <p>
 * <b>@EnableFeignClients 설정:</b>
 * basePackages에 지정된 패키지에서 @FeignClient 인터페이스를 스캔합니다.
 *
 * @see org.springframework.cloud.openfeign.FeignClient
 */
@Configuration
@EnableFeignClients(basePackages = "com.template.infrastructure.external")
public class FeignConfig {

    /**
     * Feign 로깅 레벨 설정
     * <p>
     * 로깅 레벨 옵션:
     * <ul>
     * <li>NONE: 로깅 없음 (기본값)</li>
     * <li>BASIC: 요청 메서드, URL, 응답 상태 코드, 실행 시간</li>
     * <li>HEADERS: BASIC + 요청/응답 헤더</li>
     * <li>FULL: HEADERS + 요청/응답 본문 (개발 환경에서만 권장)</li>
     * </ul>
     * <p>
     * 주의: Logback 설정에서 feign 패키지 로거 레벨도 DEBUG 이상이어야 출력됩니다.
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        // 운영 환경에서는 BASIC 또는 NONE 권장
        return Logger.Level.FULL;
    }

    /**
     * Feign 타임아웃 설정
     * <p>
     * 외부 API 호출 시 적절한 타임아웃을 설정하여
     * 느린 응답으로 인한 서비스 장애를 방지합니다.
     * <p>
     * 설정 값:
     * - connectTimeout: TCP 연결 수립 타임아웃 (5초)
     * - readTimeout: 응답 수신 타임아웃 (10초)
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                5, TimeUnit.SECONDS, // connectTimeout
                10, TimeUnit.SECONDS, // readTimeout
                true // followRedirects
        );
    }

    /**
     * Feign 재시도 정책 설정
     * <p>
     * 일시적인 네트워크 오류나 서버 과부하로 인한 실패 시
     * 자동으로 재시도를 수행합니다.
     * <p>
     * 재시도 설정:
     * - period: 첫 번째 재시도까지 대기 시간 (100ms)
     * - maxPeriod: 최대 대기 시간 (1초)
     * - maxAttempts: 최대 재시도 횟수 (3회)
     * <p>
     * 주의: 멱등성(Idempotent)이 보장되지 않는 POST/PUT/DELETE 요청은
     * 재시도 시 부작용이 발생할 수 있으므로 주의가 필요합니다.
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                100, // period (ms)
                1000, // maxPeriod (ms)
                3 // maxAttempts
        );
    }

    /**
     * Feign 에러 디코더 설정
     * <p>
     * HTTP 에러 응답(4xx, 5xx)을 애플리케이션 예외로 변환합니다.
     * 커스텀 에러 처리가 필요한 경우 이 빈을 교체합니다.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
