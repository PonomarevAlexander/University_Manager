package com.foxminded.university.domain.config;

import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

@Configuration
@ComponentScan("com.foxminded.university")
public class ApplicationConfig {
    
    private static final String JNDI_DATASOURCE_PATH = "java:comp/env/jdbc/postgres";
    
    @Bean
    public DataSource getDataSource() throws NamingException {
        JndiTemplate jndiTemplate = new JndiTemplate();
        return (DataSource) jndiTemplate.lookup(JNDI_DATASOURCE_PATH);
    }
}   

