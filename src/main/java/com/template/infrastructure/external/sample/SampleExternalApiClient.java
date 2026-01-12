package com.template.infrastructure.external.sample;

import com.template.infrastructure.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 샘플 외부 API 클라이언트 (OpenFeign)
 * <p>
 * OpenFeign을 사용한 외부 REST API 호출 예시입니다.
 * 인터페이스와 어노테이션만으로 HTTP 클라이언트를 선언적으로 정의합니다.
 * <p>
 * <b>@FeignClient 주요 속성:</b>
 * <ul>
 * <li>name: Feign 클라이언트 이름 (필수, 유일해야 함)</li>
 * <li>url: 호출할 외부 API의 기본 URL</li>
 * <li>configuration: 클라이언트별 설정 클래스</li>
 * <li>fallback/fallbackFactory: 서킷브레이커 폴백 처리</li>
 * </ul>
 * <p>
 * <b>사용 예시:</b>
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;Service
 *     @RequiredArgsConstructor
 *     public class ExternalDataService {
 *         private final SampleExternalApiClient apiClient;
 * 
 *         public SampleExternalApiClient.Post getPost(Long id) {
 *             return apiClient.getPostById(id);
 *         }
 *     }
 * }
 * </pre>
 * <p>
 * <b>주의:</b> 실제 사용 시 url을 application.yml의 외부 설정으로 관리하세요.
 * 예: url = "${external.api.sample.url}"
 *
 * @see com.template.infrastructure.config.feign.FeignConfig
 */
@FeignClient(name = "sampleExternalApi", url = "${external.api.sample.url:https://jsonplaceholder.typicode.com}", configuration = FeignConfig.class)
public interface SampleExternalApiClient {

    /**
     * 게시글 목록 조회
     * <p>
     * GET /posts
     *
     * @return 게시글 목록
     */
    @GetMapping("/posts")
    List<Post> getPosts();

    /**
     * 게시글 상세 조회
     * <p>
     * GET /posts/{id}
     *
     * @param id 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/posts/{id}")
    Post getPostById(@PathVariable("id") Long id);

    /**
     * 게시글 검색 (쿼리 파라미터)
     * <p>
     * GET /posts?userId={userId}
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 게시글 목록
     */
    @GetMapping("/posts")
    List<Post> getPostsByUserId(@RequestParam("userId") Long userId);

    /**
     * 게시글 응답 DTO
     * <p>
     * 외부 API 응답을 매핑하는 내부 레코드입니다.
     * Java 17+ 레코드를 사용하여 불변 객체로 정의합니다.
     */
    record Post(
            Long id,
            Long userId,
            String title,
            String body) {
    }
}
