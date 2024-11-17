package com.zbn.Interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.mapper.QuestionFavourMapper;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionFavour;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.service.QuestionFavourService;
import com.zbn.Interview.service.QuestionService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 题目收藏服务实现
 *
 * @author 朱贝宁
 * @description 针对表【question_favour(题目收藏表)】的数据库操作Service实现
 * @createDate 2024-10-24 14:23:12
 */
@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour> implements QuestionFavourService {
    @Resource
    QuestionService questionService;

    @Override
    public int doQuestionFavour(long questionId, User loginUser) {
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        QuestionFavourService questionFavourService = (QuestionFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(loginUser.getId()).intern()) {
            return questionFavourService.doQuestionFavourInner(loginUser.getId(), questionId);
        }
    }

    @Override
    public Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper, long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listQuestionPostByPage(page, queryWrapper, favourUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionFavourInner(long userId, long questionId) {
        QuestionFavour questionFavour = new QuestionFavour();
        questionFavour.setUserId(userId);
        questionFavour.setQuestionId(questionId);
        QueryWrapper<QuestionFavour> queryWrapper = new QueryWrapper<>(questionFavour);
        QuestionFavour oldQuestionFavour = this.getOne(queryWrapper);
        boolean result;
        if (oldQuestionFavour == null) {
            // 题目未收藏
            result = this.save(questionFavour);
            if (result) {
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("favourNum = favourNum + 1").update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "收藏失败");
            }
        } else {
            // 题目已收藏
            result = this.remove(queryWrapper);
            if (result) {
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "取消收藏失败");
            }

        }
    }
}




