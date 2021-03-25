package song.pan.toolkit.mybatis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import song.pan.toolkit.mybatis.entity.City;
import song.pan.toolkit.mybatis.mapper.CityMapper;

import java.util.List;
import java.util.Map;

/**
 * 如果是单数据源, 直接使用自动装配在启动类上面加 {@link MapperScan}
 * 如果是多数据源, 需要在每个配置类上面加
 *
 * @author Song Pan
 * @version 1.0.0
 * @see song.pan.toolkit.mybatis.config.MybatisConfig
 */
//@MapperScan(basePackages = {"song.pan.toolkit.mybatis.mapper"})
@SpringBootApplication
public class MybatisTemplateApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MybatisTemplateApplication.class);

        CityMapper cityMapper = applicationContext.getBean(CityMapper.class);

        System.out.println("== 1 to 10 ===");
        List<City> cities = cityMapper.betweenIds(1, 10);
        cities.forEach(System.out::println);
        System.out.println();

        System.out.println("== condition query ===");
        cities = cityMapper.query(Map.of("name", "Kabul", "countrycode", "AFG"));
        cities.forEach(System.out::println);
        System.out.println();

        System.out.println("== population larger than limit 5 ===");
        cities = cityMapper.populationLargerThan(10000, 5);
        cities.forEach(System.out::println);
        System.out.println();

        System.out.println("== top 5 rows ===");
        List<Map<String, Object>> rows = cityMapper.topRowsOf(5);
        rows.forEach(System.out::println);
        System.out.println();

        System.out.println("== city in (1, 10) ===");
        cities = cityMapper.citiesIn(List.of(1, 10));
        cities.forEach(System.out::println);
        System.out.println();


        System.out.println("== first page ===");
        PageHelper.startPage(3, 10);
        Page<City> page = cityMapper.pagedQuery();
        cities = page.getResult();
        cities.forEach(System.out::println);
        System.out.println();
    }

}
