/**
 * Copyright (C), 2020
 * FileName: DataSourceContextAop
 * Author:   HDC
 * Date:     2020/6/3 18:41
 * Description: 切面逻辑配置类
 */
package com.poyi.springboot.configure;

import com.poyi.springboot.annotation.DynamicSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 〈切面逻辑配置类〉
 *
 * @author Administrator
 * @create 2020/6/3
 * @since 1.0.0
 */
@Aspect
@Component
public class DataSourceContextAop {

    @Around("@annotation(com.poyi.springboot.annotation.DynamicSource)")
    public Object setDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable {
        boolean clear = true;
        try {
            Method method = this.getMethod(pjp);
            DynamicSource dynamicSource = method.getAnnotation(DynamicSource.class);
            clear = dynamicSource.clear();
            DataSourceContextHolder.set(dynamicSource.value());
            System.out.println("========数据源切换至：{"+dynamicSource.value()+"}");
            return pjp.proceed();
        } finally {
            if (clear) {
                DataSourceContextHolder.clear();
            }
        }
    }

    private Method getMethod(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        return signature.getMethod();
    }

}
