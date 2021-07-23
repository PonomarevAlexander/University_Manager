package com.foxminded.university.domain.services;

import java.util.List;
import java.util.Optional;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.persistence.DepartmentsRepository;

@Service
public class DepartmentService implements UniversityService<Department> {

    private DepartmentsRepository departmentRepository;

    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! department name is null";
    private static final String EXCEPTION_ADD = "Failed to creating new department!";
    private static final String EXCEPTION_GET = "department with id - %d not found";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all departments list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the department(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the department(id=%d). Reason is ";

    @Autowired
    public DepartmentService(DepartmentsRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void add(Department department) {
        validateEntity(department);
        try {
            departmentRepository.save(department);
        } catch (HibernateException ex) {
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Department getById(int id){
        try {
            Optional<Department> optional = departmentRepository.findById(id);
            return optional.get();
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET, id));
        }
    }

    @Override
    public List<Department> getAll() {
        try {
            List<Department> departments = (List<Department>) departmentRepository.findAll();
            return departments;
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Department department) throws ServiceException {
        validateEntity(department);
        try {
            departmentRepository.save(department);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_UPDATE, department.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        try {
            departmentRepository.deleteById(id);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Department department) {
        if (department.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
    }

}
