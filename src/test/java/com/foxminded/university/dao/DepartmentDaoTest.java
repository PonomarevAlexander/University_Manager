package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.foxminded.university.domain.exceptions.EntityNotFoundException;
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.persistence.DepartmentDao;

class DepartmentDaoTest {

    private DepartmentDao departmentDao;
    private static final String TEST_NAME_1 = "department1";
    private static final String TEST_NAME_4 = "department4";
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    
    @BeforeEach
    void init() {
        DataSource testDataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();

        departmentDao = new DepartmentDao(testDataSource);
    }
    
    @Test
    void testAdd() {
        departmentDao.add(new Department(TEST_NAME_4));
        Department actual = departmentDao.get(4);

        assertEquals(4, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
    }
    
    @Test
    void testGet() {
        Department actual = departmentDao.get(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_1, actual.getName());
    }
    
    @Test
    void testShouldThrowsException() {
        assertThrows(EntityNotFoundException.class, () -> {departmentDao.get(999);});
        assertThrows(EntityNotFoundException.class, () -> {departmentDao.remove(999);});
    }
    
    @Test
    void testGetAll() {
        List<Department> actual = departmentDao.getAll();
        assertEquals(3, actual.size());
    }
    
    @Test
    void testUpdate() {
        Department initial = departmentDao.get(1);
        assertEquals(1, initial.getId());
        assertEquals(TEST_NAME_1, initial.getName());
        
        initial.setName(TEST_NAME_4);
        departmentDao.update(initial);
        
        Department actual = departmentDao.get(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
    }
    
    @Test
    void testRemove() {
        List<Department> initial = departmentDao.getAll();
        departmentDao.remove(1);
        List<Department> actual = departmentDao.getAll();
        
        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }
}
