package com.zbn.Interview.model.dto.questionbankquestion;

import lombok.Data;

@Data
public class QuestionBankQuestionAddRequest {
    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;
    /**
     * 题目顺序（题号）
     */
    private Integer questionOrder;
}
