package com.zbn.Interview.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbn.Interview.common.BaseResponse;
import com.zbn.Interview.common.DeleteRequest;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.common.ResultUtils;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.exception.ThrowUtils;
import com.zbn.Interview.model.dto.questionbankquestion.QuestionBankQuestionAddRequest;
import com.zbn.Interview.model.dto.questionbankquestion.QuestionBankQuestionRemoveRequest;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionBank;
import com.zbn.Interview.model.entity.QuestionBankQuestion;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.service.QuestionBankQuestionService;
import com.zbn.Interview.service.QuestionBankService;
import com.zbn.Interview.service.QuestionService;
import com.zbn.Interview.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;

@RestController
@RequestMapping("/questionBank_question")
public class QuestionBankQuestionController {

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;
    @Resource
    private UserService userService;
    @Resource
    private QuestionService questionService;
    @Resource
    QuestionBankService questionBankService;

    @PostMapping("/add")
    public BaseResponse<Long> addQuestionBankQuestion(@RequestBody QuestionBankQuestionAddRequest questionBankQuestionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankQuestionAddRequest == null, ErrorCode.PARAMS_ERROR);
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        BeanUtils.copyProperties(questionBankQuestionAddRequest, questionBankQuestion);
        // 题目和题库必须存在
        Long questionBankId = questionBankQuestionAddRequest.getQuestionBankId();
        Long questionId = questionBankQuestionAddRequest.getQuestionId();
        if (questionBankId != null && questionId != null) {
            Question question = questionService.getById(questionId);
            ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
            QuestionBank questionBank = questionBankService.getById(questionBankId);
            ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR);
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        questionBankQuestion.setUserId(loginUser.getId());
        boolean result = questionBankQuestionService.save(questionBankQuestion);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Long id = questionBankQuestion.getId();
        return ResultUtils.success(id);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionBankQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        // 判断请求是否合法
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        if (deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断删除的关系是否存在
        long id = deleteRequest.getId();
        QuestionBankQuestion oldQuestionBankQuestion = questionBankQuestionService.getById(id);
        if (oldQuestionBankQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        User loginUser = userService.getLoginUser(request);
        boolean result = false;
        if (oldQuestionBankQuestion.getUserId().equals(loginUser.getId()) || userService.isAdmin(loginUser)) {
            result = questionBankQuestionService.removeById(id);
        }
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    @DeleteMapping("/remove")
    public BaseResponse<Boolean> removeQuestionBankQuestion(@RequestBody QuestionBankQuestionRemoveRequest questionBankQuestionRemoveRequest) {
        ThrowUtils.throwIf(questionBankQuestionRemoveRequest == null, ErrorCode.PARAMS_ERROR);
        Long questionBankId = questionBankQuestionRemoveRequest.getQuestionBankId();
        Long questionId = questionBankQuestionRemoveRequest.getQuestionId();
        LambdaQueryWrapper<QuestionBankQuestion> questionBankQuestionLambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class);
        questionBankQuestionLambdaQueryWrapper
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .eq(QuestionBankQuestion::getQuestionId, questionId);
        boolean remove = questionBankQuestionService.remove(questionBankQuestionLambdaQueryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);

    }
}
