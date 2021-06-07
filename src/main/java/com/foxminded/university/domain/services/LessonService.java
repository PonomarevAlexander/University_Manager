package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.persistence.Dao;
import com.foxminded.university.persistence.GenericHibernateDao;

@Component
public class LessonService implements ServiceInterface<Lesson> {

    private Dao<Lesson> lessonDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonService.class);
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! lesson name is null";
    private static final String EXCEPTION_NOT_VALID_GROUP = "validation failed! lesson group is null";
    private static final String EXCEPTION_NOT_VALID_TEACHER = "validation failed! lesson teacher is null";
    private static final String EXCEPTION_NOT_VALID_START_TIME = "validation failed! lesson start time is null";
    private static final String EXCEPTION_NOT_VALID_DURATION = "validation failed! lesson duration less 45 min";
    private static final String EXCEPTION_ADD = "Failed to creating new lesson!";
    private static final String EXCEPTION_GET = "Failed to receiving a lesson(id=%). Reason is ";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all lessons list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the lesson(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the lesson(id=%d). Reason is ";

    @Override
    public void add(Lesson lesson) {
        LOGGER.debug("creating a new lesson with name={}", lesson.getName());
        validateEntity(lesson);
        try {
            lessonDao.add(lesson);
            LOGGER.debug("lesson with name={} was created", lesson.getName());
        } catch (DaoException ex) {
            LOGGER.error("new lesson was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Lesson getById(int id) {
        LOGGER.debug("obtaining a lesson by id={}", id);
        try {
            Lesson lesson = lessonDao.get(id);
            LOGGER.debug("Lesson with id={} was prepared and returned", lesson.getId());
            return lesson;
        } catch (DaoException ex) {
            LOGGER.error("lesson with id={} not found or teacher, group related to the group not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Lesson> getAll() {
        LOGGER.debug("obtaining list of all lessons");
        try {
            List<Lesson> lessonsList = lessonDao.getAll();
            LOGGER.debug("all lessons obtained and returned");
            return lessonsList;
        } catch (DaoException ex) {
            LOGGER.error("no one lesson not found");
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Lesson lesson) throws ServiceException {
        LOGGER.debug("updating lesson with id={}", lesson.getId());
        validateEntity(lesson);
        try {
            lessonDao.update(lesson);
            LOGGER.debug("lesson with id={} was updated", lesson.getId());
        } catch (DaoException ex) {
            LOGGER.error("lesson updatining fail! Reason to lesson with id={} not found", lesson.getId());
            throw new ServiceException(String.format(EXCEPTION_UPDATE, lesson.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing lesson with id={}", id);
        try {
            lessonDao.remove(id);
            LOGGER.debug("lesson with id={} was removed", id);
        } catch (DaoException ex) {
            LOGGER.error("lesson with id={} was not removed! Lesson not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Lesson lesson) {
        LOGGER.debug("begin validation");
        if (lesson.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (lesson.getGroup() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_GROUP);
        }
        if (lesson.getTeacher() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_TEACHER);
        }
        if (lesson.getLessonDurationSecond() < 2700) {
            throw new ServiceException(EXCEPTION_NOT_VALID_DURATION);
        }
        if (lesson.getStartTime() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_START_TIME);
        }
        LOGGER.debug("validation passed");
    }

    @Autowired
    public void setLessonDao(GenericHibernateDao<Lesson> lessonDao) {
        lessonDao.setClazz(Lesson.class);
        this.lessonDao = lessonDao;
    }

}
