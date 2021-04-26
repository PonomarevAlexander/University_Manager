package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.EntityNotCreatedException;
import com.foxminded.university.domain.exceptions.EntityNotFoundException;
import com.foxminded.university.domain.exceptions.ServiceException;
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
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! department name is null";
    private static final String EXCEPTION_NOT_VALID_TEACHERS_LIST = "validation failed! department teachers list is null";
    private static final String EXCEPTION_NOT_VALID_GROUPS_LIST = "validation failed! department groups list is null";
    private static final String EXCEPTION_ADD = "Failed to creating new department!";
    private static final String EXCEPTION_GET = "Failed to receiving a department(id=%). Reason is ";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all departments list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the department(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the department(id=%d). Reason is ";

    @Override
    public void add(Department department) throws ServiceException {
        LOGGER.debug("creating new department");
        validateEntity(department);
        try {
            int departmentId = departmentDao.add(department);
            department.getGroupList().forEach(group -> groupDao.setDepartmentToGroup(departmentId, group.getId()));
            department.getTeacherList()
                    .forEach(teacher -> teacherDao.setDepartmentToTeacher(departmentId, teacher.getId()));
            LOGGER.debug("department with name={} was created successfuly", department.getName());
        } catch (EntityNotCreatedException ex) {
            LOGGER.error("new department was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Department getById(int id) {
        LOGGER.debug("obtaining department by id={}", id);
        try {
            Department department = departmentDao.get(id);
            department.setGroupList(groupDao.getGroupsByDepartment(department.getId()));
            department.setTeacherList(teacherDao.getTeachersByDepartment(department.getId()));
            LOGGER.debug("department with id={} was prepared and returned", id);
            return department;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("department with id={} not found or group or teacher related to the department not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Department> getAll() {
        LOGGER.debug("obtaining all departments");
        try {
            List<Department> departments = departmentDao.getAll();
            departments.forEach(department -> {
                department.setGroupList(groupDao.getGroupsByDepartment(department.getId()));
                department.setTeacherList(teacherDao.getTeachersByDepartment(department.getId()));
            });
            LOGGER.debug("departments list(size={}) was prepared and returned", departments.size());
            return departments;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("no one department not found");
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Department department) throws ServiceException {
        LOGGER.debug("updating department");
        validateEntity(department);
        try {
            departmentDao.update(department);
            department.getGroupList()
                    .forEach(group -> groupDao.updateGroupDepartment(department.getId(), group.getId()));
            department.getTeacherList()
                    .forEach(teacher -> teacherDao.updateTeacherDepartment(department.getId(), teacher.getId()));
            LOGGER.debug("department with id={} successfuly updated", department.getId());
        } catch (EntityNotFoundException ex) {
            LOGGER.error("department with id={} was not updated! group or teacher related to the department not found", department.getId());
            throw new ServiceException(String.format(EXCEPTION_UPDATE, department.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing department with id={}", id);
        try {
            departmentDao.remove(id);
            LOGGER.debug("department with id={} was removed", id);
        } catch (EntityNotFoundException ex) {
            LOGGER.error("department with id={} was not removed! Department not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Department department) {
        LOGGER.debug("begin validation");
        if (department.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (department.getTeacherList() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_TEACHERS_LIST);
        }
        if (department.getGroupList() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_GROUPS_LIST);
        }
        LOGGER.debug("validation passed");
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
