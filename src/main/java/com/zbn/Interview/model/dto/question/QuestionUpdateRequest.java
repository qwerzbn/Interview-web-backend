package com.zbn.Interview.model.dto.question;

import lombok.Data;

import java.io.Serial;import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 更新题目请求
 *
 * @author zbn
 * @date 2024/10/24
 */
@Data
public class QuestionUpdateRequest implements Serializable {

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
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    @Serial
    private static final long serialVersionUID = 1L;
}