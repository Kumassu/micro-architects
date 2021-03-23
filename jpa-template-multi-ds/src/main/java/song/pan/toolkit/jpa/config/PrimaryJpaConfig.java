package song.pan.toolkit.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="primaryEntityManagerFactory",
        transactionManagerRef="primaryTransactionManager",
        basePackages= {"song.pan.toolkit.jpa.dao.primary" })
public class PrimaryJpaConfig {


    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primaryDataSource;


    @Autowired(required = false)
    EntityManagerFactoryBuilder emfBuilder;


    @Autowired(required = false)
    private JpaProperties jpaProps;


    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory() {

        // use emf builder
//        return emfBuilder.dataSource(primaryDataSource)
//                .properties(jpaProperties.getProperties())
//                .packages("song.pan.toolkit.jpa.entity.primary")
//                .persistenceUnit("primaryPersistenceUnit")
//                .build();

        // new bean
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(primaryDataSource);
        emf.setPackagesToScan("song.pan.toolkit.jpa.entity.primary");
        emf.setJpaDialect(new HibernateJpaDialect());
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.putAll(jpaProps.getProperties());
        emf.setJpaProperties(jpaProperties);
        return emf;
    }


    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryEntityManagerFactory")
                                                                            LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory.getObject());
    }


}
