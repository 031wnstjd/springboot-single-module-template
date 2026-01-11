# Spring Boot Single Module Template

실무 베스트 프랙티스를 적용한 Docker 기반 Spring Boot 싱글 모듈 템플릿 프로젝트입니다.

> 🎯 **목적**: 신규 프로젝트 구축 시 팀 표준으로 사용할 수 있는 실무 최적화 템플릿

---

## 📋 목차

- [주요 특징](#-주요-특징)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [아키텍처 설계](#-아키텍처-설계)
- [시작하기](#-시작하기)
- [환경별 설정](#-환경별-설정)
- [API 사용법](#-api-사용법)
- [주요 컴포넌트 가이드](#-주요-컴포넌트-가이드)
- [Docker 사용법](#-docker-사용법)
- [테스트](#-테스트)
- [라이선스](#-라이선스)

---

## 🚀 주요 특징

| 구분 | 내용 |
|------|------|
| **아키텍처** | Clean Architecture + Domain-Driven Design (DDD) |
| **의존성 관리** | 의존성 역전 원칙(DIP) 준수, 레이어 간 단방향 의존성 |
| **데이터 접근** | JPA (QueryDSL 포함) + MyBatis 동시 지원 |
| **환경 분리** | local / dev / prod 프로파일 기반 설정 |
| **컨테이너화** | Docker 멀티스테이지 빌드 최적화 |
| **코드 품질** | 상세한 한글 주석, 공통 예외/응답 처리, AOP 로깅 |

---

## 🛠 기술 스택

### Core
- **Java 21** (LTS)
- **Spring Boot 3.3.5**
- **Gradle 8.10.2** (Kotlin DSL)

### Data Access
- **Spring Data JPA** - ORM 기반 데이터 접근
- **QueryDSL 5.1.0** (jakarta) - 타입 세이프 동적 쿼리
- **MyBatis 3.0.3** - SQL 매퍼 프레임워크

### Database
- **H2** - 로컬 개발용 인메모리 DB
- **PostgreSQL 15** - 개발/운영 환경

### Infrastructure
- **Docker** - 컨테이너화
- **Docker Compose** - 로컬 개발 환경 오케스트레이션

---

## 📁 프로젝트 구조

```text
src/main/java/com/template/
│
├── TemplateApplication.java          # 애플리케이션 진입점
│
├── domain/                           # 🟢 도메인 레이어 (순수 비즈니스 로직)
│   ├── common/
│   │   ├── entity/
│   │   │   └── BaseEntity.java       # 공통 엔티티 (생성/수정 시간)
│   │   └── exception/
│   │       └── BusinessException.java # 비즈니스 예외 기본 클래스
│   │
│   └── sample/                        # 샘플 도메인
│       ├── entity/
│       │   └── Sample.java           # 샘플 엔티티
│       └── repository/
│           └── SampleRepository.java # 리포지토리 인터페이스 (Port)
│
├── application/                      # 🔵 애플리케이션 레이어 (유스케이스)
│   └── sample/
│       ├── dto/
│       │   ├── SampleRequest.java    # 요청 DTO
│       │   └── SampleResponse.java   # 응답 DTO
│       └── usecase/
│           ├── SampleUseCase.java    # 유스케이스 인터페이스
│           └── SampleService.java    # 유스케이스 구현체
│
├── infrastructure/                   # 🟠 인프라스트럭처 레이어 (기술적 구현)
│   ├── config/
│   │   ├── jpa/
│   │   │   └── JpaConfig.java        # JPA Auditing 설정
│   │   ├── querydsl/
│   │   │   └── QueryDslConfig.java   # JPAQueryFactory 빈 설정
│   │   └── mybatis/
│   │       └── MyBatisConfig.java    # MyBatis 매퍼 스캔 설정
│   │
│   └── persistence/
│       ├── jpa/
│       │   ├── SampleJpaRepository.java    # Spring Data JPA
│       │   └── SampleRepositoryImpl.java   # 도메인 리포지토리 구현 (QueryDSL)
│       └── mybatis/
│           └── SampleMyBatisMapper.java    # MyBatis 매퍼 인터페이스
│
├── presentation/                     # 🟣 프레젠테이션 레이어 (API)
│   ├── common/
│   │   ├── response/
│   │   │   └── ApiResponse.java      # 공통 API 응답 포맷
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java # 전역 예외 처리
│   │
│   └── api/
│       └── sample/
│           └── SampleController.java # REST API 컨트롤러
│
└── support/                          # ⚪ 서포트 레이어 (공통 유틸리티)
    ├── aop/
    │   └── LoggingAspect.java        # API 로깅 AOP
    └── util/
        └── StringUtils.java          # 문자열 유틸리티
```

---

## 🏛 아키텍처 설계

### 레이어 의존성 규칙

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│              (Controller, ApiResponse, Exception)            │
└──────────────────────────┬──────────────────────────────────┘
                           │ 의존
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    Application Layer                         │
│                 (UseCase, Service, DTO)                      │
└──────────────────────────┬──────────────────────────────────┘
                           │ 의존
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│           (Entity, Repository Interface, Exception)          │
│                  ❌ 외부 의존성 없음 ❌                        │
└─────────────────────────────────────────────────────────────┘
                           ▲
                           │ 구현 (의존성 역전)
┌─────────────────────────────────────────────────────────────┐
│                  Infrastructure Layer                        │
│        (JPA Repository, QueryDSL, MyBatis, Config)           │
└─────────────────────────────────────────────────────────────┘
```

### 핵심 원칙

1. **의존성 역전 원칙 (DIP)**
   - 도메인 레이어는 외부 기술에 의존하지 않습니다.
   - `SampleRepository` 인터페이스는 도메인에 정의되고, `SampleRepositoryImpl`은 인프라에서 구현합니다.

2. **단방향 의존성**
   - `Presentation → Application → Domain ← Infrastructure`
   - 상위 레이어는 하위 레이어만 의존합니다.

3. **관심사 분리**
   - 각 레이어는 명확한 책임을 가집니다.
   - 비즈니스 로직은 도메인에, 기술적 구현은 인프라에 위치합니다.

---

## 🚀 시작하기

### 사전 요구사항

- Java 21+
- Docker (선택사항)

### 로컬 실행 (H2 인메모리 DB)

```bash
# 프로젝트 클론
git clone <repository-url>
cd springboot-single-module-template

# 빌드
./gradlew build

# 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 접속 정보

| 서비스 | URL |
|--------|-----|
| API | http://localhost:8080/api/v1/samples |
| H2 Console | http://localhost:8080/h2-console |

**H2 Console 접속 정보:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (비워두기)

---

## ⚙ 환경별 설정

### 프로파일 구조

| 프로파일 | 용도 | 데이터베이스 |
|----------|------|-------------|
| `local` | 로컬 개발 | H2 인메모리 |
| `dev` | 개발 서버 | PostgreSQL |
| `prod` | 운영 서버 | PostgreSQL (환경변수) |

### 환경변수 (prod)

```bash
export DB_HOST=your-db-host
export DB_PORT=5432
export DB_NAME=your-db-name
export DB_USERNAME=your-username
export DB_PASSWORD=your-password
```

### 프로파일 적용 방법

```bash
# Gradle
./gradlew bootRun --args='--spring.profiles.active=dev'

# JAR 실행
java -jar app.jar --spring.profiles.active=prod

# Docker
docker run -e SPRING_PROFILES_ACTIVE=prod your-image
```

---

## 📡 API 사용법

### 샘플 API 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| `POST` | `/api/v1/samples` | 샘플 생성 |
| `GET` | `/api/v1/samples` | 전체 샘플 조회 |
| `GET` | `/api/v1/samples/{id}` | 샘플 상세 조회 |
| `PUT` | `/api/v1/samples/{id}` | 샘플 수정 |
| `DELETE` | `/api/v1/samples/{id}` | 샘플 삭제 |

### 요청/응답 예시

**생성 요청:**
```bash
curl -X POST http://localhost:8080/api/v1/samples \
  -H "Content-Type: application/json" \
  -d '{"title": "Hello", "content": "World"}'
```

**응답 형식:**
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "title": "Hello",
    "content": "World",
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  },
  "errorCode": null
}
```

**에러 응답:**
```json
{
  "success": false,
  "message": "샘플을 찾을 수 없습니다.",
  "data": null,
  "errorCode": "SAMPLE_NOT_FOUND"
}
```

---

## 📚 주요 컴포넌트 가이드

### 1. QueryDSL 사용법

`SampleRepositoryImpl`에서 동적 쿼리 작성 예시:

```java
@Repository
@RequiredArgsConstructor
public class SampleRepositoryImpl implements SampleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Sample> findByTitleContaining(String title) {
        QSample sample = QSample.sample;
        
        return queryFactory
                .selectFrom(sample)
                .where(sample.title.containsIgnoreCase(title))
                .fetch();
    }
}
```

**Q클래스 생성 위치:** `build/generated/querydsl`

### 2. MyBatis 사용법

**Mapper 인터페이스:**
```java
@Mapper
public interface SampleMyBatisMapper {
    List<Sample> selectByTitle(@Param("title") String title);
}
```

**XML 매퍼 (resources/mybatis/mapper/SampleMapper.xml):**
```xml
<select id="selectByTitle" resultType="Sample">
    SELECT * FROM samples WHERE title LIKE CONCAT('%', #{title}, '%')
</select>
```

### 3. 예외 처리

**비즈니스 예외 발생:**
```java
throw new BusinessException("샘플을 찾을 수 없습니다.", "SAMPLE_NOT_FOUND");
```

`GlobalExceptionHandler`에서 자동으로 캐치하여 `ApiResponse` 형태로 응답합니다.

### 4. AOP 로깅

`LoggingAspect`가 모든 API 호출에 대해 자동으로 로깅합니다:

```
[API Request] GET /api/v1/samples | Method: SampleController.getAll | Args: []
[API Response] GET /api/v1/samples | Time: 15ms
```

---

## 🐳 Docker 사용법

### 이미지 빌드

```bash
docker build -t springboot-template .
```

### 단독 실행 (로컬 프로파일)

```bash
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local springboot-template
```

### Docker Compose

```bash
# 전체 스택 실행 (App + PostgreSQL)
docker-compose up -d

# 로그 확인
docker-compose logs -f app

# 종료
docker-compose down
```

### Dockerfile 멀티스테이지 빌드 구조

```dockerfile
# 1단계: 빌드
FROM eclipse-temurin:21-jdk-jammy AS build
# 의존성 캐싱 및 JAR 생성

# 2단계: 실행
FROM eclipse-temurin:21-jre-jammy
# 경량화된 JRE 이미지로 실행
```

---

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "SampleServiceTest"

# 테스트 리포트 확인
# build/reports/tests/test/index.html
```

---

## 📄 라이선스

이 프로젝트는 팀 표준 템플릿으로 자유롭게 사용 및 수정이 가능합니다.

---

## 🤝 기여 가이드

1. 이 저장소를 Fork 합니다.
2. 새 브랜치를 생성합니다: `git checkout -b feature/amazing-feature`
3. 변경사항을 커밋합니다: `git commit -m 'feat: Add amazing feature'`
4. 브랜치에 Push 합니다: `git push origin feature/amazing-feature`
5. Pull Request를 생성합니다.

---

## 📞 문의

프로젝트에 대한 문의사항은 이슈를 통해 등록해 주세요.
