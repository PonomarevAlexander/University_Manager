package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.foxminded.university.domain.models.Student;
import com.foxminded.university.persistence.StudentDao;

class StudentDaoTest {

    private StudentDao studentDao;

    private static final String TEST_NAME_1 = "studentName1";
    private static final String TEST_NAME_2 = "studentName2";
    private static final String TEST_NAME_3 = "studentName3";
    private static final String TEST_NAME_4 = "studentName4";
    private static final String TEST_LAST_NAME_1 = "studentLastName1";
    private static final String TEST_LAST_NAME_4 = "studentLastName4";
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";

    @BeforeEach
    void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();
        
        this.studentDao = new StudentDao(dataSource);
    }

    @Test
    void testAdd() {
        studentDao.add(new Student(TEST_NAME_4, TEST_LAST_NAME_4, 100));
        Student actual = studentDao.get(4);
        assertEquals(4, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(TEST_LAST_NAME_4, actual.getLastName());
        assertEquals(100, actual.getAge());
    }

    @Test
    void testGet() {
        Student actual = new Student();
        actual = studentDao.get(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_1, actual.getName());
        assertEquals(TEST_LAST_NAME_1, actual.getLastName());
        assertEquals(11, actual.getAge());
    }

    @Test
    void testGetAll() {
        List<Student> actual = new ArrayList<>();
        actual = studentDao.getAll();
        assertEquals(3, actual.size());
        assertEquals(TEST_NAME_1, actual.get(0).getName());
        assertEquals(TEST_NAME_2, actual.get(1).getName());
        assertEquals(TEST_NAME_3, actual.get(2).getName());
    }

    @Test
    void testUpdate() {
        Student initial = studentDao.get(1);
        assertEquals(TEST_NAME_1, initial.getName());
        assertEquals(TEST_LAST_NAME_1, initial.getLastName());
        assertEquals(11, initial.getAge());

        initial.setName(TEST_NAME_4);
        initial.setLastName(TEST_LAST_NAME_4);
        initial.setAge(44);
        studentDao.update(initial);

        Student actual = studentDao.get(1);
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(TEST_LAST_NAME_4, actual.getLastName());
        assertEquals(44, actual.getAge());
        assertEquals(1, actual.getId());
    }

    @Test
    void testRemove() {
        List<Student> initialList = studentDao.getAll();
        assertEquals(3, initialList.size());

        studentDao.remove(3);
        List<Student> actualList = studentDao.getAll();
        assertEquals(2, actualList.size());
    }

    @Test
    void testgetStudentRelatedGroup() {
        List<Student> studentsList = studentDao.getStudentRelatedGroup(1);

        assertEquals(1, studentsList.get(0).getId());
        assertEquals(2, studentsList.get(1).getId());
        assertEquals(TEST_NAME_1, studentsList.get(0).getName());
        assertEquals(TEST_NAME_2, studentsList.get(1).getName());
    }
}
