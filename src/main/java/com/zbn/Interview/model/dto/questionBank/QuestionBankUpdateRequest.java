package com.zbn.Interview.model.dto.questionBank;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 更新题库请求
 *
 * @author zbn
 * @date 2024/10/24
 */
@Data
public class QuestionBankUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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
     * 创建用户 id
     */
    private Long userId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 浏览量
     */
    private Integer viewNum;
    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 审核时间
     */
    private Date reviewTime;
       @Serial
    private static final long serialVersionUID = 1L;
}