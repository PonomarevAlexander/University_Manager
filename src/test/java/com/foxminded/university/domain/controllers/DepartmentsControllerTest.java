package com.foxminded.university.domain.controllers;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.domain.models.Department;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.services.DepartmentService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class DepartmentsControllerTest {

    private MockMvc mockMvc;
    
    private static final String MODEL_NAME_DEPARTMENT = "department";
    private static final String MODEL_ALL_DEPARTMENTS = "departments";
    private static final String MODEL_NAME_GROUPS = "groups";
    private static final String MODEL_NAME_TEACHERS = "teachers";
    private static final String VIEW_ALL_DEPARTMENTS = "department/all_departments";
    private static final String VIEW_DEPARTMENT = "department/department";
    private static final String VIEW_ADD_DEPARTMENT = "department/add_department";
    private static final String VIEW_UPDATE_DEPARTMENT = "department/update_department";
    private static final String VIEW_REDIRECT_TO_ALL_DEPARTMENTS = "redirect:/departments";
    private static final String NAME = "name";

    @Mock
    private DepartmentService mockedDepartmentService;

    @InjectMocks
    private DepartmentsController departmentsController;

    @BeforeEach
    public void setupMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentsController).build();
    }

    @Test
    void testShouldRenderPage_ListOfAllDepartments() throws Exception {
        when(mockedDepartmentService.getAll()).thenReturn(List.of(new Department(NAME)));

        mockMvc.perform(get("/departments/"))
                .andExpect(view().name(VIEW_ALL_DEPARTMENTS))
                .andExpect(model().attribute(MODEL_ALL_DEPARTMENTS, List.of(new Department(NAME))));

        verify(mockedDepartmentService).getAll();
    }

    @Test
    void testShouldRenderPage_DepartmentWithId1() throws Exception {
        Department dept = new Department(1, NAME, List.of(new Teacher()), List.of(new Group()));
        when(mockedDepartmentService.getById(1)).thenReturn(dept);

        mockMvc.perform(get("/departments/1"))
                .andExpect(view().name(VIEW_DEPARTMENT))
                .andExpect(model().attribute(MODEL_NAME_DEPARTMENT, dept))
                .andExpect(model().attribute(MODEL_NAME_GROUPS, List.of(new Group())))
                .andExpect(model().attribute(MODEL_NAME_TEACHERS, List.of(new Teacher())));

        verify(mockedDepartmentService).getById(1);
    }

    @Test
    void testShouldRenderPage_NewDepartmentForm() throws Exception {
        mockMvc.perform(get("/departments/new"))
                .andExpect(view().name(VIEW_ADD_DEPARTMENT));
    }

    @Test
    void testShouldCreateNewDepartmentAndRedirectToAllDepartmentsPage() throws Exception {
        when(mockedDepartmentService.add(new Department(0, NAME, null, null))).thenReturn(0);

        mockMvc.perform(post("/departments?name=name"))
                .andExpect(view().name(VIEW_REDIRECT_TO_ALL_DEPARTMENTS))
                .andExpect(model().attribute(MODEL_NAME_DEPARTMENT, new Department(0, NAME, null, null)));
    }

    @Test
    void testShouldUpdateDepartmentAndRedirectToAllDepartmentsPage() throws Exception {
        Department dept = new Department();
        dept.setId(1);
        doNothing().when(mockedDepartmentService).update(dept);

        mockMvc.perform(patch("/departments/1"))
                .andExpect(view().name(VIEW_REDIRECT_TO_ALL_DEPARTMENTS))
                .andExpect(model().attribute(MODEL_NAME_DEPARTMENT, dept));

        verify(mockedDepartmentService).update(dept);
    }

    @Test
    void testShouldRenderUpdatingFormForDepartment() throws Exception {
        mockMvc.perform(get("/departments/1/update"))
                .andExpect(view().name(VIEW_UPDATE_DEPARTMENT));
    }

    @Test
    void testShouldRemoveDepartmentAndRedirectToAllDepartments() throws Exception {
        doNothing().when(mockedDepartmentService).remove(1);

        mockMvc.perform(delete("/departments/1"))
                .andExpect(view().name(VIEW_REDIRECT_TO_ALL_DEPARTMENTS));

        verify(mockedDepartmentService).remove(1);
    }

}
