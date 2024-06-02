package nurbek.librarymanagementsystem.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {
    }

    @Pointcut("execution(* nurbek.librarymanagementsystem.controller.*.*(..))")
    public void controllerPointcut() {
    }

    @Before(value = "controllerPointcut() || executeLogging()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.debug(">> CALLED METHOD: {}() with args: {}", methodName, args);
    }

    @AfterReturning(value = "controllerPointcut() || executeLogging()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("<< METHOD RETURNED: {}() result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "controllerPointcut() || executeLogging()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("!! ERROR: {}() - {}", methodName, exception.getMessage());
    }
}