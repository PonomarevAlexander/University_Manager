package com.foxminded.university.domain.services;

import java.util.List;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.persistence.UniversityRepository;
import com.foxminded.university.persistence.GenericHibernateRepositoryImpl;

@Service
public class DepartmentService implements UniversityService<Department> {

    private UniversityRepository<Department> departmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! department name is null";
    private static final String EXCEPTION_ADD = "Failed to creating new department!";
    private static final String EXCEPTION_GET = "department with id - %d not found";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all departments list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the department(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the department(id=%d). Reason is ";

    @Override
    public void add(Department department) {
        LOGGER.debug("creating new department");
        validateEntity(department);
        try {
            departmentRepository.add(department);
            LOGGER.debug("department with name={} was created successfuly", department.getName());
        } catch (HibernateException ex) {
            LOGGER.error("new department was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Department getById(int id){
        LOGGER.debug("obtaining department by id={}", id);
        try {
            Department department = departmentRepository.get(id);
            LOGGER.debug("department with id={} was prepared and returned", id);
            return department;
        } catch (HibernateException ex) {
            LOGGER.error("department with id={} not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id));
        }
    }

    @Override
    public List<Department> getAll() {
        LOGGER.debug("obtaining all departments");
        try {
            List<Department> departments = departmentRepository.getAll();
            LOGGER.debug("departments list(size={}) was prepared and returned", departments.size());
            return departments;
        } catch (HibernateException ex) {
            LOGGER.error("no one department not found");
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Department department) throws ServiceException {
        LOGGER.debug("updating department");
        validateEntity(department);
        try {
            departmentRepository.update(department);
            LOGGER.debug("department with id={} successfuly updated", department.getId());
        } catch (HibernateException ex) {
            LOGGER.error("department with id={} was not updated! group or teacher related to the department not found", department.getId());
            throw new ServiceException(String.format(EXCEPTION_UPDATE, department.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing department with id={}", id);
        try {
            departmentRepository.remove(id);
            LOGGER.debug("department with id={} was removed", id);
        } catch (HibernateException ex) {
            LOGGER.error("department with id={} was not removed! Department not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Department department) {
        LOGGER.debug("begin validation");
        if (department.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        LOGGER.debug("validation passed");
    }

    @Autowired
    public void setDepartmentDao(GenericHibernateRepositoryImpl<Department> genericHibernateRepository) {
        genericHibernateRepository.setClazz(Department.class);
        this.departmentRepository = genericHibernateRepository;
    }

}
