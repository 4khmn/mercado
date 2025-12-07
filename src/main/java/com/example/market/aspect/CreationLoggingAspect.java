package com.example.market.aspect;

import com.example.market.annotation.CreatedEntity;
import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.create.ShopCreateDto;
import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.dto.response.ShopResponseDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.model.Order;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component
public class CreationLoggingAspect {


    @Pointcut("@annotation(com.example.market.annotation.CreatedEntity)")
    public void annotatedWithCreatedEntity() {
    }


    @Around("@annotation(com.example.market.annotation.CreatedEntity)")
    public Object logCreatedEntity(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CreatedEntity annotation = signature.getMethod().getAnnotation(CreatedEntity.class);
        String entityName = annotation.value();

        Object createDto = joinPoint.getArgs()[0];

        String name = extractName(createDto);

        if (createDto instanceof OrderCreateDto) {
            log.info("POST /api/{}s — creating new {} for user with id={}",
                    entityName.toLowerCase(),
                    entityName.toLowerCase(),
                    name);
        }
        else if (createDto instanceof ProductCreateDto) {
            log.info("POST /api/{}s — creating new {} with title={}",
                    entityName.toLowerCase(),
                    entityName.toLowerCase(),
                    name);
        }
        else {
            log.info("POST /api/{}s — creating new {} with name={}",
                    entityName.toLowerCase(),
                    entityName.toLowerCase(),
                    name);
        }
        Object result = joinPoint.proceed();

        String id = extractId(result);
        log.info("{} created successfully with id={}", entityName, id);

        return result;
    }

    private String extractName(Object dto) {
        if (dto instanceof UserCreateDto) {
            return ((UserCreateDto) dto).getName();
        }
        else if (dto instanceof ShopCreateDto) {
            return ((ShopCreateDto) dto).getName();
        }
        else if (dto instanceof ProductCreateDto) {
            return ((ProductCreateDto) dto).getTitle();
        }
        else if (dto instanceof OrderCreateDto) {
            return String.valueOf(((OrderCreateDto) dto).getUserId());
        }
        return "unknown";
    }

    private String extractId(Object result) {
        if (result instanceof UserResponseDto) {
            return String.valueOf(((UserResponseDto) result).getId());
        }
        else if (result instanceof ShopResponseDto) {
            return String.valueOf(((ShopResponseDto) result).getId());
        }
        else if (result instanceof ProductResponseDto) {
            return String.valueOf(((ProductResponseDto) result).getId());
        }
        else if (result instanceof OrderResponseDto) {
            return  String.valueOf(((OrderResponseDto) result).getId());
        }
        return "unknown";
    }
}
