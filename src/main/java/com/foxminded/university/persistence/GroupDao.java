package com.foxminded.university.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.foxminded.university.domain.exceptions.EntityNotCreatedException;
import com.foxminded.university.domain.exceptions.EntityNotFoundException;
import com.foxminded.university.domain.models.Group;

@Repository
public class GroupDao implements Dao<Group> {

    private JdbcTemplate jdbcTemplate;

    private static final String QUERY_INSERT = "insert into groups(name) values(?)";
    private static final String QUERY_INSERT_DEPARTMENT = "insert into groups(department_id) values(?) where groups.id=? on conflict do nonthing";
    private static final String QUERY_SELECT_BY_ID = "select * from groups where id=?";
    private static final String QUERY_SELECT_ALL = "select * from groups";
    private static final String QUERY_SELECT_GROUP_BY_STUDENT = "select g.* from groups g left join students s on g.id=s.group_id where s.id=?";
    private static final String QUERY_SELECT_GROUP_BY_LESSON = "select g.* from groups g left join lessons l on l.group_id=g.id where l.id=?";
    private static final String QUERY_SELECT_GROUP_BY_DEPARTMENT = "select * from groups where groups.department_id=?";
    private static final String QUERY_SELECT_GROUP_BY_TEACHER = "select * from groups where head=?";
    private static final String QUERY_UPDATE = "update groups set name=? where id=?";
    private static final String QUERY_UPDATE_DEPARTMENT = "update groups set department_id=? where groups.id=?";
    private static final String QUERY_UPDATE_GROUPHEAD = "update groups set head=? where id=?";
    private static final String QUERY_DELETE = "delete from groups where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String OR = " or ";
    private static final String EXCEPTION_GROUP_NOT_FOUND = "group with id=%d not found";
    private static final String EXCEPTION_DEPARTMENT_NOT_FOUND = "department with id=%d not found";
    private static final String EXCEPTION_TEACHER_NOT_FOUND = "teacher with id=%d not found";
    private static final String EXCEPTION_LESSON_NOT_FOUND = "lesson with id=%d not found";
    private static final String EXCEPTION_STUDENT_NOT_FOUND = "student with id=%d not found";
    private static final String EXCEPTION_ALL_GROUPS_NOT_FOUND = "nothing to get. Database has no groups yet";
    private static final String EXCEPTION_GROUP_CREATE = "could not create a new group with name=%s";

    @Autowired
    public GroupDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Group group) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(group), holder);
        if (holder.getKey().longValue() != 0) {
            return holder.getKey().intValue();
        } else {
            throw new EntityNotCreatedException(String.format(EXCEPTION_GROUP_CREATE, group.getName()));
        }
    }

    @Override
    public Group get(int id) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_GROUP_NOT_FOUND, id));
        }
    }

    @Override
    public List<Group> getAll() {
        try {
            return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(EXCEPTION_ALL_GROUPS_NOT_FOUND);
        }
    }

    @Override
    public void update(Group group) {
        try {
            jdbcTemplate.update(QUERY_UPDATE, group.getName(), group.getId());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_GROUP_NOT_FOUND, group.getId()));
        }
    }

    @Override
    public void remove(int id) {
        try {
            jdbcTemplate.update(QUERY_DELETE, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_GROUP_NOT_FOUND, id));
        }
    }

    public Group getGroupByStudent(int studentId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_GROUP_BY_STUDENT, getRowMapper(), studentId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_STUDENT_NOT_FOUND, studentId));
        }
    }
    
    public Group getGroupByLesson(int lessonId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_GROUP_BY_LESSON, getRowMapper(), lessonId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_LESSON_NOT_FOUND, lessonId));
        }
    }
    
    public List<Group> getGroupsByDepartment(int departmentId) {
        try {
            return jdbcTemplate.query(QUERY_SELECT_GROUP_BY_DEPARTMENT, getRowMapper(), departmentId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_DEPARTMENT_NOT_FOUND, departmentId));
        }
    }
    
    public Group getGroupByTeacher(int teacherId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_GROUP_BY_TEACHER, getRowMapper(), teacherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_TEACHER_NOT_FOUND, teacherId));
        }
    }
    
    public void setDepartmentToGroup(int departmentId, int groupId) {
        try {
            jdbcTemplate.update(QUERY_INSERT_DEPARTMENT, departmentId, groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_DEPARTMENT_NOT_FOUND + OR + EXCEPTION_GROUP_NOT_FOUND, departmentId, groupId));
        }
    }
    
    public void updateGroupDepartment(int departmentId, int groupId) {
        try {
            jdbcTemplate.update(QUERY_UPDATE_DEPARTMENT, departmentId, groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_DEPARTMENT_NOT_FOUND + OR + EXCEPTION_GROUP_NOT_FOUND, departmentId, groupId));
        }
    }
    
    public void updateGroupHead(int teacherId, int groupId) {
        try {
            jdbcTemplate.update(QUERY_UPDATE_GROUPHEAD, teacherId, groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_TEACHER_NOT_FOUND + OR + EXCEPTION_GROUP_NOT_FOUND, teacherId, groupId));
        }
    }
    
    private RowMapper<Group> getRowMapper() {
        return (resultSet, rowNum) -> {
            Group group = new Group();
            group.setId(resultSet.getInt(COLUMN_ID));
            group.setName(resultSet.getString(COLUMN_NAME));
            return group;
        };
    }
    
    private PreparedStatementCreator getInsertParametredStatement(Group group) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, group.getName());
                return statement;
            }
        };
    }
}
