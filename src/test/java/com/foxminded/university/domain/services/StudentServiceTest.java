package com.foxminded.university.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.StudentDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    
    private StudentService studentService;
    private StudentDao studentDao;
    private GroupDao groupDao;
    private TimetableDao timetableDao;
    private TeacherDao teacherDao;
    private LessonDao lessonDao;
    
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    private static final String TEST_NAME_1 = "studentName1";
    private static final String TEST_NAME_4 = "studentName4";
    private static final String TEST_LAST_NAME_4 = "studentLastName4";
    private static final String TEST_GROUP_NAME = "testGroup4";
    
    @Mock
    private StudentDao mockedStudentDao;
    
    @Mock
    private GroupDao mockedGroupDao;
    
    @Mock
    private TimetableDao mockedTimetableDao;
    
    @Mock
    private TeacherDao mockedTeacherDao;
    
    @Mock
    private LessonDao mockedLessonDao;
    
    @Mock
    private Student mockedStudent;
    
    @Mock
    private Group mockedGroup;
    
    @Mock
    private Timetable mockedTimeTable;
    
    @Mock
    private Teacher mockedTeacher;
    
    @InjectMocks
    private StudentService mockedStudentService;
    
    @BeforeEach
    void setup() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();
        
        studentDao = new StudentDao(dataSource);
        this.groupDao = new GroupDao(dataSource);
        this.timetableDao = new TimetableDao(dataSource);
        this.teacherDao = new TeacherDao(dataSource);
        this.lessonDao = new LessonDao(dataSource);
        this.studentService = new StudentService();
        studentService.setStudentDao(studentDao);
        studentService.setGroupDao(groupDao);
        studentService.setTimetableDao(timetableDao);
        studentService.setTeacherDao(teacherDao);
        studentService.setLessonDao(lessonDao);
    }
    
    @Test
    void testAdd() {
        Student student = new Student(TEST_NAME_4, TEST_LAST_NAME_4, 100);
        student.setGroup(groupDao.get(1));
        studentService.add(student);
        
        Student actual = studentService.getById(4);
        assertEquals(4, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(1, actual.getGroup().getId());
    }
    
    @Test
    void testTimesInvokeIntoAdd() {
        Student student = new Student(TEST_NAME_4, TEST_LAST_NAME_4, 100);
        Group group = new Group(TEST_GROUP_NAME);
        group.setId(3);
        student.setGroup(group);
        mockedStudentService.add(student);
        
        verify(mockedStudentDao).add(student);
        verify(mockedStudentDao).setStudentToGroup(student.getId(),
                student.getGroup().getId());
    }
    
    @Test
    void testOrderInvokeIntoAdd() {
        Student student = new Student(TEST_NAME_4, TEST_LAST_NAME_4, 100);
        Group group = new Group(TEST_GROUP_NAME);
        group.setId(3);
        student.setGroup(group);
        InOrder inOrder = inOrder(mockedStudentDao);
        mockedStudentService.add(student);
        
        inOrder.verify(mockedStudentDao).add(student);        
        inOrder.verify(mockedStudentDao).setStudentToGroup(student.getId(),
                student.getGroup().getId());        
    }

    @Test
    void testGetById() {
        Student actual = studentService.getById(1);
        Group group = actual.getGroup();
        Timetable timetable = group.getTimetable();
        
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_1, actual.getName());
        assertEquals(1, group.getId());
        assertEquals(1, timetable.getId());
    }
    
    @Test
    void testTimesInvokeGetById() {
        when(mockedStudentDao.get(anyInt())).thenReturn(mockedStudent);
        when(mockedGroupDao.getGroupByStudent(anyInt())).thenReturn(mockedGroup);
        when(mockedTimetableDao.getTimetableRelatedGroup(anyInt())).thenReturn(mockedTimeTable);
        
        mockedStudentService.getById(1);
        
        verify(mockedStudentDao).get(anyInt());
        verify(mockedGroupDao).getGroupByStudent(anyInt());
        verify(mockedTimetableDao).getTimetableRelatedGroup(anyInt());
        verify(mockedLessonDao).getLessonsOfTimetable(anyInt());
        verify(mockedStudentDao).getStudentRelatedGroup(anyInt());
        verify(mockedTeacherDao).getGroupTeacher(anyInt());
    }
    
    @Test
    void testOrderInvokeGetById() {
        when(mockedStudentDao.get(anyInt())).thenReturn(mockedStudent);
        when(mockedGroupDao.getGroupByStudent(anyInt())).thenReturn(mockedGroup);
        when(mockedTimetableDao.getTimetableRelatedGroup(anyInt())).thenReturn(mockedTimeTable);
        InOrder inOrder = inOrder(mockedStudentDao, mockedGroupDao, mockedTimetableDao,
                mockedTeacherDao, mockedLessonDao);
        
        mockedStudentService.getById(anyInt());
        
        inOrder.verify(mockedStudentDao).get(anyInt());
        inOrder.verify(mockedGroupDao).getGroupByStudent(anyInt());
        inOrder.verify(mockedTimetableDao).getTimetableRelatedGroup(anyInt());
        inOrder.verify(mockedLessonDao).getLessonsOfTimetable(anyInt());
        inOrder.verify(mockedStudentDao).getStudentRelatedGroup(anyInt());
        inOrder.verify(mockedTeacherDao).getGroupTeacher(anyInt());
    }
    
    @Test
    void testGetAll() {
        List<Student> actual = studentService.getAll();
        
        assertEquals(3, actual.size());
        assertEquals(TEST_NAME_1, actual.get(0).getName());
        assertEquals(1, actual.get(0).getGroup().getId());
        assertEquals(1, actual.get(0).getGroup().getTimetable().getId());
    }
    
    @Test
    void testUpdate() {
        Student initial = studentService.getById(1);
        Group newGroup = new Group();
        newGroup.setId(2);
        initial.setName(TEST_NAME_4);
        initial.setLastName(TEST_LAST_NAME_4);
        initial.setGroup(newGroup);
        
        studentService.update(initial);
        Student actual = studentService.getById(1);
        
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(TEST_LAST_NAME_4, actual.getLastName());
        assertEquals(2, actual.getGroup().getId());
    }
    
    @Test
    void testTimeInvokeUpdate() {
        when(mockedStudent.getGroup()).thenReturn(mockedGroup);
        mockedStudentService.update(mockedStudent);
        
        verify(mockedStudentDao).update(mockedStudent);
        verify(mockedStudentDao).setStudentToGroup(mockedStudent.getId(),
                mockedStudent.getGroup().getId());
    }
    
    @Test
    void testInvokeOrderUpdate() {
        when(mockedStudent.getGroup()).thenReturn(mockedGroup);
        InOrder inOrder = inOrder(mockedStudentDao);
        
        mockedStudentService.update(mockedStudent);
        
        inOrder.verify(mockedStudentDao).update(mockedStudent);
        inOrder.verify(mockedStudentDao).setStudentToGroup(mockedStudent.getId(),
                mockedStudent.getGroup().getId());
    }
    
    @Test
    void testRemove() {
        List<Student> initial = studentService.getAll();
        studentService.remove(1);
        List<Student> actual = studentService.getAll();
        
        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }
    
    @Test
    void testTimeInvokeRemove() {
        mockedStudentService.remove(anyInt());
        
        verify(mockedStudentDao).remove(anyInt());
    }

}
