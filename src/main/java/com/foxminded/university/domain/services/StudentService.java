package com.foxminded.university.domain.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.EntityCreatingFailureException;
import com.foxminded.university.domain.exceptions.EntityGettingFailureException;
import com.foxminded.university.domain.exceptions.EntityRemovingFailureException;
import com.foxminded.university.domain.exceptions.EntityUpdatingFailureException;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    @Override
    public void add(Student student) throws EntityCreatingFailureException {
        LOGGER.debug("creating new student");
        try {
            student.setId(studentDao.add(student));
            studentDao.setStudentToGroup(student.getId(), student.getGroup().getId());
            LOGGER.debug("new student with id={} was created", student.getId());
        } catch (EntityCreatingFailureException ex) {
            throw ex;
        }
    }

    @Override
    public Student getById(int id) throws EntityGettingFailureException {
        LOGGER.debug("getting student by id={}", id);
        try {
            Student student = studentDao.get(id);
            Group group = groupDao.getGroupByStudent(id);
            Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());
            
            timetable.setSchedule(
                    lessonDao.getLessonsOfTimetable(timetable.getId()));
            group.setStudentList(
                    studentDao.getStudentRelatedGroup(group.getId()));
            group.setTimetable(timetable);
            group.setCheef(
                    teacherDao.getGroupTeacher(group.getId()));
            student.setGroup(group);
            LOGGER.debug("student with id={} was prepared and returned", student.getId());
            return student;
        } catch (EntityGettingFailureException ex) {
            throw ex;
        }
    }

    @Override
    public List<Student> getAll() throws EntityGettingFailureException {
        LOGGER.debug("getting all students");
        try {
            List<Student> studentsList = studentDao.getAll();
            studentsList.forEach(student -> {
                Group group = groupDao.getGroupByStudent(student.getId());
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
            LOGGER.debug("students list(size={}) was prepared and returned", studentsList.size());
            return studentsList;
        } catch (EntityGettingFailureException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Student student) throws EntityUpdatingFailureException {
        LOGGER.debug("updating student");
        try {
            studentDao.update(student);
            studentDao.setStudentToGroup(student.getId(),
                    student.getGroup().getId());
            LOGGER.debug("student with id={} successfully updated", student.getId());
        } catch (EntityUpdatingFailureException ex) {
            throw ex;
        }
    }

    @Override
    public void remove(int id) throws EntityRemovingFailureException {
        LOGGER.debug("removing student");
        try {
            studentDao.remove(id);
            LOGGER.debug("student with id={} has been deleted", id);
        } catch (EntityRemovingFailureException ex) {
            throw ex;
        }
    }
    
    public void assignToGroup(Student student, int targetGroup) throws EntityUpdatingFailureException {
        LOGGER.debug("seting student(id={}) to group(id={})", student.getId(), targetGroup);
        try {
            studentDao.setStudentToGroup(student.getId(), targetGroup);
            LOGGER.debug("student with id={} successfully assigned to group with id={}", student.getId(), targetGroup);
        } catch (EntityUpdatingFailureException ex) {
            throw ex;
        }
    }
    
    public void removeFromGroup(Student student) throws EntityUpdatingFailureException {
        LOGGER.debug("removing student from group");
        try {
            studentDao.removeStudentFromGroup(student.getId());
            LOGGER.debug("student with id={} was removed from group with id={}", student.getId(), student.getGroup().getId());
        } catch (EntityUpdatingFailureException ex) {
            throw ex;
        }
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
