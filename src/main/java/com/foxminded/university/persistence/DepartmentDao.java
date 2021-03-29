package com.foxminded.university.persistence;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.foxminded.university.domain.models.Department;

@Repository
public class DepartmentDao implements Dao<Department> {
    
    private JdbcTemplate jdbcTemplate;
    private static final String QUERY_INSERT = "insert into departments(name) values(?)";
    private static final String QUERY_SELECT_BY_ID = "select * from departments where id=?";
    private static final String QUERY_SELECT_ALL = "select * from departments";
    private static final String QUERY_UPDATE = "update departments set name=? where id=?";
    private static final String QUERY_DELETE = "delete from departments where id=?";

    @Autowired
    public DepartmentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public void add(Department department) {
        jdbcTemplate.update(QUERY_INSERT, department.getName());
    }

    @Override
    public Department get(int id) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
    }

    @Override
    public List<Department> getAll() {
        return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
    }

    @Override
    public void update(Department department) {
        jdbcTemplate.update(QUERY_UPDATE, department.getName(), department.getId());
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(QUERY_DELETE, id);
    }
    
    private RowMapper<Department> getRowMapper() { 
        return (resultSet, rowNum) -> {
            Department department = new Department();
            department.setId(resultSet.getInt("id"));
            department.setName(resultSet.getString("name"));
            return department;
        };
    }

}
