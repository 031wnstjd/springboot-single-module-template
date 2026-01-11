package com.template.infrastructure.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정 클래스
 * <p>
 * 
 * @MapperScan: 지정된 패키지 하위의 매퍼 인터페이스를 스캔하여 빈으로 등록
 */
@Configuration
@MapperScan("com.template.infrastructure.persistence.mybatis")
public class MyBatisConfig {
}
