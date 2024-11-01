package com.zbn.Interview.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除题目题库关系请求
 */
@Data
public class QuestionBankQuestionBatchRemoveRequest implements Serializable {
    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;

       @Serial
    private static final long serialVersionUID = 1L;
}
