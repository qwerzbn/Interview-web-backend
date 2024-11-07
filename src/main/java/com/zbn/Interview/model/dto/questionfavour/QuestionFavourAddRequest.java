package com.zbn.Interview.model.dto.questionfavour;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

/**
 * 题目收藏 / 取消收藏请求
 *
 * @author zbn
 * @date 2024/10/23
 */
@Data
public class QuestionFavourAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long questionId;

       @Serial
    private static final long serialVersionUID = 1L;
}