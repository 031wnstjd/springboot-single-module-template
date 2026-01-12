# ===================================================
# Dockerfile - 최적화된 멀티스테이지 빌드
# ===================================================
# 이 Dockerfile은 Spring Boot Layered JAR를 활용하여
# 빌드 캐싱을 최적화하고, 보안을 강화한 이미지를 생성합니다.
#
# 주요 최적화 사항:
# 1. Layered JAR: 의존성과 애플리케이션 코드를 분리하여 캐싱 효율 향상
# 2. Non-root 사용자: 보안 강화를 위해 root가 아닌 사용자로 실행
# 3. JVM 메모리 최적화: 컨테이너 환경에 적합한 메모리 설정
# 4. 멀티스테이지 빌드: 최종 이미지 크기 최소화
# ===================================================

# ===================================================
# Stage 1: 빌드 스테이지
# ===================================================
# Gradle을 사용하여 애플리케이션을 빌드합니다.
# ===================================================
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Gradle Wrapper 복사 (캐싱 레이어 1)
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

# 빌드 설정 파일 복사 (캐싱 레이어 2)
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 의존성 사전 다운로드 (캐싱 레이어 3)
# 소스 코드 변경 시에도 의존성 캐시를 재사용할 수 있습니다.
RUN ./gradlew dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# ===================================================
# Stage 2: 레이어 추출 스테이지
# ===================================================
# Spring Boot Layered JAR를 레이어별로 추출합니다.
# ===================================================
FROM eclipse-temurin:21-jdk-jammy AS extractor
WORKDIR /app

# 빌드된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Layered JAR 추출
# - dependencies: 외부 라이브러리 (변경 빈도 낮음)
# - spring-boot-loader: Spring Boot 로더 (변경 거의 없음)
# - snapshot-dependencies: 스냅샷 의존성
# - application: 애플리케이션 코드 (변경 빈도 높음)
RUN java -Djarmode=layertools -jar app.jar extract

# ===================================================
# Stage 3: 실행 스테이지
# ===================================================
# 최소한의 런타임만 포함한 경량 이미지를 생성합니다.
# ===================================================
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# 보안 강화: non-root 사용자 생성 및 사용
RUN groupadd --system --gid 1000 appgroup && \
    useradd --system --uid 1000 --gid appgroup appuser

# 로그 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/logs && chown -R appuser:appgroup /app

# Layered JAR 레이어별 복사 (캐싱 최적화)
# 변경 빈도가 낮은 레이어를 먼저 복사합니다.
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

# 소유자 변경
RUN chown -R appuser:appgroup /app

# non-root 사용자로 전환
USER appuser

# ===================================================
# JVM 메모리 최적화 설정
# ===================================================
# -XX:+UseContainerSupport: 컨테이너 메모리 제한 인식
# -XX:MaxRAMPercentage: 컨테이너 메모리의 75% 사용
# -XX:+ExitOnOutOfMemoryError: OOM 발생 시 즉시 종료
# ===================================================
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

# 환경 변수 (기본값)
ENV SPRING_PROFILES_ACTIVE=prod

# 포트 노출
EXPOSE 8080

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher"]
