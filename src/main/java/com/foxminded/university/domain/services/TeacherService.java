package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    @Override
    public void add(Teacher teacher) {
        int receivedId = teacherDao.add(teacher);
        if (teacher.getTimetable() != null) {
            timetableDao.setTimetableToTeacher(teacher.getTimetable().getId(), receivedId);
        }
    }
    
    @Override
    public Teacher getById(int id) {
        Teacher teacher = teacherDao.get(id);
        Timetable timetable = timetableDao.getTimetableRelatedTeacher(teacher.getId());
        List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
        timetable.setSchedule(lessons);
        teacher.setTimetable(timetable);
        
        return teacher;
    }

    @Override
    public List<Teacher> getAll() {
        List<Teacher> teachersList = teacherDao.getAll();
        teachersList.forEach(teacher -> {
            Timetable timetable = timetableDao.getTimetableRelatedTeacher(teacher.getId());
            List<Lesson> lessons = lessonDao.getLessonsOfTimetable(timetable.getId());
            timetable.setSchedule(lessons);
            teacher.setTimetable(timetable);
        });
        return teachersList;
    }

    @Override
    public void update(Teacher teacher) {
        teacherDao.update(teacher);
        timetableDao.updateTimetableRelatedTeacher(teacher.getTimetable().getId(), teacher.getId());
    }

    @Override
    public void remove(int id) {
        teacherDao.remove(id);
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