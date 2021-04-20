package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.TeacherDao;

public class LessonService implements Service<Lesson> {
    
    private GroupDao groupDao;
    private LessonDao lessonDao;
    private TeacherDao teacherDao;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonService.class);
    private static final String VALID_MESSAGE_OK = "OK";
    private static final String VALID_MESSAGE_NAME = "validation failed! lesson name is null";
    private static final String VALID_MESSAGE_GROUP = "validation failed! lesson group is null";
    private static final String VALID_MESSAGE_TEACHER = "validation failed! lesson teacher is null";
    private static final String VALID_MESSAGE_TIME = "validation failed! lesson start time is null";
    private static final String VALID_MESSAGE_DURATION = "validation failed! lesson duration less 45 min";
    
    @Override
    public void add(Lesson lesson) throws DaoException, ServiceException {
        LOGGER.debug("creating a new lesson with name={}", lesson.getName());
        String message = validateEntity(lesson);
        if (message.equals(VALID_MESSAGE_OK)) {
            try {
                int obtainedId = lessonDao.add(lesson); 
                LOGGER.debug("lesson with name={} was created", obtainedId);
            } catch (DaoException ex) {
                throw ex;
            }
        } else {
            throw new ServiceException(message);
        }
    }

    @Override
    public Lesson getById(int id) throws DaoException {
        LOGGER.debug("obtaining a lesson by id={}", id);
        try {
            Lesson lesson = lessonDao.get(id);
            lesson.setTeacher(
                    teacherDao.getTeacherByLessonId(id));
            lesson.setGroup(
                    groupDao.getGroupByLesson(id));
            LOGGER.debug("Lesson with id={} was prepared and returned", lesson.getId());
            return lesson;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public List<Lesson> getAll() throws DaoException {
        LOGGER.debug("obtaining list of all lessons");
        try {
            List<Lesson> lessonsList = lessonDao.getAll();
            lessonsList.forEach(lesson -> {
                lesson.setTeacher(
                        teacherDao.getTeacherByLessonId(lesson.getId()));
                lesson.setGroup(
                        groupDao.getGroupByLesson(lesson.getId()));
            });
            LOGGER.debug("all lessons obtained and returned");
            return lessonsList;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Lesson lesson) throws DaoException, ServiceException {
        LOGGER.debug("updating lesson with id={}", lesson.getId());
        String validMessage = validateEntity(lesson);
        if (validMessage.equals(VALID_MESSAGE_OK)) {
            try {
                lessonDao.update(lesson);
                LOGGER.debug("lesson with id={} was updated", lesson.getId());
            } catch (DaoException ex) {
                throw ex;
            }
        } else {
            throw new ServiceException(validMessage);
        }
    }

    @Override
    public void remove(int id) throws DaoException {
        LOGGER.debug("removing lesson with id={}", id);
        try {
            lessonDao.remove(id);
            LOGGER.debug("lesson with id={} was removed", id);
        } catch (DaoException ex) {
            throw ex;
        }
    }
    
    public List<Lesson> getLessonsByTimetable(Timetable timetable) {
        LOGGER.debug("obtaining lessons by timetable(id={})", timetable.getId());
        try {
            List<Lesson> lessonsList = lessonDao.getLessonsOfTimetable(timetable.getId());
            LOGGER.debug("lessons list was obtained and returned");
            return lessonsList;
        } catch (DaoException ex) {
            throw ex;
        }
    }
    
    public String validateEntity(Lesson lesson) {
        LOGGER.debug("begin validation");
        String message = VALID_MESSAGE_OK;
        if (lesson.getName() == null) {
            message = VALID_MESSAGE_NAME;
        }
        if (lesson.getGroup() == null) {
            message = VALID_MESSAGE_GROUP;
        }
        if (lesson.getTeacher() == null) {
            message = VALID_MESSAGE_TEACHER;
        }
        if (lesson.getLessonDurationSecond() < 2700) {
            message = VALID_MESSAGE_DURATION;
        }
        if (lesson.getStartTime() == null) {
            message = VALID_MESSAGE_TIME;
        }
        LOGGER.debug("validation passed");
        return message;
    }
    
    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setLessonDao(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }
    
    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }
}
