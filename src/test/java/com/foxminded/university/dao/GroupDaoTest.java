package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.persistence.GroupDao;

class GroupDaoTest {

    private GroupDao groupDao;

    private static final String TEST_NAME_1 = "group1";
    private static final String TEST_NAME_2 = "group2";
    private static final String TEST_NAME_4 = "group4";
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";

    @BeforeEach
    void init() {
        DataSource testDataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();

        groupDao = new GroupDao(testDataSource);
    }

    @Test
    void testAdd() {
        groupDao.add(new Group(TEST_NAME_4));
        Group actual = groupDao.get(4);

        assertEquals(4, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
    }

    @Test
    void testGetAll() {
        List<Group> groupList = groupDao.getAll();
        assertEquals(3, groupList.size());
    }

    @Test
    void testUpdate() {
        Group initial = groupDao.get(1);
        assertEquals(1, initial.getId());
        assertEquals(TEST_NAME_1, initial.getName());

        initial.setName(TEST_NAME_4);
        groupDao.update(initial);

        Group actual = groupDao.get(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
    }

    @Test
    void testRemove() {
        List<Group> initialList = groupDao.getAll();
        assertEquals(3, initialList.size());

        groupDao.remove(1);
        List<Group> actual = groupDao.getAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testGetGroupOfStudent() {
        Group actual = groupDao.getGroupByStudent(3);
        assertEquals(2, actual.getId());
        assertEquals(TEST_NAME_2, actual.getName());
    }
    
    @Test
    void testGetGroupByLesson() {
        Group actual = groupDao.getGroupByLesson(1);
        assertEquals(1, actual.getId());
    }
    
    @Test
    void testGetGroupByDepartment() {
        List<Group> actual = groupDao.getGroupsByDepartment(1);
        assertEquals(1, actual.get(0).getId());
        assertEquals(2, actual.get(1).getId());
    }
    
    @Test
    void testGetGroupByTeacher() {
        Group actual = groupDao.getGroupByTeacher(3);
        assertEquals(3, actual.getId());
    }
}
