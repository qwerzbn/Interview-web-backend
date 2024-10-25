package com.zbn.Interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.mapper.QuestionViewMapper;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionView;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.service.QuestionService;
import com.zbn.Interview.service.QuestionViewService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 题目浏览服务实现
 *
 * @author 朱贝宁
 * @description 针对表【question_view(题目浏览表)】的数据库操作Service实现
 * @createDate 2024-10-24 14:23:12
 */
@Service
public class QuestionViewServiceImpl extends ServiceImpl<QuestionViewMapper, QuestionView>
        implements QuestionViewService {
    @Resource
    private QuestionService questionService;

    /**
     * 浏览题目
     *
     * @param questionId 题目id
     * @param loginUser  登录用户
     * @return
     */
    @Override
    public int doQuestionView(long questionId, User loginUser) {
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        QuestionViewService questionViewService = (QuestionViewService) AopContext.currentProxy();
        synchronized (String.valueOf(loginUser.getId()).intern()) {
            return questionViewService.doQuestionInnerView(questionId, loginUser.getId());
        }
    }

    /**
     * 浏览题目(内部服务)
     *
     * @param questionId 题目id
     * @param userId     用户id
     * @return
     */
    @Override
    public int doQuestionInnerView(long questionId, long userId) {
        QuestionView questionView = new QuestionView();
        questionView.setQuestionId(questionId);
        questionView.setUserId(userId);
        QueryWrapper<QuestionView> queryWrapper = new QueryWrapper<>(questionView);
        QuestionView oldQuestion = this.getOne(queryWrapper);
        if (oldQuestion == null) {
            return this.save(questionView) ? 1 : 0;
        } else {
            return 0;
        }
    }
}




