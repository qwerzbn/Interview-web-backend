package com.zbn.Interview.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑题目请求(给用户使用)
 *
 * @author zbn
 * @date 2024/10/24
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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

    /**
     * 编辑时间
     */
    private Date editTime;

    private static final long serialVersionUID = 1L;
}