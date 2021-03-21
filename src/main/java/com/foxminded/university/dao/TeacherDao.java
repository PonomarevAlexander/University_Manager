package com.foxminded.university.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.models.Teacher;

@Component
public class TeacherDao implements Dao<Teacher> {
	
	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Teacher> teacherRowMapper = (resultSet, rowNum) -> {
		Teacher teacher = new Teacher();
		teacher.setId(resultSet.getInt("id"));
		teacher.setName(resultSet.getString("name"));
		teacher.setLastName(resultSet.getString("last_name"));
		return teacher;
	};
	
	@Autowired
	public TeacherDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void add(Teacher teacher) {
		jdbcTemplate.update("insert into teachers(name, last_name) values(?, ?)",
				teacher.getName(), teacher.getLastName());
	}

	@Override
	public Teacher get(int id) {
		return jdbcTemplate.queryForObject("select * from teachers where id=?", teacherRowMapper, id);
	}

	@Override
	public List<Teacher> getAll() {
		return jdbcTemplate.query("select * from teachers", teacherRowMapper);
	}

	@Override
	public void update(Teacher teacher) {
		jdbcTemplate.update("update teachers set name=?, last_name=? where id=?", teacher.getName(), teacher.getLastName(), teacher.getId());
	}

	@Override
	public void remove(int id) {
		jdbcTemplate.update("delete from teachers where id=?", id);
		
	}
}
