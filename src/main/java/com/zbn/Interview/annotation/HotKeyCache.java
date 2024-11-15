package com.zbn.Interview.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 热键缓存
 *
 * @author zbn
 * @date 2024/10/23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HotKeyCache {
    String value() default "";
    String param() default "";

}

