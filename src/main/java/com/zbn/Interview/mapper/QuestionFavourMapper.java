package com.zbn.Interview.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionFavour;
import org.apache.ibatis.annotations.Param;

/**
 * @author 朱贝宁
 * @description 针对表【question_favour(题目收藏表)】的数据库操作Mapper
 * @createDate 2024-10-24 14:23:12
 * @Entity generator.domain.QuestionFavour
 */
public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {
    /**
     * 分页查询收藏题目列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listQuestionPostByPage(IPage<Question> page,
                                          @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper,
                                          long favourUserId);
}




