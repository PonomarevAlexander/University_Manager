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
import com.foxminded.university.domain.models.Department;

@Repository
public class DepartmentDao implements Dao<Department> {
    
    private JdbcTemplate jdbcTemplate;
    private static final String QUERY_INSERT = "insert into departments(name) values(?)";
    private static final String QUERY_SELECT_BY_ID = "select * from departments where id=?";
    private static final String QUERY_SELECT_ALL = "select * from departments";
    private static final String QUERY_UPDATE = "update departments set name=? where id=?";
    private static final String QUERY_DELETE = "delete from departments where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String EXCEPTION_STUDENT_CREATE = "new department(%s) was not create";
    private static final String EXCEPTION_DEPARTMENT_NOT_FOUND = "group department with id=%d not found";
    private static final String EXCEPTION_ALL_DEPARTMENTS_NOT_FOUND = "nothing to get. Database has no department yet";

    @Autowired
    public DepartmentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public int add(Department department) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(department), holder);
        int obtainedKey = holder.getKey().intValue();
        if (obtainedKey != 0) {
            return obtainedKey;
        } else {
            throw new EntityNotCreatedException(EXCEPTION_STUDENT_CREATE);
        }
    } 
        
    @Override
    public Department get(int id) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_DEPARTMENT_NOT_FOUND, id));
        }
    }

    @Override
    public List<Department> getAll() {
        try {
            return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_ALL_DEPARTMENTS_NOT_FOUND));
        }
    }

    @Override
    public void update(Department department) {
        try {
            jdbcTemplate.update(QUERY_UPDATE, department.getName(), department.getId());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(EXCEPTION_DEPARTMENT_NOT_FOUND);
        }
    }

    @Override
    public void remove(int id) {
        try {
            jdbcTemplate.update(QUERY_DELETE, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(EXCEPTION_DEPARTMENT_NOT_FOUND);
        }
    }
    
    private RowMapper<Department> getRowMapper() { 
        return (resultSet, rowNum) -> {
            Department department = new Department();
            department.setId(resultSet.getInt(COLUMN_ID));
            department.setName(resultSet.getString(COLUMN_NAME));
            return department;
        };
    }
    
    private PreparedStatementCreator getInsertParametredStatement(Department department) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, department.getName());
                return statement;
            }
        };
    }

}
