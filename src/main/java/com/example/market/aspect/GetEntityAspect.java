package com.example.market.aspect;
import com.example.market.annotation.GetEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GetEntityAspect {
    @Around("@annotation(com.example.market.annotation.GetEntity)")
    public Object logGetEntityById(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GetEntity annotation = signature.getMethod().getAnnotation(GetEntity.class);
        String entityName = annotation.value();

        Long id = (Long) joinPoint.getArgs()[0];

        log.info("GET /api/{}s/{} — fetching {}",
                entityName.toLowerCase(),
                id,
                entityName.toLowerCase());

        Object result = joinPoint.proceed();

        if (result != null) {
            log.debug("{} fetched successfully with id={}", entityName, id);
        } else {
            log.debug("{} not found with id={}", entityName, id);
        }

        return result;
    }
}
