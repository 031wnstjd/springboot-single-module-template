package com.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 애플리케이션 메인 클래스
 * <p>
 * 이 클래스는 애플리케이션의 진입점(Entry Point)입니다.
 * Spring Boot의 자동 설정(Auto Configuration)과 컴포넌트 스캔을 활성화합니다.
 * <p>
 * <b>@SpringBootApplication</b> 어노테이션은 다음 3개의 어노테이션을 결합합니다:
 * <ul>
 * <li>@Configuration: 이 클래스가 Spring 설정 클래스임을 나타냅니다</li>
 * <li>@EnableAutoConfiguration: Spring Boot의 자동 설정을 활성화합니다</li>
 * <li>@ComponentScan: 현재 패키지 및 하위 패키지에서 컴포넌트를 스캔합니다</li>
 * </ul>
 * <p>
 * <b>프로젝트 구조:</b>
 * 
 * <pre>
 * com.template
 * ├── domain          - 도메인 레이어 (엔티티, 리포지토리 인터페이스, 비즈니스 규칙)
 * ├── application     - 애플리케이션 레이어 (유스케이스, 서비스, DTO)
 * ├── infrastructure  - 인프라 레이어 (DB 설정, JPA/MyBatis 구현체, 외부 연동)
 * ├── presentation    - 프레젠테이션 레이어 (REST Controller, 예외 처리)
 * └── support         - 서포트 레이어 (공통 유틸리티, AOP, 어노테이션)
 * </pre>
 * 
 * @author Template Team
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
public class TemplateApplication {

    /**
     * 애플리케이션 진입점
     * <p>
     * SpringApplication.run() 메서드를 호출하여 Spring Boot 애플리케이션을 시작합니다.
     * 이 메서드는 다음과 같은 작업을 수행합니다:
     * <ol>
     * <li>ApplicationContext 생성 및 초기화</li>
     * <li>자동 설정(Auto Configuration) 적용</li>
     * <li>컴포넌트 스캔 수행</li>
     * <li>내장 웹 서버(Tomcat) 시작</li>
     * </ol>
     *
     * @param args 명령줄 인수 (예: --spring.profiles.active=local)
     */
    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
