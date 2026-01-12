# Spring Boot Single Module Template

실무 베스트 프랙티스를 적용한 Docker 기반 Spring Boot 싱글 모듈 템플릿 프로젝트입니다.

> 🎯 **목적**: 신규 프로젝트 구축 시 팀 표준으로 사용할 수 있는 실무 최적화 템플릿

---

## 📋 목차

- [주요 특징](#-주요-특징)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [시작하기](#-시작하기)
- [Multi-DB 구성](#-multi-db-구성)
- [OpenFeign (외부 API 연동)](#-openfeign-외부-api-연동)
- [로깅 설정](#-로깅-설정)
- [모니터링 (Actuator)](#-모니터링-actuator)
- [Gradle Version Catalog](#-gradle-version-catalog)
- [Docker 사용법](#-docker-사용법)
- [API 사용법](#-api-사용법)

---

## 🚀 주요 특징

| 구분 | 내용 |
|------|------|
| **아키텍처** | Clean Architecture + Domain-Driven Design (DDD) |
| **Multi-DB** | Oracle (Primary) + PostgreSQL/GPDB (2개) 동시 지원 |
| **외부 API 연동** | OpenFeign (선언적 HTTP 클라이언트) |
| **데이터 접근** | JPA (QueryDSL) + MyBatis 동시 지원 |
| **로깅** | Logback 프로파일별 분리 (local/dev/prod) |
| **모니터링** | Spring Actuator (Health, Metrics, Prometheus) |
| **버전 관리** | Gradle Version Catalog 중앙 집중식 관리 |
| **컨테이너화** | Docker Layered JAR 최적화 |

---

## 🛠 기술 스택

### Core
- **Java 21** (LTS)
- **Spring Boot 3.3.5**
- **Spring Cloud 2023.0.3** (OpenFeign)
- **Gradle 8.10.2** (Kotlin DSL + Version Catalog)

### Data Access
- **Spring Data JPA** + **QueryDSL 5.1.0**
- **MyBatis 3.0.3**

### Database
- **Oracle** - Primary 데이터소스
- **PostgreSQL** - GPDB1, GPDB2 (Greenplum)
- **H2** - 로컬 개발용 인메모리 DB

### Monitoring & Logging
- **Spring Actuator** - Health, Metrics, Prometheus
- **Logback** - 환경별 설정 (JSON, 롤링)

---

## 📁 프로젝트 구조

```text
src/main/java/com/template/
├── TemplateApplication.java
├── domain/                           # 🟢 도메인 레이어
├── application/                      # 🔵 애플리케이션 레이어
├── infrastructure/                   # 🟠 인프라스트럭처 레이어
│   ├── config/
│   │   ├── datasource/               # Multi-DB 설정 (Oracle, GPDB1, GPDB2)
│   │   ├── feign/                    # ⭐ OpenFeign 설정
│   │   │   ├── FeignConfig.java      # 전역 Feign 설정
│   │   │   └── FeignErrorDecoder.java # 에러 처리
│   │   ├── jpa/, querydsl/, mybatis/
│   └── external/                     # ⭐ 외부 API 클라이언트
│       └── sample/SampleExternalApiClient.java
├── presentation/                     # 🟣 프레젠테이션 레이어
└── support/                          # ⚪ 서포트 레이어

src/main/resources/
├── application.yml                   # 공통 설정
├── application-local.yml             # 로컬 환경
├── application-dev.yml               # 개발 환경
├── application-prod.yml              # 운영 환경
├── logback-spring.xml                # ⭐ Logback 메인 (프로파일 분기)
├── logback-local.xml                 # ⭐ 로컬 로깅 설정
├── logback-dev.xml                   # ⭐ 개발 로깅 설정
├── logback-prod.xml                  # ⭐ 운영 로깅 설정 (JSON)
└── mybatis/mapper/
```

---

## 🚀 시작하기

### 사전 요구사항
- Java 21+
- Docker (선택)

### 로컬 실행
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 접속 정보
| 서비스 | URL |
|--------|-----|
| API | http://localhost:8080/api/v1/samples |
| H2 Console | http://localhost:8080/h2-console |
| Actuator Health | http://localhost:8080/actuator/health |
| Actuator Metrics | http://localhost:8080/actuator/metrics |

---

## 🗄 Multi-DB 구성

### 데이터소스 구조

| 데이터소스 | 타입 | 용도 |
|------------|------|------|
| Primary | Oracle | 주 트랜잭션 DB |
| GPDB1 | PostgreSQL | 분석 DB #1 |
| GPDB2 | PostgreSQL | 분석 DB #2 |

### 사용 방법

```java
// Primary 데이터소스 (기본)
@Transactional
public void saveToOracle(Sample sample) { ... }

// GPDB1 데이터소스 (명시적 지정)
@Transactional("gpdb1TransactionManager")
public void saveToGpdb1(Data data) { ... }
```

---

## 🌐 OpenFeign (외부 API 연동)

### 개요
OpenFeign은 선언적 HTTP 클라이언트로, 인터페이스와 어노테이션만으로 외부 REST API 호출을 구현합니다.

### 설정 파일
- `FeignConfig.java`: 전역 설정 (타임아웃, 재시도, 로깅 레벨)
- `FeignErrorDecoder.java`: HTTP 에러를 BusinessException으로 변환

### 사용 예시

**1. Feign 클라이언트 정의:**
```java
@FeignClient(
    name = "sampleApi",
    url = "${external.api.sample.url}",
    configuration = FeignConfig.class
)
public interface SampleExternalApiClient {
    @GetMapping("/posts/{id}")
    Post getPostById(@PathVariable("id") Long id);
}
```

**2. 서비스에서 사용:**
```java
@Service
@RequiredArgsConstructor
public class ExternalDataService {
    private final SampleExternalApiClient apiClient;
    
    public Post getPost(Long id) {
        return apiClient.getPostById(id);
    }
}
```

### 설정 (application.yml)
```yaml
external:
  api:
    sample:
      url: https://jsonplaceholder.typicode.com

spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            connect-timeout: 5000
            read-timeout: 10000
```

---

## 📝 로깅 설정

### 프로파일별 분리 구조

```
logback-spring.xml          # 메인 (프로파일별 include)
├── logback-local.xml       # local 프로파일
├── logback-dev.xml         # dev 프로파일
└── logback-prod.xml        # prod 프로파일
```

### 환경별 로깅 전략

| 환경 | 레벨 | 출력 | 포맷 | 특징 |
|------|------|------|------|------|
| local | DEBUG | 콘솔 | 컬러 텍스트 | SQL, Feign 로깅 활성화 |
| dev | INFO | 콘솔+파일 | 텍스트 | 일별 롤링, gzip 압축 |
| prod | WARN | 파일 | JSON | 비동기, ELK 연동용 |

### 로그 파일 위치
- 경로: `./logs/springboot-template.log`
- 에러 전용: `./logs/springboot-template-error.log` (prod)

---

## 📊 모니터링 (Actuator)

### 활성화된 엔드포인트

| 엔드포인트 | URL | 설명 |
|------------|-----|------|
| Health | `/actuator/health` | 애플리케이션 상태 |
| Info | `/actuator/info` | 애플리케이션 정보 |
| Metrics | `/actuator/metrics` | 메트릭 목록 |
| Prometheus | `/actuator/prometheus` | Prometheus 형식 메트릭 |

### 설정 (application.yml)
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when_authorized
```

### Prometheus + Grafana 연동

```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

---

## 📦 Gradle Version Catalog

### 버전 중앙 관리

모든 의존성 버전은 `gradle/libs.versions.toml`에서 관리합니다.

```toml
[versions]
spring-boot = "3.3.5"
spring-cloud = "2023.0.3"
querydsl = "5.1.0"

[libraries]
spring-boot-starter-actuator = { module = "org.springframework.boot:spring-boot-starter-actuator" }
spring-cloud-starter-openfeign = { module = "org.springframework.cloud:spring-cloud-starter-openfeign" }
```

---

## 🐳 Docker 사용법

### 최적화된 빌드
- **Layered JAR**: 의존성과 코드 분리로 캐싱 효율 향상
- **Non-root 사용자**: 보안 강화
- **JVM 튜닝**: 컨테이너 메모리 최적화

### 빌드 및 실행
```bash
# 이미지 빌드
docker build -t springboot-template .

# 실행
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local springboot-template

# Docker Compose (전체 스택)
docker-compose up -d
```

---

## 📡 API 사용법

### 샘플 API

| Method | Endpoint | 설명 |
|--------|----------|------|
| `POST` | `/api/v1/samples` | 생성 |
| `GET` | `/api/v1/samples` | 전체 조회 |
| `GET` | `/api/v1/samples/{id}` | 상세 조회 |
| `PUT` | `/api/v1/samples/{id}` | 수정 |
| `DELETE` | `/api/v1/samples/{id}` | 삭제 |

### 응답 형식
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": { ... },
  "errorCode": null
}
```

---

## 📄 라이선스

이 프로젝트는 팀 표준 템플릿으로 자유롭게 사용 및 수정이 가능합니다.
