package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Student;
import com.foxminded.university.persistence.Dao;
import com.foxminded.university.persistence.GenericHibernateDao;

@Component
public class StudentService implements ServiceInterface<Student> {

    private Dao<Student> studentDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! student name is null";
    private static final String EXCEPTION_NOT_VALID_LAST_NAME = "validation failed! student last name is null";
    private static final String EXCEPTION_NOT_VALID_AGE = "validation failed! student age is 0 or less";
    private static final String EXCEPTION_NOT_VALID_GROUP = "validation failed! student group is null";
    private static final String EXCEPTION_ADD = "Failed to creating new student!";
    private static final String EXCEPTION_GET = "Failed to receiving a student(id=%). Reason is ";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all students list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the student(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the student(id=%d). Reason is ";

    @Override
    public void add(Student student) {
        LOGGER.debug("creating new student");
        validateEntity(student);
        try {
            studentDao.add(student);
            LOGGER.debug("new student was created");
        } catch (DaoException ex) {
            LOGGER.error("new student was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Student getById(int id) {
        LOGGER.debug("getting student by id={}", id);
        try {
            Student student = studentDao.get(id);
            LOGGER.debug("student with id={} was prepared and returned", student.getId());
            return student;
        } catch (DaoException ex) {
            LOGGER.error("student with id={} not found or his group not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("getting all students");
        try {
            List<Student> studentsList = studentDao.getAll();
            LOGGER.debug("students list(size={}) was prepared and returned", studentsList.size());
            return studentsList;
        } catch (DaoException ex) {
            LOGGER.error("no one student not found");
            throw new ServiceException(String.format(EXCEPTION_GET_ALL) + ex.getMessage());
        }
    }

    @Override
    public void update(Student student) throws ServiceException {
        LOGGER.debug("updating student");
        validateEntity(student);
        try {
            studentDao.update(student);
            LOGGER.debug("student with id={} successfully updated", student.getId());
        } catch (DaoException ex) {
            LOGGER.error("student updating failed!");
            throw new ServiceException(String.format(EXCEPTION_UPDATE, student.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing student");
        try {
            studentDao.remove(id);
            LOGGER.debug("student with id={} has been deleted", id);
        } catch (DaoException ex) {
            LOGGER.error("student with id={} was not removed! Student not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Student student) 
    {
        LOGGER.debug("begin validation");
        if (student.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (student.getLastName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_LAST_NAME);
        }
        if (student.getAge() <= 0) {
            throw new ServiceException(EXCEPTION_NOT_VALID_AGE);
        }
        if (student.getGroup() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_GROUP);
        }
        LOGGER.debug("validation passed");
    }

    @Autowired
    public void setStudentDao(GenericHibernateDao<Student> studentDao) {
        studentDao.setClazz(Student.class);
        this.studentDao = studentDao;
    }
}
