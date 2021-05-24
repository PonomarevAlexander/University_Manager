package com.foxminded.university.domain.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.domain.services.TeacherService;
import com.foxminded.university.domain.services.TimetableService;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private MockMvc mockMvc;

    private static final String MODEL_ALL_TEACHERS = "teachers";
    private static final String MODEL_TEACHER = "teacher";
    private static final String MODEL_TIMETABLES = "timetables";
    private static final String VIEW_ALL_TEACHERS = "teacher/all_teachers";
    private static final String VIEW_TEACHER = "teacher/teacher";
    private static final String VIEW_NEW_TEACHER = "teacher/new_teacher";
    private static final String VIEW_UPDATE_TEACHER = "teacher/update_teacher";
    private static final String VIEW_REDIRECT_TO_TICHERS = "redirect:/teachers";

    @Mock
    private TeacherService teacherService;

    @Mock
    private TimetableService timetableService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void testShouldRenderTeachersStatusPage() throws Exception {
        when(teacherService.getAll()).thenReturn(List.of(new Teacher()));

        mockMvc.perform(get("/teachers"))
                .andExpect(view().name(VIEW_ALL_TEACHERS))
                .andExpect(model().attribute(MODEL_ALL_TEACHERS, List.of(new Teacher())));

        verify(teacherService).getAll();
    }

    @Test
    void testShouldRenderTeacherProfilePage() throws Exception {
        when(teacherService.getById(1)).thenReturn(new Teacher());

        mockMvc.perform(get("/teachers/1"))
                .andExpect(view().name(VIEW_TEACHER))
                .andExpect(model().attribute(MODEL_TEACHER, new Teacher()));

        verify(teacherService).getById(1);
    }

    @Test
    void testShouldRenderTeacherCreatingFormPage() throws Exception {
        when(timetableService.getAll()).thenReturn(List.of(new Timetable()));

        mockMvc.perform(get("/teachers/new"))
                .andExpect(view().name(VIEW_NEW_TEACHER))
                .andExpect(model().attribute(MODEL_TIMETABLES, List.of(new Timetable())))
                .andExpect(model().attribute(MODEL_TEACHER, new Teacher()));

        verify(timetableService).getAll();
    }

    @Test
    void testShouldCreateNewTeacherThenRedirect() throws Exception {
        Teacher teacher = new Teacher("name", "lastName");
        when(teacherService.add(teacher)).thenReturn(1);

        mockMvc.perform(post("/teachers?name=name&lastName=lastName"))
                .andExpect(view().name(VIEW_REDIRECT_TO_TICHERS))
                .andExpect(model().attribute(MODEL_TEACHER, teacher));
    }

    @Test
    void testShouldRenderTeacherUpdatingPage() throws Exception {
        Teacher teacher = new Teacher("name", "lastName");
        when(teacherService.getById(1)).thenReturn(teacher);
        when(timetableService.getAll()).thenReturn(List.of(new Timetable()));

        mockMvc.perform(get("/teachers/1/update"))
                .andExpect(view().name(VIEW_UPDATE_TEACHER));
    }

    @Test
    void testShouldUpdateTeacherThenRedirect() throws Exception {
        Teacher teacher = new Teacher("name", "lastName");
        teacher.setId(1);
        doNothing().when(teacherService).update(teacher);

        mockMvc.perform(patch("/teachers/1?name=name&lastName=lastName"))
                .andExpect(view().name(VIEW_REDIRECT_TO_TICHERS))
                .andExpect(model().attribute(MODEL_TEACHER, teacher));
    }

    @Test
    void testShouldRemoveTeacherThenRedirect() throws Exception {
        doNothing().when(teacherService).remove(1);

        mockMvc.perform(delete("/teachers/1"))
                .andExpect(view().name(VIEW_REDIRECT_TO_TICHERS));
    }

}
