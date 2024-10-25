package com.zbn.Interview.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionFavour;
import com.zbn.Interview.model.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * 题目收藏服务
 *
 * @author 朱贝宁
 * @description 针对表【question_favour(题目收藏表)】的数据库操作Service
 * @createDate 2024-10-24 14:23:12
 */
public interface QuestionFavourService extends IService<QuestionFavour> {
    /**
     * 题目收藏
     *
     * @param questionId 题目id
     * @param loginUser  登录用户
     */
    int doQuestionFavour(long questionId, User loginUser);

    /**
     * 分页获取用户收藏的题目列表
     *
     * @param page         分页对象
     * @param queryWrapper 查询条件
     * @param favourUserId 收藏用户id
     * @return 题目列表
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper, long favourUserId);

    /**
     * 题目收藏(内部服务)
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionFavourInner(long userId, long questionId);
}
