package com.zbn.Interview.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbn.Interview.model.entity.QuestionThumb;
import com.zbn.Interview.model.entity.User;


/**
 * 题目点赞服务
 *
 * @author 朱贝宁
 * @description 针对表【question_thumb(题目点赞表)】的数据库操作Service
 * @createDate 2024-10-24 14:23:12
 */
public interface QuestionThumbService extends IService<QuestionThumb> {
    /**
     * 题目点赞
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionThumb(long questionId, User loginUser);
    /**
     * 题目点赞(内部服务)
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doInnerQuestionThumb(long questionId, User loginUser);

}
