package com.zbn.Interview.model.dto.question;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建题目请求
 *
 * @author zbn
 * @date 2024/10/24
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;
    /**
     * 推荐答案
     */
    private String answer;
    /**
     * 仅会员可见（1 表示仅会员可见）
     */
    private Integer needVip;
    /**
     * 题目来源
     */
    private String source;

    /**
     * 优先级
     */
    private Integer priority;
       @Serial
    private static final long serialVersionUID = 1L;
}