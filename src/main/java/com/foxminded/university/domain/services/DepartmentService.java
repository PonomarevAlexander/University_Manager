package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.persistence.DepartmentDao;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.TeacherDao;

@Component
public class DepartmentService implements Service<Department> {
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private DepartmentDao departmentDao;

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Autowired
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public void add(Department department) {
        int departmentId = departmentDao.add(department);
        department.getGroupList().forEach(group ->
                groupDao.setDepartmentToGroup(departmentId, group.getId()));
        department.getTeacherList().forEach(teacher -> 
                teacherDao.setDepartmentToTeacher(departmentId, teacher.getId()));
    }

    @Override
    public Department getById(int id) {
        Department department = departmentDao.get(id);
        department.setGroupList(
                groupDao.getGroupsByDepartment(department.getId()));
        department.setTeacherList(
                teacherDao.getTeachersByDepartment(department.getId()));
        return department;
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = departmentDao.getAll();
        departments.forEach(department -> {
            department.setGroupList(
                    groupDao.getGroupsByDepartment(department.getId()));
            department.setTeacherList(
                    teacherDao.getTeachersByDepartment(department.getId()));
        });
        return departments;
    }

    @Override
    public void update(Department department) {
        departmentDao.update(department);
        department.getGroupList().forEach(group -> 
                groupDao.updateGroupDepartment(department.getId(), group.getId()));
        department.getTeacherList().forEach(teacher -> 
                teacherDao.updateTeacherDepartment(department.getId(), teacher.getId()));
    }

    @Override
    public void remove(int id) {
        departmentDao.remove(id);
    }

}
