package com.foxminded.university.domain.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    
    @Override
    public void add(Group group) {
        int groupId = groupDao.add(group);
        groupDao.updateGroupHead(group.getCheef().getId(), groupId);
        group.getStudentList().forEach(student ->
            studentDao.setStudentToGroup(student.getId(), groupId));
        timetableDao.setTimetableToGroup(group.getTimetable().getId(), groupId);
    }

    @Override
    public Group getById(int id) {
        Group group = groupDao.get(id);
        group.setCheef(teacherDao.getGroupTeacher(group.getId()));
        group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
        group.setTimetable(timetableDao.getTimetableRelatedGroup(group.getId()));
        
        return group;
    }

    @Override
    public List<Group> getAll() {
        List<Group> groupsList = groupDao.getAll();
        groupsList.forEach(group -> {
            group.setCheef(teacherDao.getGroupTeacher(group.getId()));
            group.setStudentList(studentDao.getStudentRelatedGroup(group.getId()));
            group.setTimetable(timetableDao.getTimetableRelatedGroup(group.getId()));
        });
        return groupsList;
    }

    @Override
    public void update(Group group) {
        groupDao.update(group);
        groupDao.updateGroupHead(group.getCheef().getId(),
                group.getId());
        group.getStudentList().forEach(student -> 
                studentDao.setStudentToGroup(student.getId(), group.getId()));
        timetableDao.updateTimetableRelatedGroup(group.getTimetable().getId(), group.getId());
    }

    @Override
    public void remove(int id) {
        groupDao.remove(id);
    }
    
    public void changeDepartmentForGroup(Group group, int departmentId) {
        groupDao.updateGroupDepartment(departmentId, group.getId());
    }
    
    public List<Student> getStudentsOfGroup(Group group) {
        return studentDao.getStudentRelatedGroup(group.getId());
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
