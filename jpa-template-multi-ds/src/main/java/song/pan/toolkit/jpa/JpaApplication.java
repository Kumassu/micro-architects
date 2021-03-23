package song.pan.toolkit.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author Song Pan
 * @version 1.0.0
 */
@SpringBootApplication
public class JpaApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(JpaApplication.class);

        JdbcTemplate primaryJdbcTemplate = applicationContext.getBean("primaryJdbcTemplate", JdbcTemplate.class);
        JdbcTemplate secondaryJdbcTemplate = applicationContext.getBean("secondaryJdbcTemplate", JdbcTemplate.class);


        List<Map<String, Object>> rows = primaryJdbcTemplate.queryForList("select * from primary_city limit 10");
        System.out.println(rows);

        rows = secondaryJdbcTemplate.queryForList("select * from secondary_city limit 10");
        System.out.println(rows);

        rows = secondaryJdbcTemplate.queryForList("select * from jpa_test limit 10");
        System.out.println(rows);

        secondaryJdbcTemplate.update("drop table jpa_test");
    }

}
