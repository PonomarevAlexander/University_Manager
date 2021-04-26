
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
import com.foxminded.university.domain.exceptions.EntityRemovingException;
import com.foxminded.university.domain.exceptions.EntityUpdatingException;
import com.foxminded.university.domain.models.Teacher;

@Repository
public class TeacherDao implements Dao<Teacher> {

    private JdbcTemplate jdbcTemplate;
    
    private static final String QUERY_INSERT = "insert into teachers(name, last_name) values(?, ?)";
    private static final String QUERY_INSERT_DEPARTMENT = "insert into teachers(department_id) values(?) where id=?";
    private static final String QUERY_GET_BY_ID = "select * from teachers where id=?";
    private static final String QUERY_GET_ALL = "select * from teachers";
    private static final String QUERY_GET_TEACHER_BY_LESSON = "select t.* from teachers t left join lessons l on t.id=l.teacher_id where l.id=?";
    private static final String QUERY_GET_TEACHER_BY_DEPARTMENT = "select * from teachers t where t.department_id=?";
    private static final String QUERY_GET_TEACHER_BY_GROUP = "select t.* from teachers t left join groups g on t.id=g.head where g.id=?";
    private static final String QUERY_UPDATE = "update teachers set name=?, last_name=? where id=?";
    private static final String QUERY_DEPARTMEN = "update teachers set department_id=? where id=?";
    private static final String QUERY_DELETE = "delete from teachers where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String EXCEPTION_TEACHER_NOT_FOUND = "teacher with id=%d not found";
    private static final String EXCEPTION_LESSON_NOT_FOUND = "lesson with id=%d not found";
    private static final String EXCEPTION_ALL_TEACHERS_NOT_FOUND = "nothing to get. Database has no teachers yet";
    private static final String EXCEPTION_DEPARTMENT_NOT_FOUND = "department with id=%d not found";
    private static final String EXCEPTION_TEACHER_CREATE = "could not create a new teacher(name=%s, last name=%s)";
    private static final String OR = " or ";


    @Autowired
    public TeacherDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Teacher teacher) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(teacher), holder);
        int obtainedId = holder.getKey().intValue();
        if (obtainedId != 0) {
            return obtainedId;
        } else {
            throw new EntityNotCreatedException(String.format(EXCEPTION_TEACHER_CREATE, teacher.getName(), teacher.getLastName()));
        }
    }

    @Override
    public Teacher get(int id) {
        try {
            return jdbcTemplate.queryForObject(QUERY_GET_BY_ID, getRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
             throw new EntityNotFoundException(String.format(EXCEPTION_TEACHER_NOT_FOUND, id));
        }
    }

    @Override
    public List<Teacher> getAll() {
        try {
            return jdbcTemplate.query(QUERY_GET_ALL, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(EXCEPTION_ALL_TEACHERS_NOT_FOUND);
        }
    }

    @Override
    public void update(Teacher teacher) {
        try {
            jdbcTemplate.update(QUERY_UPDATE, teacher.getName(), teacher.getLastName(), teacher.getId());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_TEACHER_NOT_FOUND, teacher.getId()));
        }
    }

    @Override
    public void remove(int id) {
        try {
            jdbcTemplate.update(QUERY_DELETE, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityRemovingException(String.format(EXCEPTION_TEACHER_NOT_FOUND, id));
        }
    }
    
    public Teacher getTeacherByLessonId(int lessonId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_GET_TEACHER_BY_LESSON, getRowMapper(), lessonId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_LESSON_NOT_FOUND, lessonId));
        }
    }
    
    public Teacher getGroupTeacher(int groupId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_GET_TEACHER_BY_GROUP, getRowMapper(), groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_TEACHER_NOT_FOUND, groupId));
        }
    }
    
    public void setDepartmentToTeacher(int departmentId, int teacherId) {
        try {
            jdbcTemplate.update(QUERY_INSERT_DEPARTMENT, departmentId, teacherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_TEACHER_NOT_FOUND + OR + EXCEPTION_DEPARTMENT_NOT_FOUND, teacherId, departmentId));
        }
    }
    
    public void updateTeacherDepartment(int departmentId, int teacherId) {
        try {
            jdbcTemplate.update(QUERY_DEPARTMEN, departmentId, teacherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_TEACHER_NOT_FOUND + OR + EXCEPTION_DEPARTMENT_NOT_FOUND, teacherId, departmentId));
        }
    }
    
    public List<Teacher> getTeachersByDepartment(int departmentId) {
        try {
            return jdbcTemplate.query(QUERY_GET_TEACHER_BY_DEPARTMENT, getRowMapper(), departmentId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_DEPARTMENT_NOT_FOUND, departmentId));
        }
        
    }
    
    private RowMapper<Teacher> getRowMapper() {
        return (resultSet, rowNum) -> {
            Teacher teacher = new Teacher();
            teacher.setId(resultSet.getInt(COLUMN_ID));
            teacher.setName(resultSet.getString(COLUMN_NAME));
            teacher.setLastName(resultSet.getString(COLUMN_LAST_NAME));
            return teacher;
        };
    }
    
    private PreparedStatementCreator getInsertParametredStatement(Teacher teacher) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, teacher.getName());
                statement.setString(2, teacher.getLastName());
                return statement;
            }
        };
    }
}
