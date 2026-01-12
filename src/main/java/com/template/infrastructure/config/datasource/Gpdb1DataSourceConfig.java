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
 * GPDB1 데이터소스 설정 (PostgreSQL - Greenplum Database #1)
 * <p>
 * 이 클래스는 첫 번째 Greenplum 데이터베이스(GPDB1)에 대한 데이터소스를 구성합니다.
 * Greenplum은 PostgreSQL 기반의 MPP(Massively Parallel Processing) 데이터베이스입니다.
 * <p>
 * 주요 특징:
 * - PostgreSQL JDBC 드라이버 사용
 * - 별도의 EntityManagerFactory 및 TransactionManager 구성
 * - gpdb1 전용 리포지토리 패키지 스캔
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        // GPDB1 데이터소스를 사용할 JPA 리포지토리가 위치한 패키지
        basePackages = "com.template.infrastructure.persistence.jpa.gpdb1",
        // 이 데이터소스 전용 EntityManagerFactory 빈 이름
        entityManagerFactoryRef = "gpdb1EntityManagerFactory",
        // 이 데이터소스 전용 TransactionManager 빈 이름
        transactionManagerRef = "gpdb1TransactionManager")
public class Gpdb1DataSourceConfig {

    /**
     * GPDB1 데이터소스 속성 설정
     * <p>
     * application.yml의 spring.datasource.gpdb1 하위 속성을 바인딩합니다.
     */
    @Bean
    @ConfigurationProperties("spring.datasource.gpdb1")
    public DataSourceProperties gpdb1DataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * GPDB1 HikariCP 데이터소스 생성
     * <p>
     * Greenplum 데이터베이스 전용 커넥션 풀을 구성합니다.
     * PostgreSQL JDBC 드라이버를 사용하여 연결합니다.
     */
    @Bean
    public DataSource gpdb1DataSource() {
        return gpdb1DataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * GPDB1 EntityManagerFactory 생성
     * <p>
     * PostgreSQL/Greenplum용 Hibernate 방언을 설정합니다.
     * 대용량 분석 쿼리에 최적화된 설정을 적용할 수 있습니다.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean gpdb1EntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("gpdb1DataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        // PostgreSQL(Greenplum) 데이터베이스용 Hibernate 방언 설정
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return builder
                .dataSource(dataSource)
                // GPDB1 데이터소스가 관리할 엔티티 패키지 경로
                // 필요에 따라 별도의 엔티티 패키지를 지정할 수 있습니다
                .packages("com.template.infrastructure.persistence.entity")
                // 영속성 유닛 이름 (JPA 내부 식별자)
                .persistenceUnit("gpdb1")
                .properties(properties)
                .build();
    }

    /**
     * GPDB1 트랜잭션 매니저 생성
     * <p>
     * GPDB1 관련 트랜잭션을 관리합니다.
     * @Transactional("gpdb1TransactionManager") 형태로 명시적으로 지정하여 사용합니다.
     */
    @Bean
    public PlatformTransactionManager gpdb1TransactionManager(
            @Qualifier("gpdb1EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
