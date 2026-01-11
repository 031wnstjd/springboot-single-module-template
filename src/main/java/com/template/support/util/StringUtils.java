package com.template.support.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 문자열 관련 공통 유틸리티
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
