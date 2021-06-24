package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.HibernateException;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.persistence.LessonRepository;

@Component
public class LessonService implements UniversityService<Lesson> {

    private LessonRepository lessonRepository;

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

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void add(Lesson lesson) {
        validateEntity(lesson);
        try {
            lessonRepository.save(lesson);
        } catch (HibernateException ex) {
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Lesson getById(int id) {
        try {
            return lessonRepository.findById(id).get();
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Lesson> getAll() {
        try {
            List<Lesson> lessonsList = (List<Lesson>) lessonRepository.findAll();
            return lessonsList;
        } catch (HibernateException ex) {
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Lesson lesson) throws ServiceException {
        validateEntity(lesson);
        try {
            lessonRepository.save(lesson);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_UPDATE, lesson.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        try {
            lessonRepository.deleteById(id);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Lesson lesson) {
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
    }

}
