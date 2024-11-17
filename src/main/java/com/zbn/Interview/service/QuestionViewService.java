package com.zbn.Interview.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbn.Interview.model.entity.QuestionView;
import com.zbn.Interview.model.entity.User;

/**
 * 题目浏览服务
 *
 * @author 朱贝宁
 * @description 针对表【question_view(题目浏览表)】的数据库操作Service
 * @createDate 2024-10-24 14:23:12
 */
public interface QuestionViewService extends IService<QuestionView> {
    /**
     * 浏览题目
     *
     * @param questionId 题目id
     * @param loginUser  登录用户
     * @return
     */
    int doQuestionView(long questionId, User loginUser);

    /**
     * 浏览题目(内部服务)
     *
     * @param questionId 题目id
     * @param userId     用户id
     * @return
     */
    int doQuestionInnerView(long questionId, long userId);
}
