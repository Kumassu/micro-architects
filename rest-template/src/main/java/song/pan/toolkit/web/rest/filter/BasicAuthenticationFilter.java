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
public class BasicAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    UserService userService;


    /**
     * Basic authentication header:
     * Authorization : Basic base64_encode(username:password)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // only filter Get method for testing
        if (!request.getMethod().equals(HttpMethod.GET.name())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        if (null == authorization || !authorization.startsWith("Basic ")) {
            request.getRequestDispatcher("/unauthorized").forward(request, response);
            return;
        }

        String token = authorization.substring("Basic ".length());
        log.info("[Filter] Basic Authentication, token : {}", token);
        String[] usernameAndPassword = new String(Base64.getDecoder().decode(token)).split(":");
        String userId = usernameAndPassword[0];
        String password = usernameAndPassword[1];
        log.info("[Filter] Basic Authentication, user : {}, password : {}", userId, password);
        UserDetails user = userService.getUserById(userId);
        if (null == user || !user.getPassword().equals(password)) {
            request.getRequestDispatcher("/forbidden").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
