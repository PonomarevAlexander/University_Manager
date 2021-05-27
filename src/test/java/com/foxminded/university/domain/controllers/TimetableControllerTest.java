package com.foxminded.university.domain.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.LessonService;
import com.foxminded.university.domain.services.TeacherService;
import com.foxminded.university.domain.services.TimetableService;

@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {

    private MockMvc mockMvc;

    private static final String MODEL_TIMETABLE = "timetable";
    private static final String MODEL_ALL_TIMETABLES = "timetables";
    private static final String MODEL_GROUPS = "groups";
    private static final String MODEL_TEACHERS = "teachers";
    private static final String VIEW_ALL_TIMETABLES = "timetable/all-timetables";
    private static final String VIEW_NEW_TIMETABLE = "timetable/new-timetable";
    private static final String VIEW_TIMETABLE = "timetable/timetable";
    private static final String VIEW_UPDATE_TIMETABLE = "timetable/update-timetable";
    private static final String VIEW_REDIRECT_TO_STATUS_TIMETABLES = "redirect:/timetables";
    private static final String VIEW_ADD_LESSON_TO_TIMETABLE = "timetable/add-lesson-to-timetable";

    @Mock
    private TimetableService timetableService;

    @Mock
    private LessonService lessonService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private GroupService groupService;

    @Mock
    private Timetable mockedTimetable;

    @InjectMocks
    private TimetableController timetableController;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(timetableController).build();
    }

    @Test
    void testShouldRenderTimetablesStatusPage() throws Exception {
        when(timetableService.getAll()).thenReturn(List.of(new Timetable()));

        mockMvc.perform(get("/timetables"))
                .andExpect(view().name(VIEW_ALL_TIMETABLES))
                .andExpect(model().attribute(MODEL_ALL_TIMETABLES, List.of(new Timetable())));
    }

    @Test
    void testShouldRenderTimetableProfilePage() throws Exception {
        when(timetableService.getById(1)).thenReturn(mockedTimetable);
        when(mockedTimetable.getCreationDate()).thenReturn(LocalDateTime.parse("2021-01-01T01:01"));

        mockMvc.perform(get("/timetables/1"))
                .andExpect(view().name(VIEW_TIMETABLE))
                .andExpect(model().attribute(MODEL_TIMETABLE, mockedTimetable));
    }
    
    @Test
    void testShouldRenderCreatingNewTimetablePage() throws Exception {
        mockMvc.perform(get("/timetables/new"))
                .andExpect(view().name(VIEW_NEW_TIMETABLE))
                .andExpect(model().attribute(MODEL_TIMETABLE, new Timetable()));
    }
    
    @Test
    void testShouldCreateNewTimetableThenRedirectToStatusPage() throws Exception {
        mockMvc.perform(post("/timetables"))
                .andExpect(view().name(VIEW_REDIRECT_TO_STATUS_TIMETABLES));
    }
    
    @Test
    void testShouldRenderTimetableUpdatingFormPage() throws Exception {
        when(timetableService.getById(1)).thenReturn(mockedTimetable);
        when(mockedTimetable.getSchedule()).thenReturn(List.of(new Lesson()));
        
        mockMvc.perform(get("/timetables/1/update"))
                .andExpect(view().name(VIEW_UPDATE_TIMETABLE))
                .andExpect(model().attribute(MODEL_TIMETABLE, mockedTimetable));
    }

    @Test
    void testShouldRenderAddLessonsPage() throws Exception {
        when(timetableService.getById(1)).thenReturn(mockedTimetable);
        when(teacherService.getAll()).thenReturn(List.of(new Teacher()));
        when(groupService.getAll()).thenReturn(List.of(new Group()));
        
        mockMvc.perform(get("/timetables/1/update/addLesson"))
                .andExpect(view().name(VIEW_ADD_LESSON_TO_TIMETABLE))
                .andExpect(model().attribute(MODEL_TIMETABLE, mockedTimetable))
                .andExpect(model().attribute(MODEL_GROUPS, List.of(new Group())))
                .andExpect(model().attribute(MODEL_TEACHERS, List.of(new Teacher())));
    }
    
    @Test
    void testShouldDeleteTimetableById() throws Exception {
        doNothing().when(timetableService).remove(1);
        
        mockMvc.perform(delete("/timetables/1"))
        .andExpect(view().name(VIEW_REDIRECT_TO_STATUS_TIMETABLES));
    }
}