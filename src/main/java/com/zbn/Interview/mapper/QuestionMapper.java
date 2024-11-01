package com.zbn.Interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbn.Interview.model.entity.Question;

import java.util.Date;
import java.util.List;

/**
 * @author 朱贝宁
 * @description 针对表【question(题目)】的数据库操作Mapper
 * @createDate 2024-10-24 13:28:44
 * @Entity com.zbn.Interview.model.entity.Question
 */
public interface QuestionMapper extends BaseMapper<Question> {
    /**
     * 查询题目列表（包括已被删除的数据）
     */
    List<Question> listQuestionWithDelete(Date minUpdateTime);

}




