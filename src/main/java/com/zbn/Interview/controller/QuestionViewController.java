package com.zbn.Interview.controller;

import com.zbn.Interview.common.BaseResponse;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.common.ResultUtils;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.model.questionthumb.QuestionViewAddRequest;
import com.zbn.Interview.service.QuestionViewService;
import com.zbn.Interview.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question_view")
public class QuestionViewController {
    @Resource
    private QuestionViewService questionViewService;
    @Resource
    private UserService userService;

    @PostMapping("/")
    public BaseResponse<Integer> doView(@RequestBody QuestionViewAddRequest questionViewAddRequest,
                                        HttpServletRequest request) {
        if (questionViewAddRequest == null || questionViewAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long QuestionId = questionViewAddRequest.getQuestionId();
        int result = questionViewService.doQuestionView(QuestionId, loginUser);
        return ResultUtils.success(result);
    }
}
