package com.zbn.Interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.rholder.retry.*;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.exception.ThrowUtils;
import com.zbn.Interview.mapper.QuestionBankQuestionMapper;
import com.zbn.Interview.model.entity.Question;
import com.zbn.Interview.model.entity.QuestionBank;
import com.zbn.Interview.model.entity.QuestionBankQuestion;
import com.zbn.Interview.service.QuestionBankQuestionService;
import com.zbn.Interview.service.QuestionBankService;
import com.zbn.Interview.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author 朱贝宁
 * @description 针对表【question_bank_question(题库题目)】的数据库操作Service实现
 * @createDate 2024-10-25 09:16:32
 */
@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion>
        implements QuestionBankQuestionService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionBankService questionBankService;
    public static final int BATCH_SIZE = 1000;

    /**
     * 批量添加题目
     *
     * @param questionIds    题目id列表
     * @param questionBankId 题库id
     * @param userId         用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatchQuestion(List<Long> questionIds, Long questionBankId, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(CollectionUtils.isEmpty(questionIds), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null, ErrorCode.PARAMS_ERROR, "题库id为空");
        ThrowUtils.throwIf(userId == null, ErrorCode.PARAMS_ERROR);
        // 题目是否存在（ 用LambdaQueryWrapper来选择某一列，防止使用select *而导致性能的损耗）
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = Wrappers.lambdaQuery(Question.class)
                .select(Question::getId)
                .in(Question::getId, questionIds);
        // 用MayBaits plus 提供的listObjs方法直接将查到的数据转换成Long类型而不是Question对象，节省了空间的损耗
        List<Long> validQuestionIds = questionService.listObjs(questionLambdaQueryWrapper, obj -> (Long) obj);
        ThrowUtils.throwIf(CollectionUtils.isEmpty(validQuestionIds), ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 检查题目是否已经存在（过滤已经存在的题目，防止重复插入而导致无用的性能开销）
        LambdaQueryWrapper<QuestionBankQuestion> query = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .in(QuestionBankQuestion::getQuestionId, validQuestionIds);
        // 获取已存在的题目列表
        List<Long> existQuestionIds = this.listObjs(query, obj -> (Long) obj);
        // 过滤已存在的题目id，得到新的合法题目id
        validQuestionIds = validQuestionIds.stream().filter(questionId -> !existQuestionIds.contains(questionId)).toList();
        // 题库是否存在
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        // 自定义线程池(IO密集型任务)
        ThreadPoolExecutor customExecutor = new ThreadPoolExecutor(
                20,              // 核心线程数（正常情况下有20个线程在工作）
                50,                        // 最大线程数（如果请求过多可开辟更多线程）
                60L,                       // 线程空闲存活时间（一分钟后自动释放，防止占领资源）
                TimeUnit.SECONDS,          // 存活时间单位
                new LinkedBlockingQueue<>(1000),  // 阻塞队列容量
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：由调用线程处理任务
        );
        // 保存所有批次的任务
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        // 分批处理避免长事务，假设每次处理 1000 条数据
        int totalQuestionListSize = validQuestionIds.size();
        for (int i = 0; i < totalQuestionListSize; i += BATCH_SIZE) {
            // 生成每批次的数据
            List<Long> subList = validQuestionIds.subList(i, Math.min(i + BATCH_SIZE, totalQuestionListSize));
            List<QuestionBankQuestion> questionBankQuestions = subList.stream().map(questionId -> {
                QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                questionBankQuestion.setQuestionBankId(questionBankId);
                questionBankQuestion.setQuestionId(questionId);
                questionBankQuestion.setUserId(userId);
                return questionBankQuestion;
            }).collect(Collectors.toList());
            // 使用重试机制提升系统健壮性
            Callable<Void> callable = () -> {
                // 使用事务处理每批数据
                QuestionBankQuestionService questionBankQuestionService = (QuestionBankQuestionServiceImpl) AopContext.currentProxy();
                // 自定义线程池执行,异步处理数据，捕获异常，防止中断其他线程的操作给系统带来不确定性影响
                CompletableFuture<Void> future = CompletableFuture
                        .runAsync(() -> questionBankQuestionService.batchAddQuestionsToBankInner(questionBankQuestions)
                                , customExecutor).exceptionally(ex -> {
                            log.error("批处理任务执行失败", ex);
                            return null;
                        });

                futures.add(future);
                return null;
            };
            // 定义重试器
            Retryer<Void> retryer = RetryerBuilder.<Void>newBuilder()
                    .retryIfExceptionOfType(IOException.class) // 发生IO异常则重试
                    .retryIfRuntimeException() // 发生运行时异常则重试
                    .withWaitStrategy(WaitStrategies.incrementingWait(10,
                            TimeUnit.SECONDS, 10, TimeUnit.SECONDS)) // 等待(初始等待时间，时间单位，下一次递增时间，时间单位)
                    .withStopStrategy(StopStrategies.stopAfterAttempt(4)) // 允许执行4次（首次执行 + 最多重试3次）
                    .build();
            try {
                retryer.call(callable); // 执行
            } catch (RetryException | ExecutionException e) { // 重试次数超过阈值或被强制中断
                e.printStackTrace();
            }
            // 等待所有批次完成操作
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            // 关闭线程池
            customExecutor.shutdown();

        }
    }

    /**
     * 批量添加题目到题库(内部操作)
     *
     * @param questionBankQuestions 题目题库关系列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddQuestionsToBankInner(List<QuestionBankQuestion> questionBankQuestions) {
        try {
            // 用MayBaits plus 提供的批处理方法，减少与数据库的交互，大大提升了性能
            boolean result = this.saveBatch(questionBankQuestions);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "向题库添加题目失败");
        } catch (DataIntegrityViolationException e) {
            log.error("数据库唯一键冲突或违反其他完整性约束, 错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已存在于该题库，无法重复添加");
        } catch (DataAccessException e) {
            log.error("数据库连接问题、事务问题等导致操作失败, 错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据库操作失败");
        } catch (Exception e) {
            // 捕获其他异常，做通用处理
            log.error("添加题目到题库时发生未知错误，错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "向题库添加题目失败");
        }
    }


    /**
     * 批量移出题目
     *
     * @param questionIds    题目id列表
     * @param questionBankId 题库id
     * @param userId         用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchQuestion(List<Long> questionIds, Long questionBankId, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(CollectionUtils.isEmpty(questionIds), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null, ErrorCode.PARAMS_ERROR, "题库id为空");
        for (Long questionId : questionIds) {
            LambdaQueryWrapper<QuestionBankQuestion> removeQuery = Wrappers.lambdaQuery(QuestionBankQuestion.class).eq(QuestionBankQuestion::getQuestionId, questionId)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId);
            boolean result = this.remove(removeQuery);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "移除失败");
        }
    }
}




