package ru.gb.timesheet.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class RecoverAspect {
    
    @Around("@annotation(recover)")
    public Object recoverMethod(ProceedingJoinPoint joinPoint, Recover recover) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> returnType = method.getReturnType();

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            // Логируем исключение
            log.info("Recovering {} after Exception[{}, \"{}\"]", 
                         joinPoint.getSignature(), 
                         throwable.getClass().getSimpleName(), 
                         throwable.getMessage());

            // Проверяем, нужно ли отлавливать это исключение
            for (Class<?> exceptionClass : recover.noRecoverFor()) {
                if (exceptionClass.isAssignableFrom(throwable.getClass())) {
                    throw throwable; // Не обрабатываем это исключение
                }
            }

            // Возвращаем значение по умолчанию
            return getDefaultValue(returnType);
        }
    }

    private Object getDefaultValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) return false;
            if (returnType == byte.class) return (byte) 0;
            if (returnType == short.class) return (short) 0;
            if (returnType == int.class) return 0;
            if (returnType == long.class) return 0L;
            if (returnType == float.class) return 0.0f;
            if (returnType == double.class) return 0.0d;
        }
        return null; // Для ссылочных типов
    }
}
