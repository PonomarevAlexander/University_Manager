package com.foxminded.university.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.models.Lesson;

@Component
public class LessonDao implements Dao<Lesson> {
	
	private JdbcTemplate jdbcTemplate;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final RowMapper<Lesson> lessonRowMapper = (resultSet, rowNum) -> {
		Lesson lesson = new Lesson();
		lesson.setId(resultSet.getInt(COLUMN_ID));
		lesson.setName(resultSet.getString(COLUMN_NAME));
		lesson.setStartTime(LocalDateTime.parse(
				resultSet.getString(COLUMN_START_TIME), FORMATTER));
		lesson.setLessonDurationSecond(resultSet.getInt(COLUMN_DURATION));
		return lesson;
	};
	private static final String QUERY_INSERT = "insert into lessons(name, start_time, duration) values(?,?,?)";
	private static final String QUERY_GET_BY_ID = "select * from lessons where id=?";
	private static final String QUERY_GET_ALL = "select * from lessons";
	private static final String QUERY_UPDATE = "update lessons set name=?, start_time=?, duration=? where id=?";
	private static final String QUERY_DELETE = "delete from lessons where id=?";
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_START_TIME = "start_time";
	private static final String COLUMN_DURATION = "duration";
	
	
	@Autowired
	public LessonDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void add(Lesson lesson) {
		jdbcTemplate.update(QUERY_INSERT,
				lesson.getName(),
				lesson.getStartTime().format(FORMATTER),
				lesson.getLessonDurationSecond());
	}

	@Override
	public Lesson get(int id) {
		return jdbcTemplate.queryForObject(QUERY_GET_BY_ID, lessonRowMapper, id);
	}

	@Override
	public List<Lesson> getAll() {
		return jdbcTemplate.query(QUERY_GET_ALL, lessonRowMapper);
	}

	@Override
	public void update(Lesson lesson) {
		jdbcTemplate.update(QUERY_UPDATE,
				lesson.getName(),
				lesson.getStartTime().format(FORMATTER), 
				lesson.getLessonDurationSecond(),
				lesson.getId());
	}

	@Override
	public void remove(int id) {
		jdbcTemplate.update(QUERY_DELETE, id);
		
	}

}
