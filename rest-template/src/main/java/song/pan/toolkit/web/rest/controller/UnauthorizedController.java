package song.pan.toolkit.web.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 *
 * @author Song Pan
 * @version 1.0.0
 */
public class UnauthorizedController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView(), new HashMap<>());
        mav.setStatus(HttpStatus.UNAUTHORIZED);
        mav.addObject("succ", Boolean.FALSE);
        mav.addObject("code", HttpStatus.UNAUTHORIZED.value());
        mav.addObject("msg", "unauthorized");
        return mav;
    }

}
