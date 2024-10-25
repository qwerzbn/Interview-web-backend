package com.zbn.Interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.mapper.QuestionThumbMapper;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionThumb;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.service.QuestionService;
import com.zbn.Interview.service.QuestionThumbService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 题目点赞服务实现
 *
 * @author 朱贝宁
 * @description 针对表【question_thumb(题目点赞表)】的数据库操作Service实现
 * @createDate 2024-10-24 14:23:12
 */
@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb>
        implements QuestionThumbService {
    @Resource
    private QuestionService questionService;

    @Override
    public int doQuestionThumb(long questionId, User loginUser) {
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该题目不存在");
        }
        QuestionThumbService questionThumbService = (QuestionThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(loginUser.getId()).intern()) {
            return questionThumbService.doInnerQuestionThumb(questionId, loginUser);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doInnerQuestionThumb(long questionId, User loginUser) {
        QuestionThumb questionThumb = new QuestionThumb();
        questionThumb.setQuestionId(questionId);
        questionThumb.setUserId(loginUser.getId());
        QueryWrapper<QuestionThumb> queryWrapper = new QueryWrapper<>(questionThumb);
        QuestionThumb oldQuestionThumb = this.getOne(queryWrapper);
        boolean result;
        if (oldQuestionThumb == null) {
            result = this.save(questionThumb);
            if (result) {
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumbNum = thumbNum + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "点赞失败");
            }
        } else {
            result = this.remove(queryWrapper);
            if (result) {
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1").update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "取消点赞失败");
            }
        }
    }
}




