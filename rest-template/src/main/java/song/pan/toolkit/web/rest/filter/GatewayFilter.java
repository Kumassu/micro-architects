package song.pan.toolkit.web.rest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import song.pan.toolkit.web.rest.common.ThreadCache;
import song.pan.toolkit.web.rest.domain.TracedMethod;
import song.pan.toolkit.web.rest.security.service.UserService;
import song.pan.toolkit.web.rest.security.service.dto.UserDetails;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static song.pan.toolkit.web.rest.common.CacheHolder.CACHE;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Slf4j
public class GatewayFilter extends OncePerRequestFilter {


    /**
     * you can dynamically fill this set on applicationContext initialization,
     * like find the beans or methods annotated with @Escape, then find the urls
     * from annotation such as @GetMapping or others.
     */
    private static final Set<String> ESCAPE_TRACE_URLS = Set.of("/api/v1/caches");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String id = UUID.randomUUID().toString();
        if (ThreadCache.TASK_ID.get() == null) {
            ThreadCache.TASK_ID.set(id);
            CACHE.put(id, new LinkedList<TracedMethod>());
        }

        filterChain.doFilter(request, response);

        List<TracedMethod> methods = (List<TracedMethod>) CACHE.getIfPresent(id);
        if ((methods != null && methods.isEmpty()) || ESCAPE_TRACE_URLS.contains(request.getRequestURI()) ) {
            CACHE.invalidate(id);
        }
        ThreadCache.TASK_ID.remove();
    }

}
