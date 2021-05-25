package com.foxminded.university.domain.controllers;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.LessonService;
import com.foxminded.university.domain.services.TeacherService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    private MockMvc mockMvc;
    
    private static final String MODEL_LESSONS = "lessons";
    private static final String MODEL_LESSON = "lesson";
    private static final String MODEL_GROUPS = "groups";
    private static final String MODEL_TEACHERS = "teachers";
    private static final String VIEW_ALL_LESSONS = "lesson/all-lessons";
    private static final String VIEW_ONE_LESSON = "lesson/lesson";
    private static final String VIEW_NEW_LESSON = "lesson/new-lesson";
    private static final String VIEW_UPDATE_LESSON = "lesson/update-lesson";
    private static final String VIEW_REDIRECT_TO_ALL_LESSONS = "redirect:/lessons";
    
    @Mock
    private LessonService lessonService;
    
    @Mock
    private TeacherService teacherService;
    
    @Mock
    private GroupService groupService;
    
    @Mock
    private Lesson mockedLesson;
    
    @InjectMocks
    private LessonController lessonController;
    
    @BeforeEach
    public void setupMock() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }
    
    @Test
    void testShouldRenderLessonsStatusPage() throws Exception {
        when(lessonService.getAll()).thenReturn(List.of(new Lesson()));
        
        mockMvc.perform(get("/lessons"))
        .andExpect(view().name(VIEW_ALL_LESSONS))
        .andExpect(model().attribute(MODEL_LESSONS, List.of(new Lesson())));
        
        verify(lessonService).getAll();
    }
    
    @Test
    void testShouldRenderLessonProfilePageById() throws Exception {
        when(lessonService.getById(1)).thenReturn(mockedLesson);
        when(mockedLesson.getStartTime()).thenReturn(LocalDateTime.parse("2021-01-01T12:12"));
        
        mockMvc.perform(get("/lessons/1"))
        .andExpect(view().name(VIEW_ONE_LESSON))
        .andExpect(model().attribute(MODEL_LESSON, mockedLesson));
        
        verify(lessonService).getById(1);
        verify(mockedLesson).getStartTime();
    }
    
    @Test
    void testShouldRenderFormPageForCreatingNewLesson() throws Exception {
        when(teacherService.getAll()).thenReturn(List.of(new Teacher()));
        when(groupService.getAll()).thenReturn(List.of(new Group()));
        
        mockMvc.perform(get("/lessons/new"))
        .andExpect(view().name(VIEW_NEW_LESSON))
        .andExpect(model().attribute(MODEL_LESSON, new Lesson()))
        .andExpect(model().attribute(MODEL_GROUPS, List.of(new Group())))
        .andExpect(model().attribute(MODEL_TEACHERS, List.of(new Teacher())));
        
        verify(teacherService).getAll();
        verify(groupService).getAll();
    }
    
    @Test
    void testShouldRenderLessonUpdatingPageThenRedirectToStatusPage() throws Exception {
        when(lessonService.getById(1)).thenReturn(new Lesson());
        when(teacherService.getAll()).thenReturn(List.of(new Teacher()));
        when(groupService.getAll()).thenReturn(List.of(new Group()));
        
        mockMvc.perform(get("/lessons/1/update"))
        .andExpect(view().name(VIEW_UPDATE_LESSON))
        .andExpect(model().attribute(MODEL_GROUPS, List.of(new Group())))
        .andExpect(model().attribute(MODEL_TEACHERS, List.of(new Teacher())));
        
        verify(lessonService).getById(1);
        verify(teacherService).getAll();
        verify(groupService).getAll();
    }
  
    @Test
    void testShouldRemoveLessonByIdThenRedirectTostatusPage() throws Exception {
        doNothing().when(lessonService).remove(1);
        
        mockMvc.perform(delete("/lessons/1"))
        .andExpect(view().name(VIEW_REDIRECT_TO_ALL_LESSONS));
    }
}
