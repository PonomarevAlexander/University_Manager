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
class TimetableServiceTest {
    
    private TimetableService timetableService;
    private GroupDao groupDao;
    private TimetableDao timetableDao;
    private TeacherDao teacherDao;
    private LessonDao lessonDao;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    private static final String TEST_TIME_1 = "2021-01-11 11:11:11";
    private static final String TEST_TIME_4 = "2021-04-14 14:14:14";
    
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
    
    @InjectMocks
    TimetableService mockedTimetableService;
    
    @BeforeEach
    void setup() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();
        
        this.groupDao = new GroupDao(dataSource);
        this.timetableDao = new TimetableDao(dataSource);
        this.teacherDao = new TeacherDao(dataSource);
        this.lessonDao = new LessonDao(dataSource);
        this.timetableService = new TimetableService();
        timetableService.setGroupDao(groupDao);
        timetableService.setLessonDao(lessonDao);
        timetableService.setTeacherDao(teacherDao);
        timetableService.setTimetableDao(timetableDao);
    }
    
    @Test
    void testAdd() {
        Timetable newTimetable = new Timetable();
        List<Lesson> lessons = lessonDao.getAll();
        newTimetable.setSchedule(lessons);
        newTimetable.setCreationDate(LocalDateTime.parse(TEST_TIME_4, FORMATTER));
        
        timetableService.add(newTimetable);
        Timetable actual = timetableService.getById(4);
        
        assertEquals(4, actual.getId());
        assertEquals(3, actual.getSchedule().size());
        assertEquals(lessons.get(0).getId(), actual.getSchedule().get(0).getId());
        assertEquals(lessons.get(1).getId(), actual.getSchedule().get(1).getId());
        assertEquals(lessons.get(2).getId(), actual.getSchedule().get(2).getId());
        assertEquals(LocalDateTime.parse(TEST_TIME_4, FORMATTER), actual.getCreationDate());
    }
    
    @Test
    void testTimesInvoke_Add() {
        mockedTimetableService.add(mockedTimeTable);
        verify(mockedTimetableDao).add(mockedTimeTable);
    }
    
    @Test
    void testGetById() {
        Timetable actual = timetableService.getById(1);
        
        assertEquals(1, actual.getId());
        assertEquals(2, actual.getSchedule().size());
        assertEquals(TEST_TIME_1, actual.getCreationDate().format(FORMATTER));
    }
    
    @Test
    void testTimesInvoke_GetById() {
        when(mockedTimetableDao.get(0)).thenReturn(mockedTimeTable);
        mockedTimetableService.getById(0);
        
        verify(mockedTimetableDao).get(0);
        verify(mockedLessonDao).getLessonsOfTimetable(0);
    }
    
    @Test
    void testGetAll() {
        List<Timetable> actual = timetableService.getAll();
        assertEquals(3, actual.size());
    }
    
    @Test
    void testUpdate() {
        Timetable initial = timetableService.getById(1);
        List<Lesson> lessons = lessonDao.getAll();
        initial.setCreationDate(LocalDateTime.parse(TEST_TIME_4, FORMATTER));
        initial.setSchedule(lessons);
        
        timetableService.update(initial);
        Timetable actual = timetableService.getById(1);
        
        assertEquals(1, actual.getId());
        assertEquals(TEST_TIME_4, actual.getCreationDate().format(FORMATTER));
    }
    
    @Test
    void testRemove() {
        List<Timetable> initial = timetableDao.getAll();
        timetableDao.remove(1);
        List<Timetable> actual = timetableDao.getAll();
        
        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }
}
