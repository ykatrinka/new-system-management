package ru.clevertec.commentsservice.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.clevertec.commentsservice.util.LoggingMessage;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;

/**
 * @author Katerina
 * @version 1.0.0
 * Аспект для логирования.
 */
@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    /**
     * Сервисные методы.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void anyServices() {
    }

    /**
     * Методы контроллеров.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void anyControllers() {
    }

    /**
     * До начала работы метода контроллера.
     *
     * @param joinPoint данные метода.
     */
    @Before("anyControllers()")
    public void beforeControllerMethods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug(LoggingMessage.LOG_MESSAGE_BEFORE,
                method.getName(),
                args);
    }

    /**
     * После окончания работы метода контроллера.
     *
     * @param joinPoint данные метода.
     * @param result Результат метода.
     */
    @AfterReturning(pointcut = "anyControllers()", returning = "result")
    public void afterControllerMethods(JoinPoint joinPoint, Object result) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug(LoggingMessage.LOG_MESSAGE_AFTER_RETURNING,
                method.getName(),
                ofNullable(result)
                        .orElse(LoggingMessage.EMPTY_STRING));
    }

    /**
     * При выбросе исключения во время работы метода контроллера.
     *
     * @param joinPoint данные метода.
     * @param exception Выброшенное исключение.
     */
    @AfterThrowing(pointcut = "anyServices()", throwing = "exception")
    private void afterThrowingAnyServicesMethodsLoggingAdvice(JoinPoint joinPoint, Throwable exception) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug(LoggingMessage.LOG_MESSAGE_AFTER_THROWING,
                method.getName(),
                exception.getMessage());
    }
}
