package song.pan.toolkit.web.rest.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_TIME = "LOG_TIME";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[Interceptor] [{} {}] Addr: {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        log.info("[Interceptor] [{} {}] Handler: {} : {}", request.getMethod(), request.getRequestURI(), handler.getClass().getName(), handler);
        request.setAttribute(LOG_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("[Interceptor] [{} {}] ModelAndView : {}", request.getMethod(), request.getRequestURI(), modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long logTime = (long) request.getAttribute(LOG_TIME);
        log.info("[Interceptor] [{} {}] Time: {} ms, exception: {}", request.getMethod(), request.getRequestURI(), System.currentTimeMillis() - logTime, ex);
    }

}
