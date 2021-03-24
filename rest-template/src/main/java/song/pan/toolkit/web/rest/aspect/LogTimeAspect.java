package song.pan.toolkit.web.rest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import song.pan.toolkit.web.rest.annotation.TimeLog;

import java.lang.reflect.Method;

/**
 * Log the time cost of all the methods with {@link TimeLog} annotated
 * @author Song Pan
 * @version 1.0.0
 */
@Aspect
@Component
@Slf4j
public class LogTimeAspect {


    @Pointcut("@annotation(song.pan.toolkit.web.rest.annotation.TimeLog)")
    public void controller() { }


    @Around("controller()")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
            log.info("[Time] {}.{} [{} ms]", method.getDeclaringClass().getName(), method.getName(), System.currentTimeMillis() - begin);
        }
    }

}
