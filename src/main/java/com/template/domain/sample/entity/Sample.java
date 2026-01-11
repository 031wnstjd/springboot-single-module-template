package com.template.domain.sample.entity;

import com.template.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 샘플 도메인 엔티티
 * <p>
 * 실무에서는 Setter 사용을 지양하고 의미 있는 메서드를 통해 상태를 변경하도록 합니다.
 */
@Entity
@Table(name = "samples")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 제목과 내용을 업데이트하는 비즈니스 메서드
     */
    public void update(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        this.title = title;
        this.content = content;
    }
}
