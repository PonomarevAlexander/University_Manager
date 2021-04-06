package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.models.Lesson;
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

    @Override
    public void add(Timetable timetable) {
        int timetableId = timetableDao.add(timetable);
        timetable.getSchedule().forEach(lesson -> 
            lessonDao.setLessonToTimetable(lesson.getId(), timetableId));
    }
    
    @Override
    public Timetable getById(int id) {
        Timetable timetable = timetableDao.get(id);
        List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
        lessons.forEach(lesson -> {
            lesson.setTeacher(
                    teacherDao.getTeacherByLessonId(lesson.getId()));
            lesson.setGroup(
                    groupDao.getGroupByLesson(lesson.getId()));
        });
        timetable.setSchedule(lessons);
        return timetable;
    }

    @Override
    public List<Timetable> getAll() {
        List<Timetable> timetablesList = timetableDao.getAll();
        timetablesList.forEach(timetable -> {
            List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
            lessons.forEach(lesson -> {
                lesson.setTeacher(
                        teacherDao.getTeacherByLessonId(lesson.getId()));
                lesson.setGroup(
                        groupDao.getGroupByLesson(lesson.getId()));
            });
            timetable.setSchedule(lessons);
        });
        return timetablesList;
    }

    @Override
    public void update(Timetable timetable) {
        timetableDao.update(timetable);
        
        timetable.getSchedule().forEach(lesson -> 
            lessonDao.updateLessonOfTimetable(lesson.getId(), timetable.getId()));
    }

    @Override
    public void remove(int id) {
        timetableDao.remove(id);
    }
}
