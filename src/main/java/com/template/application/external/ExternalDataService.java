package com.template.application.external;

import com.template.domain.analytics.AnalyticsData;
import com.template.domain.analytics.AnalyticsDataRepository;
import com.template.infrastructure.external.sample.SampleExternalApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 외부 API 및 Multi-DB 사용 샘플 서비스
 * <p>
 * 이 서비스는 다음 기능을 시연합니다:
 * <ul>
 * <li>OpenFeign을 사용한 외부 REST API 호출</li>
 * <li>GPDB1, GPDB2 각각의 데이터소스에 접근</li>
 * <li>이종 DB(Primary/Oracle, GPDB1/PostgreSQL, GPDB2/PostgreSQL) 간 데이터 처리</li>
 * </ul>
 * <p>
 * <b>클린 아키텍처 원칙:</b>
 * <ul>
 * <li>애플리케이션 서비스는 도메인 객체와 인터페이스만 의존</li>
 * <li>Feign 클라이언트는 인프라 레이어에 위치</li>
 * <li>리포지토리 구현체는 @Qualifier로 주입</li>
 * </ul>
 */
@Slf4j
@Service
public class ExternalDataService {

    private final SampleExternalApiClient externalApiClient;
    private final AnalyticsDataRepository gpdb1Repository;
    private final AnalyticsDataRepository gpdb2Repository;

    /**
     * 생성자 주입
     * <p>
     * GPDB1, GPDB2 리포지토리를 @Qualifier로 구분하여 주입받습니다.
     */
    public ExternalDataService(
            SampleExternalApiClient externalApiClient,
            @Qualifier("gpdb1AnalyticsDataRepository") AnalyticsDataRepository gpdb1Repository,
            @Qualifier("gpdb2AnalyticsDataRepository") AnalyticsDataRepository gpdb2Repository) {
        this.externalApiClient = externalApiClient;
        this.gpdb1Repository = gpdb1Repository;
        this.gpdb2Repository = gpdb2Repository;
    }

    // ===================================================
    // OpenFeign 사용 예시
    // ===================================================

    /**
     * 외부 API에서 게시글 목록 조회 (OpenFeign)
     * <p>
     * JSONPlaceholder API를 호출하여 게시글 목록을 가져옵니다.
     *
     * @return 게시글 목록
     */
    public List<SampleExternalApiClient.Post> getExternalPosts() {
        log.info("[OpenFeign] 외부 API 호출 시작: 게시글 목록 조회");
        List<SampleExternalApiClient.Post> posts = externalApiClient.getPosts();
        log.info("[OpenFeign] 외부 API 호출 완료: {}개 게시글 조회됨", posts.size());
        return posts;
    }

    /**
     * 외부 API에서 특정 게시글 조회 (OpenFeign)
     *
     * @param id 게시글 ID
     * @return 게시글 상세 정보
     */
    public SampleExternalApiClient.Post getExternalPostById(Long id) {
        log.info("[OpenFeign] 외부 API 호출: 게시글 ID={}", id);
        return externalApiClient.getPostById(id);
    }

    /**
     * 외부 API에서 사용자별 게시글 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 게시글 목록
     */
    public List<SampleExternalApiClient.Post> getExternalPostsByUserId(Long userId) {
        log.info("[OpenFeign] 외부 API 호출: 사용자 ID={}의 게시글 조회", userId);
        return externalApiClient.getPostsByUserId(userId);
    }

    // ===================================================
    // GPDB1 접근 예시
    // ===================================================

    /**
     * GPDB1에 분석 데이터 저장
     * <p>
     * gpdb1TransactionManager를 사용하여 GPDB1에 데이터를 저장합니다.
     *
     * @param eventType 이벤트 유형
     * @param eventData 이벤트 데이터
     * @return 저장된 분석 데이터
     */
    @Transactional("gpdb1TransactionManager")
    public AnalyticsData saveToGpdb1(String eventType, String eventData) {
        log.info("[GPDB1] 분석 데이터 저장: eventType={}", eventType);
        AnalyticsData data = AnalyticsData.create(eventType, eventData);
        return gpdb1Repository.save(data);
    }

