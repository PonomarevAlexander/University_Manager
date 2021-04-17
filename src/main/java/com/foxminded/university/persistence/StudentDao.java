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
import com.foxminded.university.domain.exceptions.EntityCreatingFailureException;
import com.foxminded.university.domain.exceptions.EntityGettingFailureException;
import com.foxminded.university.domain.exceptions.EntityRemovingFailureException;
import com.foxminded.university.domain.exceptions.EntityUpdatingFailureException;
import com.foxminded.university.domain.models.Student;

@Repository
public class StudentDao implements Dao<Student> {

    private JdbcTemplate jdbcTemplate;

    private static final String QUERY_INSERT = "INSERT INTO students(name, last_name, age) VALUES(?, ?, ?)";
    private static final String QUERY_SELECT_BY_ID = "select * from students where id=?";
    private static final String QUERY_SELECT_ALL = "Select * from students";
    private static final String QUERY_SELECT_STUDENTS_RELATED_GROUP = "select * from students where group_id=?";
    private static final String QUERY_UPDATE = "update students set name=?, last_name=?, age=? where id=?";
    private static final String QUERY_UPDATE_STUDENT_GROUP = "update students set group_id=? where id=?";
    private static final String QUERY_DELETE_BY_ID = "delete from students where id=?";
    private static final String QUERY_DELETE_FROM_GROUP = "update students set group_id=null where id=?";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_ID = "id";

    @Autowired
    public StudentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Student student) throws EntityCreatingFailureException {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(student), holder);
        if (holder.getKey().longValue() != 0) {
            return (int) holder.getKey().longValue();
        } else {
            throw new EntityCreatingFailureException("student was not created");
        }
    }

    @Override
    public Student get(int id) throws EntityGettingFailureException {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityGettingFailureException(String.format("Student by id=%d not found", id));
        }
    }

    @Override
    public List<Student> getAll() throws EntityGettingFailureException {
        try {
            return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityGettingFailureException("students not found");
        }
    }

    @Override
    public void update(Student student) throws EntityUpdatingFailureException {
        try {
            jdbcTemplate.update(QUERY_UPDATE, student.getName(), student.getLastName(), student.getAge(), student.getId());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingFailureException(String.format("updating fail! student(id=%d) not found or not exist", student.getId()));
        }
    }

    @Override
    public void remove(int id) throws EntityRemovingFailureException {
        try {
            jdbcTemplate.update(QUERY_DELETE_BY_ID, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityRemovingFailureException("student removing fail, student(id=%d) not found or already was deleted");
        }
    }

    public List<Student> getStudentRelatedGroup(int groupId) throws EntityGettingFailureException {
        try {
            return jdbcTemplate.query(QUERY_SELECT_STUDENTS_RELATED_GROUP, getRowMapper(), groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityGettingFailureException(String.format("group(id=%d) not found or given group doesn't have students", groupId));
        }
    }

    public void setStudentToGroup(int studentId, int groupId) throws EntityUpdatingFailureException {
        try {
            jdbcTemplate.update(QUERY_UPDATE_STUDENT_GROUP, groupId, studentId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingFailureException(String.format("set student to group fail! student(id=%d) or group(id=%d) not found", studentId, groupId));
        }
    }
    
    public void removeStudentFromGroup(int studentId) throws EntityUpdatingFailureException {
        try {
            jdbcTemplate.update(QUERY_DELETE_FROM_GROUP, studentId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityRemovingFailureException(String.format("removing student from group fail! student(id=%d) not found", studentId));
        }
    }
    
    private RowMapper<Student> getRowMapper() {
        return (resultSet, rowNum) -> {
            Student student = new Student();
            student.setId(resultSet.getInt(COLUMN_ID));
            student.setName(resultSet.getString(COLUMN_NAME));
            student.setLastName(resultSet.getString(COLUMN_LAST_NAME));
            student.setAge(resultSet.getInt(COLUMN_AGE));
            return student;
        };
    }

    private PreparedStatementCreator getInsertParametredStatement(Student student) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, student.getName());
                statement.setString(2, student.getLastName());
                statement.setInt(3, student.getAge());
                return statement;
            }
        };
    }
}
