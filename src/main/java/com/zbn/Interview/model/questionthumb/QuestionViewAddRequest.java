package com.zbn.Interview.model.questionthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目浏览请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
public class QuestionViewAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}