package song.pan.toolkit.web.rest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import song.pan.toolkit.web.rest.annotation.TimeLog;
import song.pan.toolkit.web.rest.annotation.Trace;
import song.pan.toolkit.web.rest.common.CacheHolder;
import song.pan.toolkit.web.rest.common.ThreadCache;
import song.pan.toolkit.web.rest.common.util.MapperUtils;
import song.pan.toolkit.web.rest.domain.TracedMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Trace the methods with annotation {@link Trace}
 * @author Song Pan
 * @version 1.0.0
 */
@Aspect
@Component
@Slf4j
public class TraceAspect {


    @Pointcut("@annotation(song.pan.toolkit.web.rest.annotation.Trace)")
    public void trace() { }


    @Around("trace()")
    public Object traceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
        String methodFullName = method.getDeclaringClass().getName() + "." + method.getName();

        // generate id if current method is the first one
        String id = ThreadCache.TASK_ID.get();
        if (id == null) {
            return joinPoint.proceed();
        }

        List<TracedMethod> methods = (List<TracedMethod>) CacheHolder.CACHE.get(id, () -> new LinkedList<>());
        TracedMethod tracedMethod = new TracedMethod();
        tracedMethod.setMethod(methodFullName);

        // arguments
        Parameter[] params = method.getParameters();
        Object[] paramValues = joinPoint.getArgs();
        LinkedList<TracedMethod.Argument> args = new LinkedList<>();
        for (int i = 0; i < params.length; i++) {
            TracedMethod.Argument arg = new TracedMethod.Argument();
            arg.setName(params[i].getName());
            arg.setCls(params[i].getType().getName());
            if (paramValues[i] != null) {
                if (MapperUtils.isSerializable(paramValues[i])) {
                    arg.setValue(paramValues[i]);
                } else {
                    arg.setValue(paramValues[i].toString());
                }
            }
            args.add(arg);
        }
        tracedMethod.setArgs(args);

        long begin = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            tracedMethod.setReturns(result);
            tracedMethod.setSucc(Boolean.TRUE);
            return result;
        } catch (Throwable e) {
            tracedMethod.setSucc(Boolean.FALSE);
            throw e;
        } finally {
            tracedMethod.setTimeCostMs(System.currentTimeMillis() - begin);
            methods.add(tracedMethod);
        }
    }

}
