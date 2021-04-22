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

    @Override
    public void add(Student student) throws DaoException, ServiceException {
        LOGGER.debug("creating new student");
        try {
            if (validateEntity(student)) {
                student.setId(studentDao.add(student));
                studentDao.setStudentToGroup(student.getId(), student.getGroup().getId());
                LOGGER.debug("new student with id={} was created", student.getId());
            }
        } catch (DaoException | ServiceException ex) {
            throw ex;
        }
    }

    @Override
    public Student getById(int id) throws DaoException {
        LOGGER.debug("getting student by id={}", id);
        try {
            Student student = studentDao.get(id);
            Group group = groupDao.getGroupByStudent(id);
            Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());

            timetable.setSchedule(lessonDao.getLessonsOfTimetable(timetable.getId()));
            group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
            group.setTimetable(timetable);
            group.setCheef(teacherDao.getGroupTeacher(group.getId()));
            student.setGroup(group);
            LOGGER.debug("student with id={} was prepared and returned", student.getId());
            return student;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public List<Student> getAll() throws DaoException {
        LOGGER.debug("getting all students");
        try {
            List<Student> studentsList = studentDao.getAll();
            studentsList.forEach(student -> {
                Group group = groupDao.getGroupByStudent(student.getId());
                Timetable timetable = timetableDao.getTimetableRelatedGroup(group.getId());
                
                timetable.setSchedule(lessonDao.getLessonsOfTimetable(timetable.getId()));
                group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
                group.setTimetable(timetable);
                group.setCheef(teacherDao.getGroupTeacher(group.getId()));
                student.setGroup(group);
            });
            LOGGER.debug("students list(size={}) was prepared and returned", studentsList.size());
            return studentsList;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Student student) throws DaoException, ServiceException {
        LOGGER.debug("updating student");
        try {
            if (validateEntity(student)) {
                studentDao.update(student);
                studentDao.setStudentToGroup(student.getId(), student.getGroup().getId());
                LOGGER.debug("student with id={} successfully updated", student.getId());
            }
        } catch (DaoException | ServiceException ex) {
            throw ex;
        }
    }

    @Override
    public void remove(int id) throws DaoException {
        LOGGER.debug("removing student");
        try {
            studentDao.remove(id);
            LOGGER.debug("student with id={} has been deleted", id);
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public void assignToGroup(Student student, int targetGroup) throws DaoException {
        LOGGER.debug("seting student(id={}) to group(id={})", student.getId(), targetGroup);
        try {
            studentDao.setStudentToGroup(student.getId(), targetGroup);
            LOGGER.debug("student with id={} successfully assigned to group with id={}", student.getId(), targetGroup);
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public void removeFromGroup(Student student) throws DaoException {
        LOGGER.debug("removing student from group");
        try {
            studentDao.removeStudentFromGroup(student.getId());
            LOGGER.debug("student with id={} was removed from group with id={}", student.getId(),
                    student.getGroup().getId());
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public boolean validateEntity(Student student) throws ServiceException {
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
        return true;
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
