package com.template.infrastructure.config.datasource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;

/**
 * Multi-DataSource 공통 설정
 * <p>
 * Multi-DB 환경에서 Spring Boot JPA 자동설정이 비활성화되어 있으므로
 * EntityManagerFactoryBuilder를 수동으로 생성합니다.
 */
@Configuration
public class DataSourceCommonConfig {

    /**
     * JPA Vendor Adapter 생성
     * <p>
     * Hibernate를 JPA 구현체로 사용합니다.
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    /**
     * EntityManagerFactoryBuilder 생성
     * <p>
     * Multi-DB 환경에서 각 DataSource 별로 EntityManagerFactory를 생성할 때
     * 이 빌더를 사용합니다.
     */
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter) {
        // 빈 Map을 전달하여 NPE 방지
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, new HashMap<>(), null);
    }
}
