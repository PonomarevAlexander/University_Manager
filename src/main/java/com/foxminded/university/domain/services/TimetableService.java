package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.EntityNotCreatedException;
import com.foxminded.university.domain.exceptions.EntityNotFoundException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@Component
public class TimetableService implements Service<Timetable> {

    private TimetableDao timetableDao;
    private LessonDao lessonDao;
    private TeacherDao teacherDao;
    private GroupDao groupDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableService.class);
    private static final String EXCEPTION_NOT_VALID_CREATION_DATE = "validation failed! timetable creation date is null";
    private static final String EXCEPTION_NOT_VALID_SCHEDULE = "validation failed! timetable schedule is null";
    private static final String EXCEPTION_ADD = "Failed to creating new timetable!";
    private static final String EXCEPTION_GET = "Failed to receiving a timetable(id=%). Reason is ";
    private static final String EXCEPTION_GET_LIST = "Failed to receiving all timetables list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the timetable(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the timetable(id=%d). Reason is ";

    @Override
    public void add(Timetable timetable) throws ServiceException {
        LOGGER.debug("creating new timetable");
        validateEntity(timetable);
        try {
            int timetableId = timetableDao.add(timetable);
            timetable.getSchedule().forEach(lesson -> lessonDao.setLessonToTimetable(lesson.getId(), timetableId));
            LOGGER.debug("timetable with was created");
        } catch (EntityNotCreatedException ex) {
            LOGGER.error("new timetable was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Timetable getById(int id) {
        LOGGER.debug("obtaining a lesson by id={}", id);
        try {
            Timetable timetable = timetableDao.get(id);
            List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
            lessons.forEach(lesson -> {
                lesson.setTeacher(teacherDao.getTeacherByLessonId(lesson.getId()));
                lesson.setGroup(groupDao.getGroupByLesson(lesson.getId()));
            });
            timetable.setSchedule(lessons);
            LOGGER.debug("timetable with id={} was prepared and returned", timetable.getId());
            return timetable;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("timetable with id={} not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Timetable> getAll() {
        LOGGER.debug("obtaining list of all lessons");
        try {
            List<Timetable> timetablesList = timetableDao.getAll();
            timetablesList.forEach(timetable -> {
                List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
                lessons.forEach(lesson -> {
                    lesson.setTeacher(teacherDao.getTeacherByLessonId(lesson.getId()));
                    lesson.setGroup(groupDao.getGroupByLesson(lesson.getId()));
                });
                timetable.setSchedule(lessons);
            });
            LOGGER.debug("all timetables obtained and returned");
            return timetablesList;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("no one timetable not found");
            throw new ServiceException(String.format(EXCEPTION_GET_LIST) + ex.getMessage());
        }
    }

    @Override
    public void update(Timetable timetable) throws ServiceException {
        LOGGER.debug("updating timetable with id={}", timetable.getId());
        validateEntity(timetable);
        try {
            timetableDao.update(timetable);
            timetable.getSchedule()
                    .forEach(lesson -> lessonDao.updateLessonOfTimetable(lesson.getId(), timetable.getId()));
            LOGGER.debug("timetable with id={} was updated", timetable.getId());
        } catch (EntityNotFoundException ex) {
            LOGGER.error("timetable updating fail!");
            throw new ServiceException(String.format(EXCEPTION_UPDATE, timetable.getId()) + ex.getMessage());
        }

    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing timetable with id={}", id);
        try {
            timetableDao.remove(id);
            LOGGER.debug("timetable with id={} was removed", id);
        } catch (EntityNotFoundException ex) {
            LOGGER.error("timetable with id={} was not removed! Timetable not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    public Timetable getTimetablesByTeacher(Teacher teacher) {
        LOGGER.debug("obtaining timetable by teacher(id={})", teacher.getId());
        try {
            Timetable timetable = timetableDao.getTimetableRelatedTeacher(teacher.getId());
            LOGGER.debug("timetable with id={} was prepared and returned", timetable.getId());
            return timetable;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("timetable not found");
            throw new ServiceException(String.format(EXCEPTION_GET) + ex.getMessage());
        }
    }

    public Timetable getTimetableByGroup(Group group) {
        LOGGER.debug("obtaining timetable by group(id={})", group.getId());
        try {
            Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());
            LOGGER.debug("timetable with id={} was prepared and returned", timetable.getId());
            return timetable;
        } catch (EntityNotFoundException ex) {
            LOGGER.error("timetable not found");
            throw new ServiceException(String.format(EXCEPTION_GET) + ex.getMessage());
        }
    }

    private void validateEntity(Timetable timetable) {
        LOGGER.debug("begin validation");
        if (timetable.getCreationDate() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_CREATION_DATE);
        }
        if (timetable.getSchedule() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_SCHEDULE);
        }
        LOGGER.debug("validation passed");
    }

    @Autowired
    public void setTimetableDao(TimetableDao timetableDao) {
        this.timetableDao = timetableDao;
    }

    @Autowired
    public void setLessonDao(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }
}
