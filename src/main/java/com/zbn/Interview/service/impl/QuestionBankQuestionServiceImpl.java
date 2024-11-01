package com.zbn.Interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.exception.ThrowUtils;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionBank;
import com.zbn.Interview.model.entity.QuestionBankQuestion;
import com.zbn.Interview.service.QuestionBankQuestionService;
import com.zbn.Interview.mapper.QuestionBankQuestionMapper;
import com.zbn.Interview.service.QuestionBankService;
import com.zbn.Interview.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 朱贝宁
 * @description 针对表【question_bank_question(题库题目)】的数据库操作Service实现
 * @createDate 2024-10-25 09:16:32
 */
@Service
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion>
        implements QuestionBankQuestionService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionBankService questionBankService;

    /**
     * 批量添加题目
     *
     * @param questionIds    题目id列表
     * @param questionBankId 题库id
     * @param userId         用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatchQuestion(List<Long> questionIds, Long questionBankId, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(CollectionUtils.isEmpty(questionIds), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null, ErrorCode.PARAMS_ERROR, "题库id为空");
        ThrowUtils.throwIf(userId == null, ErrorCode.PARAMS_ERROR);
        // 题目是否存在
        List<Question> questions = questionService.listByIds(questionIds);
        ThrowUtils.throwIf(CollectionUtils.isEmpty(questions), ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 获取合法题目id
        List<Long> validQuestionIds = questions.stream().map(Question::getId).toList();
        // 题库是否存在
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        // 批量添加
        for (Long questionId : validQuestionIds) {
            QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
            questionBankQuestion.setQuestionBankId(questionBankId);
            questionBankQuestion.setQuestionId(questionId);
            questionBankQuestion.setUserId(userId);
            boolean result = this.save(questionBankQuestion);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加失败");
        }
    }

    /**
     * 批量移出题目
     *
     * @param questionIds    题目id列表
     * @param questionBankId 题库id
     * @param userId         用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchQuestion(List<Long> questionIds, Long questionBankId, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(CollectionUtils.isEmpty(questionIds), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null, ErrorCode.PARAMS_ERROR, "题库id为空");
        for (Long questionId : questionIds) {
            LambdaQueryWrapper<QuestionBankQuestion> removeQuery = Wrappers.lambdaQuery(QuestionBankQuestion.class).eq(QuestionBankQuestion::getQuestionId, questionId)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
            boolean result = this.remove(removeQuery);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "移除失败");
        }
    }
}




