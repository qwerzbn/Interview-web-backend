package com.zbn.Interview.model.dto.questionthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点赞请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
public class QuestionThumbAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}