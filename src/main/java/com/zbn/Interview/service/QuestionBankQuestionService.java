package com.zbn.Interview.service;

import com.zbn.Interview.model.entity.QuestionBankQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 朱贝宁
* @description 针对表【question_bank_question(题库题目)】的数据库操作Service
* @createDate 2024-10-25 09:16:32
*/
public interface QuestionBankQuestionService extends IService<QuestionBankQuestion> {
    void addBatchQuestion(List<Long> questionIds, Long questionBankId, Long userId);
    void removeBatchQuestion(List<Long> questionIds, Long questionBankId, Long userId);
    @Transactional(rollbackFor = Exception.class)
    void batchAddQuestionsToBankInner(List<QuestionBankQuestion> questionBankQuestions);
}
