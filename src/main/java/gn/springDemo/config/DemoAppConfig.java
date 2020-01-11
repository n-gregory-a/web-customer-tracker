package gn.springDemo.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("gn.springDemo")
@PropertySource({ "classpath:persistence-mysql.properties" })
public class DemoAppConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;

    private Logger logger = Logger.getLogger(getClass().getName());

    // define a bean for security datasource
    @Bean
    public DataSource securityDataSource() {

        // create a connection pool
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        // set the jdbc driver class
        try {
            securityDataSource.setDriverClass(environment.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        // log the connection props
        // for sanity's sake, log this info
        // just to make sure we are really reading data from properties file
        logger.info(">>> jdbc.url=" + environment.getProperty("jdbc.url"));
        logger.info(">>> jdbc.user=" + environment.getProperty("jdbc.user"));

        // set db connection pool props
        securityDataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
        securityDataSource.setUser(environment.getProperty("jdbc.user"));
        securityDataSource.setPassword(environment.getProperty("jdbc.password"));

        // set connection pool props
        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

        return securityDataSource;
    }

    // need a helper method
    // read environment property and convert it to int
    private int getIntProperty(String propName) {
        String propVal = environment.getProperty(propName);

        // converting to int
        assert propVal != null;

        return Integer.parseInt(propVal);
    }

    private Properties getHibernateProperties() {
        // set hibernate properties
        Properties props = new Properties();

        props.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));

        return props;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        // create session factory
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        // set the properties
        sessionFactory.setDataSource(securityDataSource());
        sessionFactory.setPackagesToScan(environment.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());

        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        // setup transaction manager based on session factory
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

}