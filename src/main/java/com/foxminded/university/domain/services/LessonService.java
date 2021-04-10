package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.TeacherDao;

public class LessonService implements Service<Lesson> {
    
    private GroupDao groupDao;
    private LessonDao lessonDao;
    private TeacherDao teacherDao;
    
    @Override
    public void add(Lesson lesson) {
        lessonDao.add(lesson);
    }

    @Override
    public Lesson getById(int id) {
        Lesson lesson = lessonDao.get(id);
        lesson.setTeacher(
                teacherDao.getTeacherByLessonId(id));
        lesson.setGroup(
                groupDao.getGroupByLesson(id));
        return lesson;
    }

    @Override
    public List<Lesson> getAll() {
        List<Lesson> lessonsList = lessonDao.getAll();
        lessonsList.forEach(lesson -> {
            lesson.setTeacher(
                    teacherDao.getTeacherByLessonId(lesson.getId()));
            lesson.setGroup(
                    groupDao.getGroupByLesson(lesson.getId()));
        });
        return lessonsList;
    }

    @Override
    public void update(Lesson lesson) {
        lessonDao.update(lesson);
    }

    @Override
    public void remove(int id) {
        lessonDao.remove(id);
    }
    
    public List<Lesson> getLessonsByTimetable(Timetable timetable) {
        return lessonDao.getLessonsOfTimetable(timetable.getId());
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
