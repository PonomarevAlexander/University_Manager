package com.foxminded.university.domain.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.persistence.UniversityRepository;
import com.foxminded.university.persistence.GenericHibernateRepositoryImpl;

@Component
public class GroupService implements UniversityService<Group> {
    
    private UniversityRepository<Group> groupDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! group name is null";
    private static final String EXCEPTION_NOT_VALID_TEACHER = "validation failed! group teacher is null";
    private static final String EXCEPTION_ADD = "Failed to creating new group!";
    private static final String EXCEPTION_GET = "Failed to receiving a group(id=%). Reason is ";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all groups list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the group(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the group(id=%d). Reason is ";

    @Override
    public void add(Group group) throws HibernateException, ServiceException {
        LOGGER.debug("creating new group with name={}", group.getName());
        validateEntity(group);
        try {
            groupDao.add(group);
            LOGGER.debug("new group successufuly created! ");
        } catch (HibernateException ex) {
            LOGGER.error("new group was not created");
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Group getById(int id) throws HibernateException {
        LOGGER.debug("getting group by id={}", id);
        try {
            Group group = groupDao.get(id);
            LOGGER.debug("group with id={} was prepared and returned", group.getId());
            return group;
        } catch (HibernateException ex) {
            LOGGER.error("group with id={} not found! Group or students, teacher, timetable related to the group not found", id);
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Group> getAll() {
        LOGGER.debug("going retrieving all groups list");
        try {
            List<Group> groupsList = groupDao.getAll();
            LOGGER.debug("groups list was prepared and returned successfuly");
            return groupsList;
        } catch (HibernateException ex) {
            LOGGER.error("no one group not found");
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Group group) throws ServiceException {
        LOGGER.debug("going update the group");
        validateEntity(group);
        try {
            groupDao.update(group);
            LOGGER.debug("The group with id={} updated successfuly", group.getId());
        } catch (HibernateException ex) {
            LOGGER.error("group with id={} not found! Group or students, teacher, timetable related to the group not found", group.getId());
            throw new ServiceException(String.format(EXCEPTION_UPDATE, group.getId()) + ex.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        LOGGER.debug("going remove group by id={}", id);
        try {
            groupDao.remove(id);
            LOGGER.debug("successfuly removed group with id={}", id);
        } catch (HibernateException ex) {
            LOGGER.error("group with id={} was not removed! Group not found", id);
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Group group) {
        LOGGER.debug("begin validation");
        if (group.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
        if (group.getTeacher() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_TEACHER);
        }
        LOGGER.debug("validation passed");
    }

    @Autowired
    public void setGroupDao(GenericHibernateRepositoryImpl<Group> groupDao) {
        groupDao.setClazz(Group.class);
        this.groupDao = groupDao;
    }
}
