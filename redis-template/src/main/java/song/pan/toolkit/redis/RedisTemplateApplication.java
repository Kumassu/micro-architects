package song.pan.toolkit.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import song.pan.toolkit.redis.test.RedisTemplateTest;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@SpringBootApplication
public class RedisTemplateApplication {

    public static void main(String[] args) throws JsonProcessingException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(RedisTemplateApplication.class);

        RedisTemplateTest test = applicationContext.getBean(RedisTemplateTest.class);
        test.test();

        applicationContext.close();
    }

}
