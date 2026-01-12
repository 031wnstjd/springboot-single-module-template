package com.template.infrastructure.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * GPDB2 데이터소스 설정 (PostgreSQL - Greenplum Database #2)
 * <p>
 * 이 클래스는 두 번째 Greenplum 데이터베이스(GPDB2)에 대한 데이터소스를 구성합니다.
 * GPDB1과 동일한 구조로, 별도의 Greenplum 클러스터에 연결할 때 사용합니다.
 * <p>
 * 사용 시나리오:
 * - 읽기/쓰기 분리 (Read Replica)
 * - 데이터 마이그레이션
 * - 크로스 클러스터 조인 등의 분석 작업
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        // GPDB2 데이터소스를 사용할 JPA 리포지토리가 위치한 패키지
        basePackages = "com.template.infrastructure.persistence.jpa.gpdb2",
        // 이 데이터소스 전용 EntityManagerFactory 빈 이름
        entityManagerFactoryRef = "gpdb2EntityManagerFactory",
        // 이 데이터소스 전용 TransactionManager 빈 이름
        transactionManagerRef = "gpdb2TransactionManager")
public class Gpdb2DataSourceConfig {

    /**
     * GPDB2 데이터소스 속성 설정
     * <p>
     * application.yml의 spring.datasource.gpdb2 하위 속성을 바인딩합니다.
     */
    @Bean
    @ConfigurationProperties("spring.datasource.gpdb2")
    public DataSourceProperties gpdb2DataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * GPDB2 HikariCP 데이터소스 생성
     * <p>
     * 두 번째 Greenplum 데이터베이스 전용 커넥션 풀을 구성합니다.
     */
    @Bean
    public DataSource gpdb2DataSource() {
        return gpdb2DataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * GPDB2 EntityManagerFactory 생성
     * <p>
     * PostgreSQL/Greenplum용 Hibernate 방언을 설정합니다.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean gpdb2EntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("gpdb2DataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        // PostgreSQL(Greenplum) 데이터베이스용 Hibernate 방언 설정
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return builder
                .dataSource(dataSource)
                // GPDB2 데이터소스가 관리할 엔티티 패키지 경로
                .packages("com.template.infrastructure.persistence.entity")
                // 영속성 유닛 이름 (JPA 내부 식별자)
                .persistenceUnit("gpdb2")
                .properties(properties)
                .build();
    }

    /**
     * GPDB2 트랜잭션 매니저 생성
     * <p>
     * GPDB2 관련 트랜잭션을 관리합니다.
     * @Transactional("gpdb2TransactionManager") 형태로 명시적으로 지정하여 사용합니다.
     */
    @Bean
    public PlatformTransactionManager gpdb2TransactionManager(
            @Qualifier("gpdb2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
