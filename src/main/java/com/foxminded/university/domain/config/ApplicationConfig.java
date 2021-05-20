package com.foxminded.university.domain.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("com.foxminded.university")
@PropertySource("classpath:db_config.properties")
public class ApplicationConfig {
    
    @Value("${database.driver}")
    private String driver;
    
    @Value("${database.url}")
    private String url;
    
    @Value("${database.user}")
    private String user;
    
    @Value("${database.password}")
    private String password;
    
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}

