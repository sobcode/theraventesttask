package com.app.theraventesttask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method entry, exit, and exceptions thrown by methods within the application.
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut to select methods within the application excluding those in the ControllerExceptionHandler class
     * and those annotated with @ExceptionHandler.
     */
    @Pointcut("execution(* com.app.theraventesttask..*.*(..)) " +
            "&& !execution(* com.app.theraventesttask..ControllerExceptionHandler.*(..)) " +
            "&& !@annotation(org.springframework.web.bind.annotation.ExceptionHandler)" +
            "&& !within(com.app.theraventesttask.filter..*)")
    public void methodsToBeLogged() {}

    @Pointcut("execution(* com.app.theraventesttask..*.*(..)) " +
            "&& !within(com.app.theraventesttask.filter..*)")
    public void afterThrowingToBeLogged() {}

    /**
     * Advice to log method entry and method exit.
     *
     * @param pjp ProceedingJoinPoint representing the method being intercepted.
     * @return The object returned by the intercepted method.
     * @throws Throwable If the intercepted method throws an exception.
     */
    @Around("methodsToBeLogged()")
    public Object aroundEveryMethod(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        log.info(String.format("LOG: %s.%s start", className, methodName));

        Object object = pjp.proceed();

        log.info(String.format("LOG: %s.%s end", className, methodName));

        return object;
    }

    /**
     * Advice to log exceptions thrown by methods within the application.
     *
     * @param jp        JoinPoint representing the method being intercepted.
     * @param exception Exception thrown by the intercepted method.
     */
    @AfterThrowing(pointcut = "afterThrowingToBeLogged()", throwing = "exception")
    public void logAfterThrowing(JoinPoint jp, Exception exception) {
        String className = jp.getTarget().getClass().getSimpleName();
        String methodName = jp.getSignature().getName();
        log.warn(String.format("LOG: handling exception in method %s.%s: %s", className, methodName, exception));
    }
}
