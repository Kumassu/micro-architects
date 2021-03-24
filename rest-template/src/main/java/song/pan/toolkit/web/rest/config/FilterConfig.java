package song.pan.toolkit.web.rest.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import song.pan.toolkit.web.rest.filter.GatewayFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<GatewayFilter> gatewayFilter() {
        FilterRegistrationBean<GatewayFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new GatewayFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

}