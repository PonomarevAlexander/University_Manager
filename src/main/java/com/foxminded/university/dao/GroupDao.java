package com.foxminded.university.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.foxminded.university.models.Group;

@Component
public class GroupDao implements Dao<Group> {

	private JdbcTemplate jdbcTemplate;
	
	private static final String QUERY_INSERT = "insert into groups(name) values(?)";
	private static final String QUERY_SELECT_BY_ID = "select * from groups where id=?";
	private static final String QUERY_SELECT_ALL = "select * from groups";
	private static final String QUERY_UPDATE = "update groups set name=? where id=?";
	private static final String QUERY_DELETE = "delete from groups where id=?";
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_NAME = "name";
	
	private final RowMapper<Group> groupRowMapper = (resultSet, rowNum) -> {
		Group group = new Group();
		group.setId(resultSet.getInt(COLUMN_ID));
		group.setName(resultSet.getString(COLUMN_NAME));
		return group;
	};
	
	@Autowired
	public GroupDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void add(Group group) {
		jdbcTemplate.update(QUERY_INSERT, group.getName());
	}

	@Override
	public Group get(int id) {
		return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID,
				groupRowMapper, id);
	}

	@Override
	public List<Group> getAll() {
		return jdbcTemplate.query(QUERY_SELECT_ALL, groupRowMapper);
	}

	@Override
	public void update(Group group) {
		jdbcTemplate.update(QUERY_UPDATE, group.getName(), group.getId());
	}

	@Override
	public void remove(int id) {
		jdbcTemplate.update(QUERY_DELETE, id);
	}
}
