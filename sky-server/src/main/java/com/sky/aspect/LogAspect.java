package com.sky.aspect;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 自定义切面，实现记录日志的功能
 * 用于在方法开始前输出方法名、@ApiOperation 注解的描述信息及传入参数
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut(" @annotation(io.swagger.annotations.ApiOperation)")
    public void apiOperationPointCut() {}

    @Before("apiOperationPointCut()")
    public void logBefore(JoinPoint joinPoint) {
        //获取被拦截的方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取接口描述信息
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        String discription = apiOperation.value();

        //获取方法参数
        Object[] args = joinPoint.getArgs();
        String result = args != null && args.length > 0 ? Arrays.toString(args) : "无参数";

        //输出日志
        log.info("{} : {} ", discription, args);
    }


}
