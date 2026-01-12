// ===================================================
// Gradle 빌드 설정 파일
// ===================================================
// 이 파일은 프로젝트의 빌드 설정, 의존성, 플러그인을 정의합니다.
// 모든 의존성 버전을 이 파일에서 중앙 관리합니다.
// ===================================================

plugins {
    // Spring Boot 플러그인: 스프링 부트 애플리케이션 빌드 지원
    id("org.springframework.boot") version "3.3.5"
    // Spring 의존성 관리 플러그인: BOM 기반 버전 관리
    id("io.spring.dependency-management") version "1.1.6"
    // Java 플러그인: Java 컴파일 및 빌드 지원
    java
}

// ===================================================
// 프로젝트 기본 정보
// ===================================================
group = "com.template"
version = "0.0.1-SNAPSHOT"

// ===================================================
// 버전 상수 정의 (중앙 관리)
// ===================================================
object Versions {
    // Core
    const val SPRING_CLOUD = "2023.0.3"
    
    // Database
    const val POSTGRESQL = "42.7.4"
    const val ORACLE = "23.5.0.24.07"
    const val H2 = "2.3.232"
    
    // ORM & Query
    const val QUERYDSL = "5.1.0"
    const val MYBATIS = "3.0.3"
    
    // Logging
    const val LOGSTASH_ENCODER = "8.0"
    
    // Utility
    const val LOMBOK = "1.18.34"
}

// ===================================================
// Java 툴체인 설정
// ===================================================
// Java 21 LTS 버전을 사용하도록 설정합니다.
// ===================================================
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// ===================================================
// 설정 확장
// ===================================================
// compileOnly 설정이 annotationProcessor 설정을 상속하도록 합니다.
// 이를 통해 Lombok 등의 어노테이션 프로세서가 올바르게 동작합니다.
// ===================================================
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

// ===================================================
// 저장소 설정
// ===================================================
repositories {
    mavenCentral()
}

// ===================================================
// Spring Cloud BOM (의존성 버전 관리)
// ===================================================
// OpenFeign 등 Spring Cloud 의존성의 버전을 일괄 관리합니다.
// ===================================================
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Versions.SPRING_CLOUD}")
    }
}

// ===================================================
// 의존성 정의
// ===================================================
dependencies {
    // -------------------- Spring Boot Starters --------------------
    // 웹 애플리케이션 개발을 위한 스타터 (내장 Tomcat, Spring MVC 포함)
    implementation("org.springframework.boot:spring-boot-starter-web")
    // JPA 데이터 접근을 위한 스타터 (Hibernate 포함)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Bean Validation을 위한 스타터 (Hibernate Validator 포함)
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // AOP(Aspect-Oriented Programming) 지원 스타터
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // Actuator: 애플리케이션 모니터링 및 관리 (헬스체크, 메트릭스 등)
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // -------------------- Spring Cloud (OpenFeign) --------------------
    // OpenFeign: 선언적 HTTP 클라이언트 (외부 API 호출)
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // -------------------- Database Drivers --------------------
    // Oracle JDBC 드라이버 (Primary 데이터소스용)
    runtimeOnly("com.oracle.database.jdbc:ojdbc11:${Versions.ORACLE}")
    // PostgreSQL JDBC 드라이버 (GPDB1, GPDB2용)
    runtimeOnly("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    // H2 인메모리 데이터베이스 (로컬 개발용)
    runtimeOnly("com.h2database:h2:${Versions.H2}")

    // -------------------- MyBatis --------------------
    // MyBatis Spring Boot 스타터 (SQL 매퍼 프레임워크)
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:${Versions.MYBATIS}")

    // -------------------- QueryDSL --------------------
    // QueryDSL JPA: 타입 세이프 동적 쿼리 지원
    // jakarta 분류자를 사용하여 Spring Boot 3.x와 호환
    implementation("com.querydsl:querydsl-jpa:${Versions.QUERYDSL}:jakarta")
    // QueryDSL APT: Q클래스 자동 생성을 위한 어노테이션 프로세서
    annotationProcessor("com.querydsl:querydsl-apt:${Versions.QUERYDSL}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // -------------------- Logging --------------------
    // Logstash Logback Encoder: JSON 형식 로그 출력 (운영 환경용)
    implementation("net.logstash.logback:logstash-logback-encoder:${Versions.LOGSTASH_ENCODER}")

    // -------------------- Lombok --------------------
    // Lombok: 보일러플레이트 코드 자동 생성 (Getter, Setter, Builder 등)
    compileOnly("org.projectlombok:lombok:${Versions.LOMBOK}")
    annotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")

    // -------------------- Testing --------------------
    // Spring Boot 테스트 스타터 (JUnit, Mockito, AssertJ 포함)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // JUnit Platform Launcher: 테스트 실행 지원
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// ===================================================
// 테스트 설정
// ===================================================
tasks.withType<Test> {
    useJUnitPlatform()
}

// ===================================================
// QueryDSL Q클래스 생성 경로 설정
// ===================================================
// QueryDSL 어노테이션 프로세서가 생성하는 Q클래스의 위치를 지정합니다.
// build/generated/querydsl 디렉토리에 Q클래스가 생성됩니다.
// ===================================================
val querydslDir = "build/generated/querydsl"

sourceSets {
    getByName("main") {
        java {
            srcDirs(querydslDir)
        }
    }
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(file(querydslDir))
}

// ===================================================
// Clean 태스크 확장
// ===================================================
// clean 태스크 실행 시 QueryDSL Q클래스 디렉토리도 함께 삭제합니다.
// ===================================================
tasks.named("clean") {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}
