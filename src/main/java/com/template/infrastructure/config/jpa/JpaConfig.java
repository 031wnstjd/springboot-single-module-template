package com.template.infrastructure.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 설정 클래스
 * <p>
 * 
 * @EnableJpaAuditing: BaseEntity의 생성/수정 시간을 기록하기 위한 오디팅 기능 활성화
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
