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
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! lesson name is null";
    private static final String EXCEPTION_NOT_VALID_GROUP = "validation failed! lesson group is null";
    private static final String EXCEPTION_NOT_VALID_TEACHER = "validation failed! lesson teacher is null";
    private static final String EXCEPTION_NOT_VALID_START_TIME = "validation failed! lesson start time is null";
    private static final String EXCEPTION_NOT_VALID_DURATION = "validation failed! lesson duration less 45 min";

    @Override
    public void add(Lesson lesson) throws DaoException, ServiceException {
        LOGGER.debug("creating a new lesson with name={}", lesson.getName());
        try {
            validateEntity(lesson);
            int obtainedId = lessonDao.add(lesson);
            LOGGER.debug("lesson with name={} was created", obtainedId);
        } catch (DaoException | ServiceException ex) {
            throw ex;
        }
    }

    @Override
    public Lesson getById(int id) throws DaoException {
        LOGGER.debug("obtaining a lesson by id={}", id);
        try {
            Lesson lesson = lessonDao.get(id);
            lesson.setTeacher(teacherDao.getTeacherByLessonId(id));
            lesson.setGroup(groupDao.getGroupByLesson(id));
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
                lesson.setTeacher(teacherDao.getTeacherByLessonId(lesson.getId()));
                lesson.setGroup(groupDao.getGroupByLesson(lesson.getId()));
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
        try {
            validateEntity(lesson);
            lessonDao.update(lesson);
            LOGGER.debug("lesson with id={} was updated", lesson.getId());
        } catch (DaoException | ServiceException ex) {
            throw ex;
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

    private void validateEntity(Lesson lesson) throws ServiceException {
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
