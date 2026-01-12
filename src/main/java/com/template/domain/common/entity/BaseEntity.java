package com.template.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 JPA 엔티티의 공통 속성을 정의하는 추상 기본 클래스
 * <p>
 * 이 클래스는 모든 엔티티가 공통으로 가져야 하는 생성 시간과 수정 시간을
 * 자동으로 관리합니다. JPA Auditing 기능을 활용하여 엔티티가 저장되거나
 * 수정될 때 자동으로 시간이 기록됩니다.
 * <p>
 * <b>주요 어노테이션 설명:</b>
 * <ul>
 * <li><b>@MappedSuperclass</b>: 이 클래스는 테이블로 매핑되지 않고,
 * 자식 엔티티에게 매핑 정보(컬럼, 리스너 등)를 상속합니다</li>
 * <li><b>@EntityListeners(AuditingEntityListener.class)</b>:
 * JPA Auditing 이벤트를 감지하여 @CreatedDate, @LastModifiedDate를 처리합니다</li>
 * </ul>
 * <p>
 * <b>사용 예시:</b>
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;Entity
 *     public class User extends BaseEntity {
 *         @Id
 *         private Long id;
 *         private String name;
 *         // createdAt, updatedAt은 BaseEntity에서 상속
 *     }
 * }
 * </pre>
 * <p>
 * <b>주의사항:</b>
 * JPA Auditing이 동작하려면 @EnableJpaAuditing 설정이 필요합니다.
 * (JpaConfig 클래스 참조)
 *
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * 엔티티 생성 시간
     * <p>
     * 엔티티가 처음 저장(persist)될 때 자동으로 현재 시간이 설정됩니다.
     * 한 번 설정되면 변경되지 않습니다 (updatable = false).
     */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 최종 수정 시간
     * <p>
     * 엔티티가 수정(update)될 때마다 자동으로 현재 시간으로 갱신됩니다.
     * 처음 저장 시에도 생성 시간과 동일하게 설정됩니다.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
