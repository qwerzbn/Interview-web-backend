package com.zbn.Interview.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbn.Interview.model.dto.question.QuestionQueryRequest;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题目服务
 *
 * @author zbn
 * @date 2024/10/24
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验数据
     *
     * @param question 题目
     * @param add      对创建的数据进行校验
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 题目查询请求
     * @return 查询条件
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question 题目
     * @param request http请求  请求
     * @return 题目封装
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage 题目分页
     * @param request http请求      请求
     * @return 题目封装分页
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 分页获取题目
     *
     * @param questionQueryRequest 题目查询请求
     * @param request http请求              请求
     * @return 题目分页
     */
    Page<QuestionVO> listQuestionVOByPage(QuestionQueryRequest questionQueryRequest, HttpServletRequest request);

    /**
     * 分页获取题目（仅管理员可用）
     *
     * @param questionQueryRequest 题目查询请求
     * @return 题目分页
     */
    Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 从 ES 查询题目
     *
     * @param questionQueryRequest 题目查询请求
     * @return 题目分页
     */
    Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest);
}
