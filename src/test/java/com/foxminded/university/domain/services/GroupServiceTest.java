package com.foxminded.university.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Student;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.StudentDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private GroupService groupService;
    private StudentDao studentDao;
    private GroupDao groupDao;
    private TimetableDao timetableDao;
    private TeacherDao teacherDao;

    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    private static final String TEST_GROUP_NAME_1 = "group1";
    private static final String TEST_GROUP_NAME_4 = "group4";
    
    @Mock
    StudentDao mockedStudentDao;
    
    @Mock
    GroupDao mockedGroupDao;
    
    @Mock
    TimetableDao mockedTimetableDao;
    
    @Mock
    TeacherDao mockedTeacherDao;
    
    @Mock
    Student mockedStudent;
    
    @Mock
    Group mockedGroup;
    
    @Mock
    Timetable mockedTimeTable;
    
    @Mock
    Teacher mockedTeacher;
    
    @Mock
    List<Student> mockedStudentList;
    
    @InjectMocks
    GroupService mockedGroupService;

    
    @BeforeEach
    void setup() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();
        
        this.studentDao = new StudentDao(dataSource);
        this.groupDao = new GroupDao(dataSource);
        this.timetableDao = new TimetableDao(dataSource);
        this.teacherDao = new TeacherDao(dataSource);
        groupService = new GroupService();
        groupService.setStudentDao(studentDao);
        groupService.setGroupDao(groupDao);
        groupService.setTimetableDao(timetableDao);
        groupService.setTeacherDao(teacherDao);        
    }
    
    @Test
    void testAdd() {
        Group newGroup = new Group(TEST_GROUP_NAME_1);
        Teacher groupTeacher = teacherDao.get(3);
        Student student = studentDao.get(2);
        Timetable timetable = timetableDao.get(3);
        newGroup.setCheef(groupTeacher);
        newGroup.setStudentList(new ArrayList<Student>(Arrays.asList(student)));
        newGroup.setTimetable(timetable);
        
        groupService.add(newGroup);
        Group actual = groupService.getById(4);
        
        assertEquals(4, actual.getId());
        assertEquals(3, actual.getCheef().getId());
        assertEquals(3, actual.getTimetable().getId());
        assertEquals(1, actual.getStudentList().size());
        assertEquals(2, actual.getStudentList().get(0).getId());
    }
    
    @Test
    void testTimesInvokeAdd() {
        Group newGroup = new Group(TEST_GROUP_NAME_1);
        Teacher groupTeacher = teacherDao.get(3);
        Student student = studentDao.get(2);
        Timetable timetable = timetableDao.get(3);
        newGroup.setCheef(groupTeacher);
        newGroup.setStudentList(new ArrayList<Student>(Arrays.asList(student)));
        newGroup.setTimetable(timetable);
        
        mockedGroupService.add(newGroup);
        verify(mockedGroupDao).add(newGroup);
        verify(mockedTeacherDao).assignTeacherToGroup(groupTeacher.getId(), newGroup.getId());
        verify(mockedTimetableDao).setTimetableToGroup(timetable.getId(), newGroup.getId());
    }
    
    
    @Test
    void testGetById() {
        Group actual = groupService.getById(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_GROUP_NAME_1, actual.getName());
        assertEquals(1, actual.getCheef().getId());
        assertEquals(2, actual.getStudentList().size());
        assertEquals(1, actual.getStudentList().get(0).getId());
        assertEquals(2, actual.getStudentList().get(1).getId());
        assertEquals(1, actual.getTimetable().getId());
    }
    
    @Test
    void testGetAll() {
        List<Group> actual = groupService.getAll();
        assertEquals(3, actual.size());
    }
    
    @Test
    void testTimesInvokeGetAll() {
        mockedGroupService.getAll();
        verify(mockedGroupDao).getAll();
    }
    
    @Test
    void testUpdate() {
        int groupId = 1;
        Group initial = groupService.getById(groupId);
        assertEquals(1, initial.getId());
        assertEquals(1, initial.getCheef().getId());
        assertEquals(1, initial.getTimetable().getId());
        assertEquals(2, initial.getStudentList().size());
        assertEquals(TEST_GROUP_NAME_1, initial.getName());
        
        initial.setName(TEST_GROUP_NAME_4);
        initial.setCheef(teacherDao.get(2));
        initial.setTimetable(timetableDao.get(2));
        initial.setStudentList(studentDao.getAll());
        
        groupService.update(initial);
        Group actual = groupService.getById(groupId);
        
        assertEquals(1, actual.getId());
        assertEquals(2, actual.getCheef().getId());
        assertEquals(2, actual.getTimetable().getId());
        assertEquals(3, actual.getStudentList().size());
        assertEquals(TEST_GROUP_NAME_4, actual.getName());
    }
    
    @Test
    void testRemove() {
        List<Group> initial = groupService.getAll();
        groupService.remove(1);
        List<Group> actual = groupService.getAll();
        
        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }    
}
