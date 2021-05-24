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
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Student;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;
    
    private static final String MODEL_STUDENT = "student";
    private static final String MODEL_ALL_STUDENTS = "students";
    private static final String MODEL_GROUPS = "groups";
    private static final String VIEW_ALL_STUDENTS = "student/all_students";
    private static final String VIEW_STUDENT = "student/student";
    private static final String VIEW_NEW_STUDENT = "student/new_student";
    private static final String VIEW_UPDATE_STUDENT = "student/update_student";
    private static final String VIEW_REDIRECT_TO_STATUS = "redirect:/students";
    
    @Mock
    private StudentService studentService;

    @Mock
    private GroupService groupService;
    
    @InjectMocks
    private StudentController studentController;
    
    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }
    
    @Test
    void testShouldRenderStudentsStatusPage() throws Exception {
        when(studentService.getAll()).thenReturn(List.of(new Student()));
        
        mockMvc.perform(get("/students"))
                .andExpect(view().name(VIEW_ALL_STUDENTS))
                .andExpect(model().attribute(MODEL_ALL_STUDENTS, List.of(new Student())));
    }
    
    @Test
    void testShouldRenderProfileStudentPage() throws Exception {
        when(studentService.getById(1)).thenReturn(new Student());
        
        mockMvc.perform(get("/students/1"))
                .andExpect(view().name(VIEW_STUDENT))
                .andExpect(model().attribute(MODEL_STUDENT, new Student()));
    }
    
    @Test
    void testShouldRenderFormPageForCreatingNewStudent() throws Exception {
        when(groupService.getAll()).thenReturn(List.of(new Group()));
        
        mockMvc.perform(get("/students/new"))
                .andExpect(view().name(VIEW_NEW_STUDENT))
                .andExpect(model().attribute(MODEL_STUDENT, new Student()))
                .andExpect(model().attribute(MODEL_GROUPS, List.of(new Group())));
    }
    
    @Test
    void testShouldCreateNewStudentThenRedirect() throws Exception {
        Student student = new Student("name", "lastName", 20);
        when(studentService.add(student)).thenReturn(1);
        
        mockMvc.perform(post("/students?name=name&lastName=lastName&age=20"))
                .andExpect(view().name(VIEW_REDIRECT_TO_STATUS))
                .andExpect(model().attribute(MODEL_STUDENT, student));
    }
    
    @Test
    void testShouldRenderUpdatingFormPage() throws Exception {
        Student student = new Student("name", "lastName", 20);
        when(studentService.getById(1)).thenReturn(student);
        when(groupService.getAll()).thenReturn(List.of(new Group()));
        
        mockMvc.perform(get("/students/1/update"))
                .andExpect(view().name(VIEW_UPDATE_STUDENT));
    }
    
    @Test
    void testShouldUpdateStudentThenRedirect() throws Exception {
        Student student = new Student();
        student.setId(1);
        doNothing().when(studentService).update(student);
        
        mockMvc.perform(patch("/students/1"))
                .andExpect(view().name(VIEW_REDIRECT_TO_STATUS))
                .andExpect(model().attribute(MODEL_STUDENT, student));
    }
    
    @Test
    void testShouldRemovingStudentByIdThenRedirect() throws Exception {
        doNothing().when(studentService).remove(1);
        
        mockMvc.perform(delete("/students/1"))
                .andExpect(view().name(VIEW_REDIRECT_TO_STATUS));
    }
}
