package song.pan.toolkit.redis.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@Component
public class RedisTemplateTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;


    public void test() throws JsonProcessingException {
        redisTemplate.opsForValue().set("test", "test");
        Object test = redisTemplate.opsForValue().get("test");
        System.out.println(test);

        // 存入对象，取出来的还是对象，自动做了一个序列化和反序列化的操作
        redisTemplate.opsForValue().set("user", new User("阿勒克斯Sanders", 28));
        Object user = redisTemplate.opsForValue().get("user");
        System.out.println(user.getClass().getName());
        System.out.println(user);

        // 存入string，取出来的还是string
        redisTemplate.opsForValue().set("user2", new ObjectMapper().writeValueAsString(new User("阿勒克斯Sanders", 28)));
        Object user2 = redisTemplate.opsForValue().get("user2");
        System.out.println(user2.getClass().getName());
        System.out.println(user2);

    }




}
