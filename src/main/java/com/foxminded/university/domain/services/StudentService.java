package com.foxminded.university.domain.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.exceptions.ServiceException;
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
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! student name is null";
    private static final String EXCEPTION_NOT_VALID_LAST_NAME = "validation failed! student last name is null";
    private static final String EXCEPTION_NOT_VALID_AGE = "validation failed! student age is 0 or less";
    private static final String EXCEPTION_NOT_VALID_GROUP = "validation failed! student group is null";
    private static final String EXCEPTION_ADD = "Failed to creating new student!";
    private static final String EXCEPTION_GET = "Failed to receiving a student(id=%). Reason is ";
    private static final String EXCEPTION_GET_BY_GROUP = "receiving a students related a group fail reason is";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all students list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the student(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the student(id=%d). Reason is ";

    @Override
    public int add(Student student) throws ServiceException {
        LOGGER.debug("creating new student");
        validateEntity(student);
        try {
            int studentId = studentDao.add(student);
            studentDao.setStudentToGroup(studentId, student.getGroup().getId());
            LOGGER.debug("new student with id={} was created", studentId);
            return studentId;
        } catch (DaoException ex) {
            LOGGER.error("new student was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Student getById(int id) {
        LOGGER.debug("getting student by id={}", id);
        try {
            Student student = studentDao.get(id);
            Group group = groupDao.getGroupByStudent(id);
            Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());

            timetable.setSchedule(lessonDao.getLessonsOfTimetable(timetable.getId()));
            group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
            group.setTimetable(timetable);
            group.setTeacher(teacherDao.getGroupTeacher(group.getId()));
            student.setGroup(group);
            LOGGER.debug("student with id={} was prepared and returned", student.getId());
            return student;
        } catch (DaoException ex) {
            LOGGER.error("student with id={} not found or his group not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("getting all students");
        try {
            List<Student> studentsList = studentDao.getAll();
            studentsList.forEach(student -> {
                Group group = groupDao.getGroupByStudent(student.getId());
                Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());
                
                timetable.setSchedule(lessonDao.getLessonsOfTimetable(timetable.getId()));
                group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
                group.setTimetable(timetable);
                group.setTeacher(teacherDao.getGroupTeacher(group.getId()));
                student.setGroup(group);
            });
            LOGGER.debug("students list(size={}) was prepared and returned", studentsList.size());
            return studentsList;
        } catch (DaoException ex) {
            LOGGER.error("no one student not found");
            throw new ServiceException(String.format(EXCEPTION_GET_ALL) + ex.getMessage());
        }
    }

    @Override
    public void update(Student student) throws ServiceException {
        LOGGER.debug("updating student");
        validateEntity(student);
        try {
            studentDao.update(student);
            studentDao.setStudentToGroup(student.getId(), student.getGroup().getId());
            LOGGER.debug("student with id={} successfully updated", student.getId());
        } catch (DaoException ex) {
            LOGGER.error("student updating failed!");
            throw new ServiceException(String.format(EXCEPTION_UPDATE, student.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("removing student");
        try {
            studentDao.remove(id);
            LOGGER.debug("student with id={} has been deleted", id);
        } catch (DaoException ex) {
            LOGGER.error("student with id={} was not removed! Student not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }
    
    public List<Student> getStudentsByGroup(Group group) {
        LOGGER.debug("getting students list by group(id={})", group.getId());
        try {
            return studentDao.getStudentRelatedGroup(group.getId());
        } catch (DaoException ex) {
            LOGGER.error("receiving students list fail! Group with id={} not found", group.getId());
            throw new ServiceException(String.format(EXCEPTION_GET_BY_GROUP) + ex.getMessage());
        }
    }

    public void assignToGroup(Student student, int targetGroup) {
        LOGGER.debug("seting student(id={}) to group(id={})", student.getId(), targetGroup);
        try {
            studentDao.setStudentToGroup(student.getId(), targetGroup);
            LOGGER.debug("student with id={} successfully assigned to group with id={}", student.getId(), targetGroup);
        } catch (DaoException ex) {
            LOGGER.error("student updating failed!");
            throw new ServiceException(String.format(EXCEPTION_UPDATE, student.getId()) + ex.getMessage());
        }
    }

    public void removeFromGroup(Student student) {
        LOGGER.debug("removing student from group");
        try {
            studentDao.removeStudentFromGroup(student.getId());
            LOGGER.debug("student with id={} was removed from group with id={}", student.getId(), student.getGroup().getId());
        } catch (DaoException ex) {
            LOGGER.error("student with id={} was not removed! Student not found", student.getId());
            throw new ServiceException(String.format(EXCEPTION_UPDATE, student.getId()) + ex.getMessage());
        }
    }

    private void validateEntity(Student student) 
    {
        LOGGER.debug("begin validation");
        if (student.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (student.getLastName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_LAST_NAME);
        }
        if (student.getAge() <= 0) {
            throw new ServiceException(EXCEPTION_NOT_VALID_AGE);
        }
        if (student.getGroup() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_GROUP);
        }
        LOGGER.debug("validation passed");
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
