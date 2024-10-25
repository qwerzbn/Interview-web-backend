package com.zbn.Interview.controller;

import com.zbn.Interview.common.BaseResponse;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.common.ResultUtils;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.model.dto.questionthumb.QuestionThumbAddRequest;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.service.QuestionThumbService;
import com.zbn.Interview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author zbn
 * @date 2024/10/23
 */
@RestController
@RequestMapping("/Question_thumb")
@Slf4j
public class QuestionThumbController {

    @Resource
    private QuestionThumbService QuestionThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param QuestionThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionThumbAddRequest QuestionThumbAddRequest,
                                         HttpServletRequest request) {
        if (QuestionThumbAddRequest == null || QuestionThumbAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long QuestionId = QuestionThumbAddRequest.getQuestionId();
        int result = QuestionThumbService.doQuestionThumb(QuestionId, loginUser);
        return ResultUtils.success(result);
    }

}
