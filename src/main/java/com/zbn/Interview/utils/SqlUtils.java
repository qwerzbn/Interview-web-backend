package com.zbn.Interview.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL 工具
 *
 * @author zbn
 * @date 2024/10/23
 */
public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
