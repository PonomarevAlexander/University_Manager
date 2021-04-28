package com.foxminded.university.domain.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foxminded.university.domain.exceptions.EntityNotCreatedException;
import com.foxminded.university.domain.exceptions.EntityNotFoundException;
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
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! teacher name is null";
    private static final String EXCEPTION_NOT_VALID_LAST_NAME = "validation failed! teacher last name is null";
    private static final String EXCEPTION_ADD = "Failed to creating new teacher!";
    private static final String EXCEPTION_GET = "Failed to receiving a teacher(id=%). Reason is ";
    private static final String EXCEPTION_GET_LIST = "Failed to receiving teachers list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the teacher(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the teacher(id=%d). Reason is ";

    @Override
    public void add(Teacher teacher) throws ServiceException {
        LOGGER.debug("creating a new teacher with name={}, last name={}", teacher.getName(), teacher.getLastName());
        validateEntity(teacher);
        try {
            int receivedId = teacherDao.add(teacher);
            if (teacher.getTimetable() != null) {
                timetableDao.setTimetableToTeacher(teacher.getTimetable().getId(), receivedId);
            }
            LOGGER.debug("new teacher({} {}) was created", teacher.getName(), teacher.getLastName());
        } catch (EntityNotCreatedException ex) {
            LOGGER.error("new teacher was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Teacher getById(int id) {
        LOGGER.debug("obtaining a teacher by id={}", id);
        try {
            Teacher teacher = teacherDao.get(id);
            Timetable timetable = timetableDao.getTimetableRelatedTeacher(teacher.getId());
            List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
            timetable.setSchedule(lessons);
            teacher.setTimetable(timetable);
            LOGGER.debug("Teacher with id={} was prepared and returned", teacher.getId());
            return teacher;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("lesson with id={} not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }

    }

    @Override
    public List<Teacher> getAll() {
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
        } catch (EntityNotFoundException ex) {
            LOGGER.error("no one teacher not found");
            throw new ServiceException(String.format(EXCEPTION_GET_LIST) +  ex.getMessage());
        }
    }

    @Override
    public void update(Teacher teacher) throws ServiceException {
        LOGGER.debug("updating teacher with id={}", teacher.getId());
        validateEntity(teacher);
        try {
            teacherDao.update(teacher);
            if (teacher.getTimetable() != null) {
                timetableDao.updateTimetableRelatedTeacher(teacher.getTimetable().getId(), teacher.getId());
            }
            LOGGER.debug("teacher with id={} was updated", teacher.getId());
        } catch (EntityNotFoundException ex) {
            LOGGER.error("teacher updating fail!");
            throw new ServiceException(String.format(EXCEPTION_UPDATE,teacher.getId()) +  ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing teacher with id={}", id);
        try {
            teacherDao.remove(id);
            LOGGER.debug("teacher with id={} was removed", id);
        } catch (EntityNotFoundException ex) {
            LOGGER.error("teacher with id={} was not removed! Lesson not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) +  ex.getMessage());
        }
    }

    private void validateEntity(Teacher teacher) {
        LOGGER.debug("begin validation");
        if (teacher.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (teacher.getLastName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_LAST_NAME);
        }
        LOGGER.debug("validation passed");
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
