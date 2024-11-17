package com.zbn.Interview.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbn.Interview.common.BaseResponse;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.common.ResultUtils;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.exception.ThrowUtils;
import com.zbn.Interview.model.dto.question.QuestionQueryRequest;
import com.zbn.Interview.model.dto.questionfavour.QuestionFavourAddRequest;
import com.zbn.Interview.model.dto.questionfavour.QuestionFavourQueryRequest;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.User;
import com.zbn.Interview.model.vo.QuestionVO;
import com.zbn.Interview.service.QuestionFavourService;
import com.zbn.Interview.service.QuestionService;
import com.zbn.Interview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目收藏接口
 *
 * @author zbn
 * @date 2024/10/23
 */
@RestController
@RequestMapping("/question_favour")
@Slf4j
public class QuestionFavourController {

    @Resource
    private QuestionFavourService QuestionFavourService;

    @Resource
    private QuestionService QuestionService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param QuestionFavourAddRequest
     * @param request http请求
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doQuestionFavour(@RequestBody QuestionFavourAddRequest QuestionFavourAddRequest,
                                                  HttpServletRequest request) {
        if (QuestionFavourAddRequest == null || QuestionFavourAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long QuestionId = QuestionFavourAddRequest.getQuestionId();
        int result = QuestionFavourService.doQuestionFavour(QuestionId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param QuestionQueryRequest
     * @param request http请求
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionByPage(@RequestBody QuestionQueryRequest QuestionQueryRequest,
                                                                     HttpServletRequest request) {
        if (QuestionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = QuestionQueryRequest.getCurrent();
        long size = QuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> QuestionPage = QuestionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                QuestionService.getQueryWrapper(QuestionQueryRequest), loginUser.getId());
        return ResultUtils.success(QuestionService.getQuestionVOPage(QuestionPage, request));
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param QuestionFavourQueryRequest
     * @param request http请求
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionVO>> listFavourQuestionByPage(@RequestBody QuestionFavourQueryRequest QuestionFavourQueryRequest,
                                                                   HttpServletRequest request) {
        if (QuestionFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = QuestionFavourQueryRequest.getCurrent();
        long size = QuestionFavourQueryRequest.getPageSize();
        Long userId = QuestionFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Question> QuestionPage = QuestionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                QuestionService.getQueryWrapper(QuestionFavourQueryRequest.getQuestionQueryRequest()), userId);
        return ResultUtils.success(QuestionService.getQuestionVOPage(QuestionPage, request));
    }
}
