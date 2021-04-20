package com.foxminded.university.domain.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@Component
public class TeacherService implements Service<Teacher> {
    
    private TeacherDao teacherDao;
    private TimetableDao timetableDao;
    private LessonDao lessonDao;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);
    private static final String VALID_MESSAGE_OK = "OK";
    private static final String VALID_MESSAGE_NAME = "validation failed! teacher name is null";
    private static final String VALID_MESSAGE_LAST_NAME = "validation failed! teacher last name is null";

    @Override
    public void add(Teacher teacher) throws DaoException, ServiceException {
        LOGGER.debug("creating a new teacher with name={}, last name={}", teacher.getName(), teacher.getLastName());
        String message = validateEntity(teacher);
        if (message.equals(VALID_MESSAGE_OK)) {
            try {
                int receivedId = teacherDao.add(teacher);
                if (teacher.getTimetable() != null) {
                    timetableDao.setTimetableToTeacher(teacher.getTimetable().getId(), receivedId);
                    LOGGER.debug("new teacher({} {}) was created! Timetable(id={}) was assigned to the teacher",
                            teacher.getName(), teacher.getLastName(), teacher.getTimetable().getId());
                } else {
                    LOGGER.debug("new teacher({} {}) was created! Timetable did not assigned", teacher.getName(), teacher.getLastName());
                }
            } catch (DaoException ex) {
                throw ex;
            }
        } else {
            throw new ServiceException(message);
        }
    }
    
    @Override
    public Teacher getById(int id) throws DaoException {
        LOGGER.debug("obtaining a teacher by id={}", id);
        try {
            Teacher teacher = teacherDao.get(id);
            Timetable timetable = timetableDao.getTimetableRelatedTeacher(teacher.getId());
            List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
            timetable.setSchedule(lessons);
            teacher.setTimetable(timetable);
            LOGGER.debug("Teacher with id={} was prepared and returned", teacher.getId());
            return teacher;
        } catch (DaoException ex) {
            throw ex;
        }
        
    }

    @Override
    public List<Teacher> getAll() throws DaoException {
        LOGGER.debug("obtaining list of all teachers");
        try {
            List<Teacher> teachersList = teacherDao.getAll();
            teachersList.forEach(teacher -> {
                Timetable timetable = timetableDao.getTimetableRelatedTeacher(teacher.getId());
                List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
                timetable.setSchedule(lessons);
                teacher.setTimetable(timetable);
            });
            LOGGER.debug("all teachers obtained and returned");
            return teachersList;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Teacher teacher) {
        LOGGER.debug("updating teacher with id={}", teacher.getId());
        String message = validateEntity(teacher);
        if (message.equals(VALID_MESSAGE_OK)) {
            try {
                teacherDao.update(teacher);
                timetableDao.updateTimetableRelatedTeacher(teacher.getTimetable().getId(), teacher.getId());
                LOGGER.debug("teacher with id={} was updated", teacher.getId());
            } catch (DaoException ex) {
                throw ex;
            }
        } else {
            throw new ServiceException(message);
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing teacher with id={}", id);
        try {
            teacherDao.remove(id);
            LOGGER.debug("teacher with id={} was removed", id);
        } catch (DaoException ex) {
            throw ex;
        }
    }
    
    public String validateEntity(Teacher teacher) {
        LOGGER.debug("begin validation");
        String message = VALID_MESSAGE_OK;
        if (teacher.getName() == null) {
            message = VALID_MESSAGE_NAME;
        }
        if (teacher.getLastName() == null) {
            message = VALID_MESSAGE_LAST_NAME;
        }
        LOGGER.debug("validation passed");
        return message;
    }
    
    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setTimetableDao(TimetableDao timetableDao) {
        this.timetableDao = timetableDao;
    }

    @Autowired
    public void setLessonDao(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

}
