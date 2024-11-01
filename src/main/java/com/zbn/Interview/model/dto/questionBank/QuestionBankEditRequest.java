package com.zbn.Interview.model.dto.questionBank;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 编辑题库请求(给用户使用)
 *
 * @author zbn
 * @date 2024/10/24
 */
@Data
public class QuestionBankEditRequest implements Serializable {

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
     * 优先级
     */
    private Integer priority;
    /**
     * 编辑时间
     */
    private Date editTime;


       @Serial
    private static final long serialVersionUID = 1L;
}