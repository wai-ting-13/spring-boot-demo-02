package me.specter.spring_boot_demo_02.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ServiceAspect {

    @Pointcut("execution(* me.specter.spring_boot_demo_02..*Service*.*(..))")
    public void pointcutAllService() {

    }

    @Around("pointcutAllService()")
    public Object aroundService(ProceedingJoinPoint point) throws Throwable {
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] params = point.getArgs();
        Object proceed = null;
        long startTime = 0;

        try {
            // @Before
            log.info("%s.%s begin".formatted(className, methodName)); 
            startTime = System.currentTimeMillis();
            // Invole Target Method
            proceed = point.proceed(params);
        } catch (Throwable e) {
            // @AfterThrowing
            log.info("%s.%s throws exception(s)".formatted(className, methodName)); 
            log.info("Exception Message: " + e.getMessage());
            log.info("Exception Cause: " + e.getCause());
            throw e;
        } finally {
            // @After
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("%s.%s end, consumed %d ms.".formatted(className, methodName, elapsedTime)); 
        }
        // @AfterReturning
        return proceed;
    }

}