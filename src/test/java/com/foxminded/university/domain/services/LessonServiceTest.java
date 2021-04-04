package com.foxminded.university.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    private static final String TEST_TIME_4 = "2021-04-14 14:14:14";
    private static final String TEST_NAME_4 = "biology";
    private static final String TEST_NAME_1 = "math";
    private static final String TEST_TIME_1 = "2021-01-11 11:11:11";
    
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private LessonDao lessonDao;
    private LessonService lessonService;
    
    
    @Mock
    GroupDao mockedGroupDao;
    
    @Mock
    TimetableDao mockedTimetableDao;
    
    @Mock
    TeacherDao mockedTeacherDao;
    
    @Mock
    LessonDao mockedLessonDao;
    
    @Mock
    Timetable mockedTimeTable;
    
    @Mock
    Teacher mockedTeacher;
    
    @Mock
    Lesson mockedLesson;
    
    @InjectMocks
    LessonService mockedLessonService;
    
    
    @BeforeEach
    void setup() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();
        
        lessonService = new LessonService();
        this.groupDao = new GroupDao(dataSource);
        this.teacherDao = new TeacherDao(dataSource);
        this.lessonDao = new LessonDao(dataSource);
        lessonService.setGroupDao(groupDao);
        lessonService.setLessonDao(lessonDao);
        lessonService.setTeacherDao(teacherDao);
    }
    
    @Test
    void testAdd() {
        Lesson lesson = new Lesson(TEST_NAME_4,
                LocalDateTime.parse(TEST_TIME_4, FORMATTER), 5400);
        lesson.setGroup(groupDao.get(3));
        lesson.setTeacher(teacherDao.get(3));
        
        lessonService.add(lesson);
        Lesson actual = lessonService.getById(4);
        
        assertEquals(4, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(TEST_TIME_4, actual.getStartTime().format(FORMATTER));
        assertEquals(5400, actual.getLessonDurationSecond());
        assertEquals(3, actual.getGroup().getId());
        assertEquals(3, actual.getTeacher().getId());
    }
    
    @Test
    void testTimesInvoke_Add() {
        mockedLessonService.add(mockedLesson);
        verify(mockedLessonDao).add(mockedLesson);
    }
    
    @Test
    void testGetById() {
        Lesson actual = lessonService.getById(1);
        
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_1, actual.getName());
        assertEquals(TEST_TIME_1, actual.getStartTime().format(FORMATTER));
        assertEquals(5400, actual.getLessonDurationSecond());
        assertEquals(1, actual.getTeacher().getId());
        assertEquals(1, actual.getGroup().getId());
    }
    
    @Test 
    void testTimesInvoke_GetById() {
        when(mockedLessonDao.get(0)).thenReturn(mockedLesson);
        mockedLessonService.getById(0);
        
        verify(mockedLessonDao).get(0);
        verify(mockedTeacherDao).getTeacherByLessonId(0);
        verify(mockedGroupDao).getGroupByLesson(0);
    }
    
    @Test
    void testOrderInvoke_GetById() {
        when(mockedLessonDao.get(0)).thenReturn(mockedLesson);
        InOrder inOrder = inOrder(mockedLessonDao, mockedTeacherDao, mockedGroupDao);
        mockedLessonService.getById(0);
        
        inOrder.verify(mockedLessonDao).get(0);
        inOrder.verify(mockedTeacherDao).getTeacherByLessonId(0);
        inOrder.verify(mockedGroupDao).getGroupByLesson(0);
    }
    
    @Test
    void testGetAll() {
        List<Lesson> actual = lessonService.getAll();
        assertEquals(3, actual.size());
    }
    
    @Test
    void testUpdate() {
        Lesson initial = lessonService.getById(1);
        initial.setName(TEST_NAME_4);
        initial.setStartTime(LocalDateTime.parse(TEST_TIME_4, FORMATTER));
        initial.setGroup(groupDao.get(2));
        initial.setTeacher(teacherDao.get(2));
        lessonService.update(initial);
        
        Lesson actual = lessonService.getById(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(TEST_TIME_4, actual.getStartTime().format(FORMATTER));
        assertEquals(2, actual.getGroup().getId());
        assertEquals(2, actual.getTeacher().getId());
    }
    
    @Test
    void testRemove() {
        List<Lesson> initial = lessonService.getAll();
        lessonService.remove(1);
        List<Lesson> actual = lessonService.getAll();
        
        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }
}
