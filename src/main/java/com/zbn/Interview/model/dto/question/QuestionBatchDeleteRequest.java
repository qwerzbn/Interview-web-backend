package com.zbn.Interview.model.dto.question;

import lombok.Data;

import java.io.Serial;import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 题目批量删除请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
public class QuestionBatchDeleteRequest implements Serializable {

    /**
     * id
     */
    private List<Long> questionId;

    @Serial
    private static final long serialVersionUID = 1L;
}