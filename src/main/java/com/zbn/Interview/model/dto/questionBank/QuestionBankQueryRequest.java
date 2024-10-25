package com.zbn.Interview.model.dto.questionBank;

import com.zbn.Interview.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询题库请求
 *
 * @author zbn
 * @date 2024/10/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionBankQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 是否需要查询题目
     */
    private Boolean needQuestionQuery = false;
    /**
     * 审核人 id
     */
    private Long reviewerId;

    private static final long serialVersionUID = 1L;
}