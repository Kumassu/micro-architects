package song.pan.toolkit.spring.boot.template.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Configuration
/**
 * 如果使用 {@link EnableConfigurationProperties} 会自动把{@link EnableConfigurationProperties#value()} 的类注入进容器，并且注入Environment的属性
 * 如果不使用 {@link EnableConfigurationProperties} 要在 {@link ConfigurationProperties} 标注的类上面加上，{@link Component} 才能注入进容器并给属性赋值
 */
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {


}
