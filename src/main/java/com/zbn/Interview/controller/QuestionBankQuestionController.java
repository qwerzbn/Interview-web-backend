package com.zbn.Interview.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbn.Interview.annotation.AuthCheck;
import com.zbn.Interview.common.BaseResponse;
import com.zbn.Interview.common.DeleteRequest;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.common.ResultUtils;
import com.zbn.Interview.constant.UserConstant;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.exception.ThrowUtils;
import com.zbn.Interview.model.dto.questionbankquestion.*;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题库题目(QuestionBankQuestion)表控制层
 *
 * @author zbn
 * @date 2024/10/24
 * @Link <a href="https://github.com/qwerzbn"></a>
 */

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
    private QuestionBankService questionBankService;

    /**
     * 添加题目到题库
     *
     * @param questionBankQuestionAddRequest 添加请求
     * @param request                        http请求
     * @return id
     */
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

    /**
     * 删除题目题库信息
     *
     * @param deleteRequest 删除请求
     * @param request       http请求
     * @return 是否成功
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
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

    /**
     * 从题库中移除题目
     *
     * @param questionBankQuestionRemoveRequest 移除请求
     * @return 是否成功
     */
    @DeleteMapping("/remove")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> removeQuestionBankQuestion(@RequestBody QuestionBankQuestionRemoveRequest questionBankQuestionRemoveRequest) {
        ThrowUtils.throwIf(questionBankQuestionRemoveRequest == null, ErrorCode.PARAMS_ERROR);
        Long questionBankId = questionBankQuestionRemoveRequest.getQuestionBankId();
        Long questionId = questionBankQuestionRemoveRequest.getQuestionId();
        LambdaQueryWrapper<QuestionBankQuestion> questionBankQuestionLambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class);
        questionBankQuestionLambdaQueryWrapper.eq(QuestionBankQuestion::getQuestionBankId, questionBankId).eq(QuestionBankQuestion::getQuestionId, questionId);
        boolean remove = questionBankQuestionService.remove(questionBankQuestionLambdaQueryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取所属题库列表
     *
     * @param questionBankQuestionQueryRequest 查询请求
     * @return 所属题库列表
     */
    @PostMapping("/list/questionBank")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<QuestionBank>> listQuestionBankByQuestion(@RequestBody QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        ThrowUtils.throwIf(questionBankQuestionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Long questionId = questionBankQuestionQueryRequest.getQuestionId();
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<QuestionBankQuestion> questionBankQuestionLambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class);
        questionBankQuestionLambdaQueryWrapper.eq(QuestionBankQuestion::getQuestionId, questionId).select(QuestionBankQuestion::getQuestionBankId);
        List<QuestionBankQuestion> questionBankQuestions = questionBankQuestionService.list(questionBankQuestionLambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(questionBankQuestions)) {
            Set<Long> questionBankIdSet = questionBankQuestions.stream().map(QuestionBankQuestion::getQuestionBankId).collect(Collectors.toSet());
            queryWrapper.in("id", questionBankIdSet);
            return ResultUtils.success(questionBankService.list(queryWrapper));
        } else {
            return ResultUtils.success(Collections.emptyList());
        }
    }

    /**
     * 批量添加题目
     *
     * @param questionBankQuestionBatchAddRequest 批量添加请求
     * @param request                             http请求
     * @return 是否成功
     */
    @PostMapping("/add/batch")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> addBatchQuestionBankQuestion(@RequestBody QuestionBankQuestionBatchAddRequest questionBankQuestionBatchAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankQuestionBatchAddRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        Long questionBankId = questionBankQuestionBatchAddRequest.getQuestionBankId();
        List<Long> questionIdList = questionBankQuestionBatchAddRequest.getQuestionId();
        User loginUser = userService.getLoginUser(request);
        questionBankQuestionService.addBatchQuestion(questionIdList, questionBankId, loginUser.getId());
        return ResultUtils.success(true);
    }

    /**
     * 批量移除题目
     *
     * @param questionBankQuestionBatchRemoveRequest 批量添加请求
     * @param request                             http请求
     * @return 是否成功
     */
    @DeleteMapping("/remove/batch")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> removeBatchQuestionBankQuestion(@RequestBody QuestionBankQuestionBatchRemoveRequest questionBankQuestionBatchRemoveRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionBankQuestionBatchRemoveRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        Long questionBankId = questionBankQuestionBatchRemoveRequest.getQuestionBankId();
        List<Long> questionIdList = questionBankQuestionBatchRemoveRequest.getQuestionId();
        User loginUser = userService.getLoginUser(request);
        questionBankQuestionService.removeBatchQuestion(questionIdList, questionBankId, loginUser.getId());
        return ResultUtils.success(true);
    }

}
