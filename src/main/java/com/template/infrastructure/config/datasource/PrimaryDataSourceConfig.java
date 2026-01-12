package com.template.infrastructure.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Primary 데이터소스 설정 (Oracle)
 * <p>
 * 이 클래스는 Oracle 데이터베이스에 대한 주(Primary) 데이터소스를 구성합니다.
 * 
 * @Primary 어노테이션을 통해 기본 데이터소스로 지정되어,
 *          별도의 Qualifier 지정 없이 주입 시 이 데이터소스가 사용됩니다.
 *          <p>
 *          주요 구성 요소:
 *          - DataSourceProperties: application.yml에서 연결 정보 바인딩
 *          - HikariDataSource: 고성능 커넥션 풀
 *          - EntityManagerFactory: JPA 엔티티 관리
 *          - TransactionManager: 트랜잭션 관리
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        // Primary 데이터소스를 사용할 JPA 리포지토리가 위치한 패키지
        basePackages = "com.template.infrastructure.persistence.jpa.primary",
        // 이 데이터소스 전용 EntityManagerFactory 빈 이름
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        // 이 데이터소스 전용 TransactionManager 빈 이름
        transactionManagerRef = "primaryTransactionManager")
public class PrimaryDataSourceConfig {

    /**
     * Primary 데이터소스 속성 설정
     * <p>
     * application.yml의 spring.datasource.primary 하위 속성을 바인딩합니다.
     * - url: JDBC 연결 URL
     * - username: 데이터베이스 사용자명
     * - password: 데이터베이스 비밀번호
     * - driver-class-name: JDBC 드라이버 클래스
     */
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Primary HikariCP 데이터소스 생성
     * <p>
     * HikariCP는 고성능의 JDBC 커넥션 풀 라이브러리입니다.
     * Spring Boot 2.x부터 기본 커넥션 풀로 채택되었습니다.
     * <p>
     * 
     * @Primary 어노테이션으로 기본 DataSource로 지정됩니다.
     */
    @Primary
    @Bean
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * Primary EntityManagerFactory 생성
     * <p>
     * JPA의 핵심 구성 요소로, 엔티티의 생명주기를 관리합니다.
     * Oracle 데이터베이스에 특화된 Hibernate 방언(Dialect)을 설정합니다.
     */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = new HashMap<>();
        // Oracle 데이터베이스용 Hibernate 방언 설정
        properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");

        return builder
                .dataSource(primaryDataSource())
                // Primary 데이터소스가 관리할 엔티티 패키지 경로
                .packages("com.template.infrastructure.persistence.entity")
                // 영속성 유닛 이름 (JPA 내부 식별자)
                .persistenceUnit("primary")
                .properties(properties)
                .build();
    }

    /**
     * Primary 트랜잭션 매니저 생성
     * <p>
     * JPA 트랜잭션을 관리합니다.
     * 
     * @Transactional 어노테이션 사용 시 이 트랜잭션 매니저가 적용됩니다.
     */
    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
