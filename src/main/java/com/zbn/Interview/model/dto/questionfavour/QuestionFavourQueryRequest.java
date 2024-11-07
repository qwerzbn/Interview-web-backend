package com.zbn.Interview.model.dto.questionfavour;

import com.zbn.Interview.common.PageRequest;
import com.zbn.Interview.model.dto.question.QuestionQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.io.Serializable;

/**
 * 题目收藏查询请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子查询请求
     */
    private QuestionQueryRequest questionQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

       @Serial
    private static final long serialVersionUID = 1L;
}