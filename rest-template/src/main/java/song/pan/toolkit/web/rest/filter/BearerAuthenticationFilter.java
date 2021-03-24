package song.pan.toolkit.web.rest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import song.pan.toolkit.web.rest.security.service.UserService;
import song.pan.toolkit.web.rest.security.service.dto.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@WebFilter(urlPatterns = {"/api/*"})
@Slf4j
public class BearerAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    UserService userService;


    /**
     * Bearer authentication header:
     * Authorization : Bearer oauth_token
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // filter all methods exclude Get for testing
        if (request.getMethod().equals(HttpMethod.GET.name())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        if (null == authorization || !authorization.startsWith("Bearer ")) {
            request.getRequestDispatcher("/unauthorized").forward(request, response);
            return;
        }

        String token = authorization.substring("Basic ".length());
        log.info("[Filter] Bearer Authentication, token : {}", token);
        UserDetails user = userService.getUserByBearerToken(token);
        if (null == user) {
            request.getRequestDispatcher("/forbidden").forward(request, response);
            return;
        }
        log.info("[Filter] Bearer Authentication, user : {}, password : {}", user.getUserId(), user.getPassword());
        filterChain.doFilter(request, response);
    }

}
