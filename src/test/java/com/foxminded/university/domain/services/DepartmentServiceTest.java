package com.foxminded.university.domain.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.persistence.DepartmentDao;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.TeacherDao;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private DepartmentDao departmentDao;
    private DepartmentService departmentService;
    
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    private static final String TEST_NAME_1 = "department1";
    private static final String TEST_NAME_4 = "department4";
    private static final String TEST_NAME_3 = "department3";
    
    @Mock
    private TeacherDao mockedTeacherDao;
    
    @Mock
    private GroupDao mockedGroupDao;
    
    @Mock
    private DepartmentDao mockedDepartmentDao;
    
    @InjectMocks
    private DepartmentService mockedDepartmentService;
    
    @BeforeEach
    void setup() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();
        
        this.teacherDao = new TeacherDao(dataSource);
        this.departmentDao = new DepartmentDao(dataSource);
        this.groupDao = new GroupDao(dataSource);
        this.departmentService = new DepartmentService();
        departmentService.setDepartmentDao(departmentDao);
        departmentService.setGroupDao(groupDao);
        departmentService.setTeacherDao(teacherDao);
    }

    @Test
    void testAdd() {
        departmentService.add(new Department(TEST_NAME_4));
        Department actual = departmentService.getById(4);
        
        assertEquals(4, actual.getId());
        assertEquals(0, actual.getGroupList().size());
        assertEquals(0, actual.getTeacherList().size());
    }

    @Test
    void testGetById() {
        Department actual = departmentService.getById(1);
        
        assertEquals(TEST_NAME_1, actual.getName());
        assertEquals(1, actual.getId());
        assertEquals(2, actual.getGroupList().size());
        assertEquals(1, actual.getGroupList().get(0).getId());
        assertEquals(2, actual.getGroupList().get(1).getId());
        assertEquals(2, actual.getTeacherList().size());
        assertEquals(1, actual.getTeacherList().get(0).getId());
    }

    @Test
    void testGetAll() {
        List<Department> actual = departmentService.getAll();
        assertEquals(3, actual.size());
    }

    @Test
    void testUpdate() {
        Department initial = departmentService.getById(3);
        
        assertEquals(3, initial.getId());
        assertEquals(TEST_NAME_3, initial.getName());
        assertEquals(0, initial.getGroupList().size());
        assertEquals(0, initial.getTeacherList().size());
        
        initial.setName(TEST_NAME_4);
        initial.setGroupList(groupDao.getAll());
        initial.setTeacherList(teacherDao.getAll());
        departmentService.update(initial);
        
        Department actual = departmentService.getById(3);
        
        assertEquals(3, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(3, actual.getGroupList().size());
        assertEquals(3, actual.getTeacherList().size());
    }

    @Test
    void testRemove() {
        List<Department> initial = departmentService.getAll();
        departmentService.remove(1);
        List<Department> actual = departmentService.getAll();
        
        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }

}
