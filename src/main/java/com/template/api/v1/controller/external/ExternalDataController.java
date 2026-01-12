package com.template.api.v1.controller.external;

import com.template.application.external.ExternalDataService;
import com.template.domain.analytics.AnalyticsData;
import com.template.infrastructure.external.sample.SampleExternalApiClient;
import com.template.api.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 외부 API 및 Multi-DB 샘플 컨트롤러
 * <p>
 * OpenFeign 외부 API 호출과 GPDB1/GPDB2 접근을 시연하는 API입니다.
 */
@RestController
@RequestMapping("/api/v1/external")
@RequiredArgsConstructor
public class ExternalDataController {

    private final ExternalDataService externalDataService;

    // ===================================================
    // OpenFeign 샘플 API
    // ===================================================

    /**
     * 외부 API 게시글 목록 조회
     */
    @GetMapping("/posts")
    public ApiResponse<List<SampleExternalApiClient.Post>> getExternalPosts() {
        return ApiResponse.success(externalDataService.getExternalPosts());
    }

    /**
     * 외부 API 게시글 상세 조회
     */
    @GetMapping("/posts/{id}")
    public ApiResponse<SampleExternalApiClient.Post> getExternalPost(@PathVariable Long id) {
        return ApiResponse.success(externalDataService.getExternalPostById(id));
    }

    /**
     * 외부 API 사용자별 게시글 조회
     */
    @GetMapping("/posts/user/{userId}")
    public ApiResponse<List<SampleExternalApiClient.Post>> getExternalPostsByUser(@PathVariable Long userId) {
        return ApiResponse.success(externalDataService.getExternalPostsByUserId(userId));
    }

    // ===================================================
    // GPDB1 샘플 API
    // ===================================================

    /**
     * GPDB1에 분석 데이터 저장
     */
    @PostMapping("/gpdb1/analytics")
    public ApiResponse<AnalyticsData> saveToGpdb1(
            @RequestParam String eventType,
            @RequestParam String eventData) {
        return ApiResponse.success(externalDataService.saveToGpdb1(eventType, eventData));
    }

    /**
     * GPDB1에서 이벤트 유형별 조회
     */
    @GetMapping("/gpdb1/analytics")
    public ApiResponse<List<AnalyticsData>> getFromGpdb1(@RequestParam String eventType) {
        return ApiResponse.success(externalDataService.getFromGpdb1ByEventType(eventType));
    }

    /**
     * GPDB1 전체 데이터 조회
     */
    @GetMapping("/gpdb1/analytics/all")
    public ApiResponse<List<AnalyticsData>> getAllFromGpdb1() {
        return ApiResponse.success(externalDataService.getAllFromGpdb1());
    }

    // ===================================================
    // GPDB2 샘플 API
    // ===================================================

    /**
     * GPDB2에 분석 데이터 저장
     */
    @PostMapping("/gpdb2/analytics")
    public ApiResponse<AnalyticsData> saveToGpdb2(
            @RequestParam String eventType,
            @RequestParam String eventData) {
        return ApiResponse.success(externalDataService.saveToGpdb2(eventType, eventData));
    }

    /**
     * GPDB2에서 이벤트 유형별 조회
     */
    @GetMapping("/gpdb2/analytics")
    public ApiResponse<List<AnalyticsData>> getFromGpdb2(@RequestParam String eventType) {
        return ApiResponse.success(externalDataService.getFromGpdb2ByEventType(eventType));
    }

    /**
     * GPDB2 전체 데이터 조회
     */
    @GetMapping("/gpdb2/analytics/all")
    public ApiResponse<List<AnalyticsData>> getAllFromGpdb2() {
        return ApiResponse.success(externalDataService.getAllFromGpdb2());
    }

    // ===================================================
    // 복합 API (외부 API + Multi-DB)
    // ===================================================

    /**
     * 외부 데이터 수집 후 GPDB에 저장 (ETL 시뮬레이션)
     */
    @PostMapping("/etl/{postId}")
    public ApiResponse<Void> fetchAndStore(@PathVariable Long postId) {
        externalDataService.fetchAndStoreAnalytics(postId);
        return ApiResponse.success();
    }
}
