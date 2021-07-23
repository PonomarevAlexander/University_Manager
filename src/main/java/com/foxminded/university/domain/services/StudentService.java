package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.HibernateException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Student;
import com.foxminded.university.persistence.StudentRepository;

@Component
public class StudentService implements UniversityService<Student> {

    private StudentRepository studentRepository;

    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! student name is null";
    private static final String EXCEPTION_NOT_VALID_LAST_NAME = "validation failed! student last name is null";
    private static final String EXCEPTION_NOT_VALID_AGE = "validation failed! student age is 0 or less";
    private static final String EXCEPTION_NOT_VALID_GROUP = "validation failed! student group is null";
    private static final String EXCEPTION_ADD = "Failed to creating new student!";
    private static final String EXCEPTION_GET = "Failed to receiving a student(id=%). Reason is ";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all students list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the student(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the student(id=%d). Reason is ";

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void add(Student student) {
        validateEntity(student);
        try {
            studentRepository.save(student);
        } catch (HibernateException ex) {
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Student getById(int id) {
        try {
            return studentRepository.findById(id).get();
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Student> getAll() {
        try {
            List<Student> studentsList = (List<Student>) studentRepository.findAll();
            return studentsList;
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET_ALL) + ex.getMessage());
        }
    }

    @Override
    public void update(Student student) throws ServiceException {
        validateEntity(student);
        try {
            studentRepository.save(student);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_UPDATE, student.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        try {
            studentRepository.deleteById(id);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Student student) 
    {
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
    }
}
