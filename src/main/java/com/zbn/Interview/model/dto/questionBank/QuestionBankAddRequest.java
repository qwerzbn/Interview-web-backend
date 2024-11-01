package com.zbn.Interview.model.dto.questionBank;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建题库请求
 *
 * @author zbn
 * @date 2024/10/24
 */
@Data
public class QuestionBankAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;
    /**
     * 优先级
     */
    private Integer priority;

       @Serial
    private static final long serialVersionUID = 1L;
}