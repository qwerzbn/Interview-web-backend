package com.zbn.Interview.model.dto.questionbankquestion;

import lombok.Data;

import java.util.List;

/**
 * 批量添加题目请求
 *
 * @author zbn
 * @date 2024/11/1
 */
@Data
public class QuestionBankQuestionBatchAddRequest {
    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private List<Long> questionId;
    /**
     * 题目顺序（题号）
     */
    private Integer questionOrder;
}
