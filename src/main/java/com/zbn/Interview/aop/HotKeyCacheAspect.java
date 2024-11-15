package com.zbn.Interview.aop;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.zbn.Interview.annotation.HotKeyCache;
import com.zbn.Interview.common.ErrorCode;
import com.zbn.Interview.common.ResultUtils;
import com.zbn.Interview.exception.BusinessException;
import com.zbn.Interview.model.dto.question.QuestionQueryRequest;
import com.zbn.Interview.model.dto.questionBank.QuestionBankQueryRequest;
import com.zbn.Interview.model.entity.QuestionBank;
import com.zbn.Interview.model.vo.QuestionBankVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class HotKeyCacheAspect {

    @Around("@annotation(hotKeyCache)")
    public Object around(ProceedingJoinPoint joinPoint, HotKeyCache hotKeyCache) throws Throwable {
        String param = hotKeyCache.param();
        String key = hotKeyCache.value();
        if (param == null || param.isEmpty()) {

        }else {
            Object paramValue = getParamValue(joinPoint, param);
            QuestionBankQueryRequest questionBankQueryRequest = (QuestionBankQueryRequest) paramValue;
            if (questionBankQueryRequest == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            key = hotKeyCache.value() + questionBankQueryRequest.getId().toString();
        }
        log.info("key:{}", key);
        if (JdHotKeyStore.isHotKey(key)) {
            log.info("是热key");
            //缓存中获取
            Object cachedQuestionBankVO = JdHotKeyStore.get(key);
            if (cachedQuestionBankVO != null) {
                return ResultUtils.success((QuestionBankVO) cachedQuestionBankVO);
            }
        }

        //执行方法
        log.info("放行");
        return joinPoint.proceed();
    }

    private Object getParamValue(ProceedingJoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (paramName.equals(parameterNames[i])) {
                return args[i];
            }
        }
        return null;
    }
}
