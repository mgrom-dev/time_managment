package ru.gb.aspect.logging;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class RecoverAspect {

    private final RecoverProperties properties;

    @Pointcut("@annotation(ru.gb.aspect.logging.Recover)") // method
    public void recoverMethodsPointcut() {
    }

    @Pointcut("@within(ru.gb.aspect.logging.Recover)") // class
    public void recoverTypePointcut() {
    }

    @Around(value = "recoverMethodsPointcut() || recoverTypePointcut()")
    public Object recoverMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> returnType = method.getReturnType();

        // Получение аннотации Recover
        Recover recover = method.getAnnotation(Recover.class);
        if (recover == null) {
            // Если метод не аннотирован, пробуем получить аннотацию из класса
            recover = joinPoint.getTarget().getClass().getAnnotation(Recover.class);
        }

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            // Логируем исключение
            log.atLevel(properties.getLevel()).log("Recovering {} after Exception[{}, \"{}\"]",
                    joinPoint.getSignature(),
                    throwable.getClass().getName(),
                    throwable.getMessage());

            // Проверяем, нужно ли отлавливать это исключение, исходя из аннотации @Recover
            if (recover != null) {
                for (Class<?> exceptionClass : recover.noRecoverFor()) {
                    if (exceptionClass.isAssignableFrom(throwable.getClass())) {
                        throw throwable; // Не обрабатываем это исключение
                    }
                }
            }

            // Проверяем, нужно ли отлавливать это исключение из файла настроек properties
            if (properties.getNoRecoverFor().contains(throwable.getClass().getName())) {
                throw throwable;
            }

            // Возвращаем значение по умолчанию
            return getDefaultValue(returnType);
        }
    }

    private Object getDefaultValue(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class)
                return false;
            if (returnType == byte.class)
                return (byte) 0;
            if (returnType == short.class)
                return (short) 0;
            if (returnType == int.class)
                return 0;
            if (returnType == long.class)
                return 0L;
            if (returnType == float.class)
                return 0.0f;
            if (returnType == double.class)
                return 0.0d;
        }
        return null; // Для ссылочных типов
    }
}
