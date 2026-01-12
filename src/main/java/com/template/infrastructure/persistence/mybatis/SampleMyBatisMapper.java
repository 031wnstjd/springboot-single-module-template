package com.template.infrastructure.persistence.mybatis;

import com.template.domain.sample.Sample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis 매퍼 인터페이스 (샘플)
 * <p>
 * MyBatis를 사용하여 SQL 쿼리를 실행하는 매퍼입니다.
 * 실제 SQL은 resources/mybatis/mapper/SampleMapper.xml에 정의됩니다.
 * <p>
 * <b>참고:</b>
 * MyBatis는 도메인 POJO 객체를 직접 반환합니다.
 * 결과 매핑은 XML 파일의 resultMap에서 정의합니다.
 */
@Mapper
public interface SampleMyBatisMapper {

    /**
     * 특정 제목을 포함하는 샘플 목록 조회 (MyBatis 예시)
     *
     * @param title 검색할 제목 키워드
     * @return 제목에 키워드를 포함하는 샘플 목록
     */
    List<Sample> selectByTitle(@Param("title") String title);
}
