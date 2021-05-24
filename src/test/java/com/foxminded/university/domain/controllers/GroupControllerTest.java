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
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.domain.services.DepartmentService;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.TeacherService;
import com.foxminded.university.domain.services.TimetableService;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;

    private static final String MODEL_GROUPS = "groups";
    private static final String MODEL_GROUP = "group";
    private static final String MODEL_TIMETABLES = "timetables";
    private static final String MODEL_DEPARTMENTS = "departments";
    private static final String MODEL_TEACHERS = "teachers";
    private static final String VIEW_ALL_GROUP = "group/all_groups";
    private static final String VIEW_ONE_GROUP = "group/group";
    private static final String VIEW_NEW_GROUP = "group/new_group";
    private static final String VIEW_UPDATE_GROUP = "group/update_group";
    private static final String VIEW_REDIRECT_TO_ALL_GROUP = "redirect:/groups";
    private static final String NAME = "name";
    private final Group testGroup = new Group(1, NAME, null, null, null, null);

    @Mock
    private GroupService groupService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TimetableService timetableService;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setupMock() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void testShouldRenderStatusPageWithAllGroups() throws Exception {
        when(groupService.getAll()).thenReturn(List.of(testGroup));

        mockMvc.perform(get("/groups"))
                .andExpect(view().name(VIEW_ALL_GROUP))
                .andExpect(model().attribute(MODEL_GROUPS, List.of(testGroup)));
        
        verify(groupService).getAll();
    }

    @Test
    void testShouldRenderGroupProfileByIdPage() throws Exception {
        when(groupService.getById(1)).thenReturn(testGroup);

        mockMvc.perform(get("/groups/1"))
                .andExpect(view().name(VIEW_ONE_GROUP))
                .andExpect(model().attribute(MODEL_GROUP, testGroup));
    }

    @Test
    void testShouldRenderFormPageToCreateNewGroup() throws Exception {
        mockMvc.perform(get("/groups/new"))
                .andExpect(view().name(VIEW_NEW_GROUP));
        
        verify(teacherService).getAll();
        verify(timetableService).getAll();
        verify(departmentService).getAll();
    }

    @Test
    void testShouldCreateNewGroupAndRedirectToStatusPage() throws Exception {
        when(groupService.add(testGroup)).thenReturn(1);

        mockMvc.perform(post("/groups?id=1&name=name"))
                .andExpect(view().name(VIEW_REDIRECT_TO_ALL_GROUP))
                .andExpect(model().attribute(MODEL_GROUP, testGroup));
        
        verify(groupService).add(testGroup);
    }

    @Test
    void testShouldRenderGroupUpdatingPage() throws Exception {
        when(groupService.getById(1)).thenReturn(testGroup);
        when(teacherService.getAll()).thenReturn(List.of(new Teacher()));
        when(timetableService.getAll()).thenReturn(List.of(new Timetable()));
        when(departmentService.getAll()).thenReturn(List.of(new Department()));

        mockMvc.perform(get("/groups/1/update")).andExpect(view().name(VIEW_UPDATE_GROUP))
                .andExpect(model().attribute(MODEL_GROUP, testGroup))
                .andExpect(model().attribute(MODEL_TEACHERS, List.of(new Teacher())))
                .andExpect(model().attribute(MODEL_TIMETABLES, List.of(new Timetable())))
                .andExpect(model().attribute(MODEL_DEPARTMENTS, List.of(new Department())));
        
        verify(groupService).getById(1);
        verify(teacherService).getAll();
        verify(timetableService).getAll();
        verify(departmentService).getAll();
        
    }

    @Test
    void testShouldUpdateGroupAndRedirectToGroupsStatusPage() throws Exception {
        doNothing().when(groupService).update(testGroup);
        
        mockMvc.perform(patch("/groups/1?id=1&name=name"))
        .andExpect(view().name(VIEW_REDIRECT_TO_ALL_GROUP))
        .andExpect(model().attribute(MODEL_GROUP, testGroup));
        
        verify(groupService).update(testGroup);
    }
    
    @Test
    void testShouldRemoveGroupAndRedirectToGroupStatusPage() throws Exception {
        doNothing().when(groupService).remove(1);
        
        mockMvc.perform(delete("/groups/1"))
        .andExpect(view().name(VIEW_REDIRECT_TO_ALL_GROUP));
        
        verify(groupService).remove(1);
    }
}
