package song.pan.toolkit.spring.boot.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import song.pan.toolkit.spring.boot.template.config.AppProperties;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@SpringBootApplication
public class SpringBootTemplateApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootTemplateApplication.class);

        AppProperties appProperties = applicationContext.getBean(AppProperties.class);

        System.out.println(appProperties);

    }

}
