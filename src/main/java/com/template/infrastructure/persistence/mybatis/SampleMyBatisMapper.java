package com.template.infrastructure.persistence.mybatis;

import com.template.domain.sample.entity.Sample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis 매퍼 인터페이스
 */
@Mapper
public interface SampleMyBatisMapper {

    /**
     * 특정 제목을 포함하는 샘플 목록 조회 (MyBatis 예시)
     */
    List<Sample> selectByTitle(@Param("title") String title);

}
