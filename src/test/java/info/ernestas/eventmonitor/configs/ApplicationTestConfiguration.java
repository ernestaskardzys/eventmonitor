package info.ernestas.eventmonitor.configs;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.jms.ConnectionFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "info.ernestas.eventmonitor")
public class ApplicationTestConfiguration {

    private String dbUrl;

    private String dbUser;

    private String dbPassword;

    private String brokerUrl;

    @Bean
    public ServletContext servletContext() {
        return new MockServletContext();
    }

    @Bean
    public DataSource dataSource() {
        String db = dbUrl + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        return new SimpleDriverDataSource(new org.h2.Driver(), db, dbUser, dbPassword);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("info.ernestas.eventmonitor.dao.entity");
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        jpaProperties.put("hibernate.cache.use_query_cache", true);
        jpaProperties.put("hibernate.cache.use_second_level_cache", true);
        jpaProperties.put("hibernate.id.new_generator_mappings", false);

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Value("${db.url}")
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Value("${db.user}")
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    @Value("${db.password}")
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    @Value("${activemq.broker.url}")
    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

}
