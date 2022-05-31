package com.orbirpinar.exchange.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;


@Aspect
@Component
@Slf4j
public class LogAspect {

    
    private final ObjectMapper mapper;
    private static final String CONTROLLER_POINTCUT = "within(com.orbirpinar.exchange.controller.*)";

    public LogAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Around(CONTROLLER_POINTCUT)
    @SneakyThrows
    public Object logAround(ProceedingJoinPoint joinPoint) {
        log.info("before {}",getLogMessage(joinPoint));
        Object proceed = joinPoint.proceed();
        log.info("after {} : with result: {}",getLogMessage(joinPoint),proceed.toString());
        return proceed;
    }
    @AfterThrowing(pointcut = CONTROLLER_POINTCUT, throwing = "e")
    @SneakyThrows
    public void logAfterException(JoinPoint joinPoint, Exception e) {
        log.error("Exception during: {} with ex: {}", getLogMessage(joinPoint),  e.toString());
    }

    private String  getLogMessage(JoinPoint joinPoint) throws JsonProcessingException {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(String::valueOf).collect(Collectors.joining(",", "[", "]"));

        String methodName = joinPoint.getSignature().getName();
        return "@" + methodName + ":" + args;
    }
}
