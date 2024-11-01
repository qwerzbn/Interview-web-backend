package com.zbn.Interview.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除题目题库关系请求
 */
@Data
public class QuestionBankQuestionQueryRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;
    private static final long serialVersionUID = 1L;
}
