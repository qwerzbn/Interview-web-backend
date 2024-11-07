package com.zbn.Interview.model.dto.question;

import com.zbn.Interview.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 查询题目请求
 *
 * @author zbn
 * @date 2024/10/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;
    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 搜索词
     */
    private String searchText;

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
     * 创建用户 id
     */
    private Long userId;

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
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;
    /**
     * 审核人 id
     */
    private Long reviewerId;


    @Serial
    private static final long serialVersionUID = 1L;
}