    /**
     * GPDB1에서 이벤트 유형별 데이터 조회
     *
     * @param eventType 이벤트 유형
     * @return 분석 데이터 목록
     */
    @Transactional(value = "gpdb1TransactionManager", readOnly = true)
    public List<AnalyticsData> getFromGpdb1ByEventType(String eventType) {
        log.info("[GPDB1] 분석 데이터 조회: eventType={}", eventType);
        return gpdb1Repository.findByEventType(eventType);
    }

    /**
     * GPDB1에서 전체 데이터 조회
     *
     * @return 모든 분석 데이터
     */
    @Transactional(value = "gpdb1TransactionManager", readOnly = true)
    public List<AnalyticsData> getAllFromGpdb1() {
        log.info("[GPDB1] 전체 분석 데이터 조회");
        return gpdb1Repository.findAll();
    }

    // ===================================================
    // GPDB2 접근 예시
    // ===================================================

    /**
     * GPDB2에 분석 데이터 저장
     * <p>
     * gpdb2TransactionManager를 사용하여 GPDB2에 데이터를 저장합니다.
     *
     * @param eventType 이벤트 유형
     * @param eventData 이벤트 데이터
     * @return 저장된 분석 데이터
     */
    @Transactional("gpdb2TransactionManager")
    public AnalyticsData saveToGpdb2(String eventType, String eventData) {
        log.info("[GPDB2] 분석 데이터 저장: eventType={}", eventType);
        AnalyticsData data = AnalyticsData.create(eventType, eventData);
        return gpdb2Repository.save(data);
    }

    /**
     * GPDB2에서 이벤트 유형별 데이터 조회
     *
     * @param eventType 이벤트 유형
     * @return 분석 데이터 목록
     */
    @Transactional(value = "gpdb2TransactionManager", readOnly = true)
    public List<AnalyticsData> getFromGpdb2ByEventType(String eventType) {
        log.info("[GPDB2] 분석 데이터 조회: eventType={}", eventType);
        return gpdb2Repository.findByEventType(eventType);
    }

    /**
     * GPDB2에서 전체 데이터 조회
     *
     * @return 모든 분석 데이터
     */
    @Transactional(value = "gpdb2TransactionManager", readOnly = true)
    public List<AnalyticsData> getAllFromGpdb2() {
        log.info("[GPDB2] 전체 분석 데이터 조회");
        return gpdb2Repository.findAll();
    }

    // ===================================================
    // 복합 사용 예시 (외부 API + Multi-DB)
    // ===================================================

    /**
     * 외부 API 데이터를 가져와서 GPDB1, GPDB2에 각각 저장 (ETL 시뮬레이션)
     * <p>
     * 외부 API에서 게시글을 조회하여:
     * - GPDB1에는 게시글 제목 이벤트로 저장
     * - GPDB2에는 게시글 본문 이벤트로 저장
     *
     * @param postId 외부 API 게시글 ID
     */
    public void fetchAndStoreAnalytics(Long postId) {
        log.info("[ETL] 외부 데이터 수집 및 분석 DB 저장 시작: postId={}", postId);

        // 1. 외부 API에서 데이터 조회
        SampleExternalApiClient.Post post = externalApiClient.getPostById(postId);
        log.info("[ETL] 외부 데이터 조회 완료: title={}", post.title());

        // 2. GPDB1에 제목 이벤트 저장
        saveToGpdb1("POST_TITLE", post.title());
        log.info("[ETL] GPDB1에 제목 이벤트 저장 완료");

        // 3. GPDB2에 본문 이벤트 저장
        saveToGpdb2("POST_BODY", post.body());
        log.info("[ETL] GPDB2에 본문 이벤트 저장 완료");

        log.info("[ETL] 외부 데이터 수집 및 분석 DB 저장 완료");
    }
}
