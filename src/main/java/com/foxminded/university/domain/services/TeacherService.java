package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.HibernateException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.persistence.TeacherRepository;

@Component
public class TeacherService implements UniversityService<Teacher> {

    private TeacherRepository teacherRepository;

    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! teacher name is null";
    private static final String EXCEPTION_NOT_VALID_LAST_NAME = "validation failed! teacher last name is null";
    private static final String EXCEPTION_ADD = "Failed to creating new teacher!";
    private static final String EXCEPTION_GET = "Failed to receiving a teacher(id=%). Reason is ";
    private static final String EXCEPTION_GET_LIST = "Failed to receiving teachers list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the teacher(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the teacher(id=%d). Reason is ";

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void add(Teacher teacher) throws ServiceException {
        validateEntity(teacher);
        try {
            teacherRepository.save(teacher);
        } catch (HibernateException ex) {
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Teacher getById(int id) {
        try {
            Teacher teacher = teacherRepository.findById(id).get();
            return teacher;
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }

    }

    @Override
    public List<Teacher> getAll() {
        try {
            List<Teacher> teachersList = (List<Teacher>) teacherRepository.findAll();
            return teachersList;
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET_LIST) +  ex.getMessage());
        }
    }

    @Override
    public void update(Teacher teacher) throws ServiceException {
        validateEntity(teacher);
        try {
            teacherRepository.save(teacher);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_UPDATE,teacher.getId()) +  ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        try {
            teacherRepository.deleteById(id);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) +  ex.getMessage());
        }
    }

    private void validateEntity(Teacher teacher) {
        if (teacher.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (teacher.getLastName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_LAST_NAME);
        }
    }

}
