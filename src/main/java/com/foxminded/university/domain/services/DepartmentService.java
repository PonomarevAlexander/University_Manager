package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.persistence.DepartmentDao;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.TeacherDao;

@Component
public class DepartmentService implements Service<Department> {
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private DepartmentDao departmentDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Override
    public void add(Department department) throws DaoException {
        try {
            LOGGER.debug("creating new department with name={}", department.getName());
            int departmentId = departmentDao.add(department);
            department.getGroupList().forEach(group ->
                    groupDao.setDepartmentToGroup(departmentId, group.getId()));
            department.getTeacherList().forEach(teacher -> 
                    teacherDao.setDepartmentToTeacher(departmentId, teacher.getId()));
            LOGGER.debug("department with name={} was created successfuly", department.getName());
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public Department getById(int id) throws DaoException {
        LOGGER.debug("obtaining department by id={}", id);
        try {
            Department department = departmentDao.get(id);
            department.setGroupList(
                    groupDao.getGroupsByDepartment(department.getId()));
            department.setTeacherList(
                    teacherDao.getTeachersByDepartment(department.getId()));
            LOGGER.debug("department with id={} was prepared and returned", id);
            return department;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public List<Department> getAll() throws DaoException {
        LOGGER.debug("obtaining all departments");
        try {
            List<Department> departments = departmentDao.getAll();
            departments.forEach(department -> {
                department.setGroupList(
                        groupDao.getGroupsByDepartment(department.getId()));
                department.setTeacherList(
                        teacherDao.getTeachersByDepartment(department.getId()));
            });
            LOGGER.debug("students list(size={}) was prepared and returned", departments.size());
            return departments;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Department department) throws DaoException {
        LOGGER.debug("updating department with id={}", department.getId());
        try {
            departmentDao.update(department);
            department.getGroupList().forEach(group -> 
                    groupDao.updateGroupDepartment(department.getId(), group.getId()));
            department.getTeacherList().forEach(teacher -> 
                    teacherDao.updateTeacherDepartment(department.getId(), teacher.getId()));
            LOGGER.debug("department with id={} successfuly updated", department.getId());
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public void remove(int id) throws DaoException {
        LOGGER.debug("removing department with id={}", id);
        try {
            departmentDao.remove(id);
            LOGGER.debug("department with id={} was removed", id);
        } catch (DaoException ex) {
            throw ex;
        }
    }
    
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

}
