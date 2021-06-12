package com.foxminded.university.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.domain.config.ApplicationConfig;
import com.foxminded.university.domain.models.Department;

@ExtendWith(SpringExtension.class)
class GenericHibernateDaoImplTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    void test() {
        assertNotNull(applicationContext);
    }
}
