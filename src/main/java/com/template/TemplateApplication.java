package com.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 프로젝트 메인 애플리케이션 클래스
 * <p>
 * @SpringBootApplication: 스프링 부트의 자동 설정, 빈 등록 등을 활성화합니다.
 */
@SpringBootApplication
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

}
