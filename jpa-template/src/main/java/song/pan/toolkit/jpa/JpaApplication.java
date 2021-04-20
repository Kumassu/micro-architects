package song.pan.toolkit.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import song.pan.toolkit.jpa.dao.primary.PrimaryCityRepo;
import song.pan.toolkit.jpa.entity.primary.PrimaryCity;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.LinkedList;
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


        PrimaryCityRepo cityRepo = applicationContext.getBean(PrimaryCityRepo.class);

        System.out.println("============= Paged Query ==============");

        Page<PrimaryCity> page = cityRepo.findAll(PageRequest.of(0, 10));
        page.getContent().forEach(System.out::println);

        page = cityRepo.findAll(PageRequest.of(1, 10));
        page.getContent().forEach(System.out::println);

        Specification<PrimaryCity> specification = new Specification<>() {

            @Override
            public Predicate toPredicate(Root<PrimaryCity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new LinkedList<>();

                Predicate predicate = criteriaBuilder.greaterThan(root.get("id").as(Long.class), 15L);
                predicates.add(predicate);

                predicate = criteriaBuilder.equal(root.get("countryCode").as(String.class), "NLD");
                predicates.add(predicate);

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        List<PrimaryCity> cities = cityRepo.findAll(specification, PageRequest.of(0, 5)).getContent();
        System.out.println("============= Conditional Query ==============");
        cities.forEach(System.out::println);

    }

}
