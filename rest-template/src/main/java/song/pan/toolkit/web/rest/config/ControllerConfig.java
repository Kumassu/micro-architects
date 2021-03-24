package song.pan.toolkit.web.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import song.pan.toolkit.web.rest.controller.ForbiddenController;
import song.pan.toolkit.web.rest.controller.UnauthorizedController;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Configuration
public class ControllerConfig{


    @Bean("/unauthorized")
    public UnauthorizedController unauthorized() {
        return new UnauthorizedController();
    }

    @Bean("/forbidden")
    public ForbiddenController forbidden() {
        return new ForbiddenController();
    }


}
