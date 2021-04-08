package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Student;
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.LessonDao;
import com.foxminded.university.persistence.StudentDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@Component
public class StudentService implements Service<Student> {
    
    private StudentDao studentDao;
    private GroupDao groupDao;
    private TimetableDao timetableDao;
    private TeacherDao teacherDao;
    private LessonDao lessonDao;

    @Override
    public void add(Student student) {
        student.setId(studentDao.add(student));
        studentDao.assignStudentToGroup(student.getId(),
                student.getGroup().getId());
    }

    @Override
    public Student getById(int id) {
        Student student = studentDao.get(id);
        Group group = groupDao.getStudentGroup(id);
        Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());
        
        timetable.setSchedule(lessonDao.getLessonsOfTimetable(timetable.getId()));
        group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
        group.setTimetable(timetable);
        group.setCheef(teacherDao.getGroupTeacher(group.getId()));
        student.setGroup(group);
        
        return student;
    }

    @Override
    public List<Student> getAll() {
        List<Student> studentsList = studentDao.getAll();
        studentsList.forEach(student -> {
            Group group = groupDao.getStudentGroup(student.getId());
            Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());
            timetable.setSchedule(
                    lessonDao.getLessonsOfTimetable(timetable.getId()));
            group.setStudentList(
                    studentDao.getStudentRelatedGroup(group.getId()));
            group.setTimetable(timetable);
            group.setCheef(
                    teacherDao.getGroupTeacher(group.getId()));
            student.setGroup(group);
        });
        return studentsList;
    }

    @Override
    public void update(Student student) {
        studentDao.update(student);
        studentDao.updateStudentRelatedGroup(student.getId(),
                student.getGroup().getId());
    }

    @Override
    public void remove(int id) {
        studentDao.remove(id);
    }
    
    public void assignToGroup(Student student, int targetGroup) {
        studentDao.assignStudentToGroup(student.getId(), targetGroup);
    }
    
    public void removeFromGroup(Student student) {
        studentDao.removeStudentFromGroup(student.getId());
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Autowired
    public void setTimetableDao(TimetableDao timetableDao) {
        this.timetableDao = timetableDao;
    }

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setLessonDao(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }
}
