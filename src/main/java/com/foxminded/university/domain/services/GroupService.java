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
import com.foxminded.university.persistence.GroupDao;
import com.foxminded.university.persistence.StudentDao;
import com.foxminded.university.persistence.TeacherDao;
import com.foxminded.university.persistence.TimetableDao;

@Component
public class GroupService implements Service<Group> {

    private StudentDao studentDao;
    private GroupDao groupDao;
    private TimetableDao timetableDao;
    private TeacherDao teacherDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! group name is null";
    private static final String EXCEPTION_NOT_VALID_TEACHER = "validation failed! group teacher is null";
    private static final String EXCEPTION_NOT_VALID_STUDENTS_LIST = "validation failed! group students list is null";

    @Override
    public void add(Group group) throws DaoException, ServiceException {
        LOGGER.debug("creating new group with name={}", group.getName());
        try {
            validateEntity(group);
            int groupId = groupDao.add(group);
            groupDao.updateGroupHead(group.getCheef().getId(), groupId);
            group.getStudentList().forEach(student -> studentDao.setStudentToGroup(student.getId(), groupId));
            if (group.getTimetable() != null) {
                timetableDao.setTimetableToGroup(group.getTimetable().getId(), groupId);
            }
            LOGGER.debug("new group with id={} successufuly created! ", groupId);
        } catch (DaoException | ServiceException ex) {
            throw ex;
        }
    }

    @Override
    public Group getById(int id) throws DaoException {
        LOGGER.debug("getting group by id={}", id);
        try {
            Group group = groupDao.get(id);
            group.setCheef(teacherDao.getGroupTeacher(group.getId()));
            group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
            group.setTimetable(timetableDao.getTimetableRelatedGroup(group.getId()));
            LOGGER.debug("group with id={} was prepared and returned", id);
            return group;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public List<Group> getAll() throws DaoException {
        LOGGER.debug("going retrieving all groups list");
        try {
            List<Group> groupsList = groupDao.getAll();
            groupsList.forEach(group -> {
                group.setCheef(teacherDao.getGroupTeacher(group.getId()));
                group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
                group.setTimetable(timetableDao.getTimetableRelatedGroup(group.getId()));
            });
            LOGGER.debug("groups list was prepared and returned successfuly");
            return groupsList;
        } catch (DaoException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Group group) throws DaoException, ServiceException {
        LOGGER.debug("going update the group");
        try {
            validateEntity(group);
            groupDao.update(group);
            groupDao.updateGroupHead(group.getCheef().getId(), group.getId());
            group.getStudentList().forEach(student -> studentDao.setStudentToGroup(student.getId(), group.getId()));
            if (group.getTimetable() != null) {
                timetableDao.updateTimetableRelatedGroup(group.getTimetable().getId(), group.getId());
            }
            LOGGER.debug("The group with id={} updated successfuly", group.getId());
        } catch (DaoException | ServiceException ex) {
            throw ex;
        }
    }

    @Override
    public void remove(int id) throws DaoException {
        LOGGER.debug("going remove group by id={}", id);
        try {
            groupDao.remove(id);
            LOGGER.debug("successfuly removed group with id={}", id);
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public void changeDepartmentForGroup(Group group, int departmentId) throws DaoException {
        LOGGER.debug("going update group(id={}) department", group.getId());
        try {
            groupDao.updateGroupDepartment(departmentId, group.getId());
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public List<Student> getStudentsOfGroup(Group group) throws DaoException {
        LOGGER.debug("getting students list by group(id={})", group.getId());
        try {
            return studentDao.getStudentRelatedGroup(group.getId());
        } catch (DaoException ex) {
            throw ex;
        }
    }

    public void validateEntity(Group group) throws ServiceException {
        LOGGER.debug("begin validation");
        if (group.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (group.getCheef() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_TEACHER);
        }
        if (group.getStudentList() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_STUDENTS_LIST);
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
}
