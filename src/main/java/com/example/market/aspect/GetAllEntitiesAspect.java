package com.example.market.aspect;

import com.example.market.annotation.GetAllEntities;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GetAllEntitiesAspect {

    @Around("@annotation(com.example.market.annotation.GetAllEntities)")
    public Object logGetAllEntities(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GetAllEntities annotation = signature.getMethod().getAnnotation(GetAllEntities.class);
        String entitiesName = annotation.value();

        log.info("GET /api/{}s — fetching all {}s",
                entitiesName.toLowerCase(),
                entitiesName.toLowerCase());

        return joinPoint.proceed();
    }
}