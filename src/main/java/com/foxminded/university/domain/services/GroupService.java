package com.foxminded.university.domain.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.persistence.GroupRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GroupService implements UniversityService<Group> {
    
    private GroupRepository groupRepository;

    private static final String EXCEPTION_NOT_VALID_NAME = "validation failed! group name is null";
    private static final String EXCEPTION_NOT_VALID_TEACHER = "validation failed! group teacher is null";
    private static final String EXCEPTION_ADD = "Failed to creating new group!";
    private static final String EXCEPTION_GET = "Failed to receiving a group(id=%). Reason is ";
    private static final String EXCEPTION_GET_ALL = "Failed to receiving all groups list. Reason is ";
    private static final String EXCEPTION_UPDATE = "Failed to updating the group(id=%d). Reason is ";
    private static final String EXCEPTION_REMOVE = "Failed to removing the group(id=%d). Reason is ";

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void add(Group group) {
        validateEntity(group);
        try {
            groupRepository.save(group);
        } catch (HibernateException ex) {
            throw new ServiceException(EXCEPTION_ADD);
        }
    }

    @Override
    public Group getById(int id) throws HibernateException {
        try {
            return groupRepository.findById(id).get();
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_GET, id) + ex.getMessage());
        }
    }

    @Override
    public List<Group> getAll() {
        try {
            return (List<Group>) groupRepository.findAll();
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(EXCEPTION_GET_ALL + ex.getMessage());
        }
    }

    @Override
    public void update(Group group) throws ServiceException {
        validateEntity(group);
        try {
            groupRepository.save(group);
        } catch (HibernateException ex) {
            throw new ServiceException(String.format(EXCEPTION_UPDATE, group.getId()) + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void remove(int id) {
        try {
            groupRepository.deleteById(id);
        } catch (IllegalArgumentException ex) {
            throw new ServiceException(String.format(EXCEPTION_REMOVE, id) + ex.getMessage());
        }
    }

    private void validateEntity(Group group) {
        if (group.getName() == null) {
            throw new ServiceException(EXCEPTION_NOT_VALID_NAME);
        }
    }

}
