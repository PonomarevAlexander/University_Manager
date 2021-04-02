package com.foxminded.university.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
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
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";
    private static final String TEST_NAME_1 = "teacherName1";
    private static final String TEST_NAME_4 = "teacherName4";
    private static final String TEST_LAST_NAME_1 = "teacherLastName1";
    private static final String TEST_LAST_NAME_4 = "teacherLastName4";
    
    @Mock
    TimetableDao mockedTimetableDao;
    
    @Mock
    TeacherDao mockedTeacherDao;
    
    @Mock
    LessonDao mockedLessonDao;
    
    @Mock
    Timetable mockedTimetable;
    
    @Mock
    Teacher mockedTeacher;
    
    @Mock
    Iterator<Teacher> mockedIterator;
    
    @Mock
    List<Teacher> mockedTeacherList;
    
    
    @InjectMocks
    TeacherService mockedTeacherService;
    
    TeacherService teacherService;
    TimetableDao timetableDao;
    TeacherDao teacherDao;
    LessonDao lessonDao;
    
    @BeforeEach
    void setup() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA).build();
        
        this.timetableDao = new TimetableDao(dataSource);
        this.teacherDao = new TeacherDao(dataSource);
        this.lessonDao = new LessonDao(dataSource);
        this.teacherService = new TeacherService();
        teacherService.setTimetableDao(timetableDao);
        teacherService.setTeacherDao(teacherDao);
        teacherService.setLessonDao(lessonDao);
    }
    
   @Test
   void testAdd() {
       Teacher newTeacher = new Teacher(TEST_NAME_4, TEST_LAST_NAME_4);
       newTeacher.setTimetable(timetableDao.get(3));
       
       teacherService.add(newTeacher);
       Teacher actual = teacherService.getById(4);
       
       assertEquals(4, actual.getId());
       assertEquals(TEST_NAME_4, actual.getName());
       assertEquals(TEST_LAST_NAME_4, actual.getLastName());
       assertEquals(3, actual.getTimetable().getId());
   }
   
   @Test
   void testTimesInvoke_Add() {
       when(mockedTeacher.getTimetable()).thenReturn(mockedTimetable);
       when(mockedTeacherDao.add(mockedTeacher)).thenReturn(anyInt());
       
       mockedTeacherService.add(mockedTeacher);
       verify(mockedTeacherDao).add(mockedTeacher);
       verify(mockedTimetableDao).setTimetableToTeacher(0, 0);
   }
   
   @Test
   void testOrderInvoke_Add() {
       when(mockedTeacher.getTimetable()).thenReturn(mockedTimetable);
       when(mockedTeacherDao.add(mockedTeacher)).thenReturn(anyInt());
       InOrder inOrder = inOrder(mockedTeacherDao, mockedTimetableDao);
       
       mockedTeacherService.add(mockedTeacher);
       
       inOrder.verify(mockedTeacherDao).add(mockedTeacher);
       inOrder.verify(mockedTimetableDao).setTimetableToTeacher(0, 0);
   }
   
   @Test
   void testGetById() {
       Teacher actual = teacherService.getById(1);
       
       assertEquals(1, actual.getId());
       assertEquals(TEST_NAME_1, actual.getName());
       assertEquals(TEST_LAST_NAME_1, actual.getLastName());
       assertEquals(1, actual.getTimetable().getId());
   }
   
   @Test
   void testTimesInvoke_GetById() {
       when(mockedTeacherDao.get(0)).thenReturn(mockedTeacher);
       when(mockedTimetableDao.getTimetableRelatedTeacher(0)).thenReturn(mockedTimetable);
       
       mockedTeacherService.getById(0);
       verify(mockedTeacherDao).get(0);
       verify(mockedTimetableDao).getTimetableRelatedTeacher(0);
       verify(mockedLessonDao).getLessonsOfTimetable(0);
   }
   
   @Test
   void testOrderInvoke_GetById() {
       when(mockedTeacherDao.get(0)).thenReturn(mockedTeacher);
       when(mockedTimetableDao.getTimetableRelatedTeacher(0)).thenReturn(mockedTimetable);
       InOrder inOrder = inOrder(mockedTeacherDao, mockedTimetableDao, mockedLessonDao);
       
       mockedTeacherService.getById(0);
       inOrder.verify(mockedTeacherDao).get(0);
       inOrder.verify(mockedTimetableDao).getTimetableRelatedTeacher(0);
       inOrder.verify(mockedLessonDao).getLessonsOfTimetable(0);
   }
   
   @Test
   void testGetAll() {
       List<Teacher> actual = teacherService.getAll();
       assertEquals(3, actual.size());
   }
   
   @Test
   void testUpdate() {
       int id = 1;
       Teacher initial = teacherService.getById(id);
       initial.setName(TEST_NAME_4);
       initial.setLastName(TEST_LAST_NAME_4);
       initial.setTimetable(timetableDao.get(2));
       
       teacherService.update(initial);
       Teacher actual = teacherService.getById(id);
       
       assertEquals(id, actual.getId());
       assertEquals(TEST_NAME_4, actual.getName());
       assertEquals(TEST_LAST_NAME_4, actual.getLastName());
       assertEquals(2, actual.getTimetable().getId());
   }
   
   @Test
   void testTimesInvoke_Update() {
       when(mockedTeacher.getTimetable()).thenReturn(mockedTimetable);
       mockedTeacherService.update(mockedTeacher);
       
       verify(mockedTeacherDao).update(mockedTeacher);
       verify(mockedTimetableDao).updateTimetableRelatedTeacher(0, 0);
   }
   
   @Test
   void testOrderInvoke_Update() {
       when(mockedTeacher.getTimetable()).thenReturn(mockedTimetable);
       InOrder inOrder = inOrder(mockedTeacherDao, mockedTimetableDao);
       mockedTeacherService.update(mockedTeacher);
       
       inOrder.verify(mockedTeacherDao).update(mockedTeacher);
       inOrder.verify(mockedTimetableDao).updateTimetableRelatedTeacher(0, 0);
   }
   
   @Test
   void testRemove() {
       List<Teacher> initial = teacherService.getAll();
       teacherService.remove(1);
       List<Teacher> actual = teacherService.getAll();
       
       assertEquals(3, initial.size());
       assertEquals(2, actual.size());
   }

}
