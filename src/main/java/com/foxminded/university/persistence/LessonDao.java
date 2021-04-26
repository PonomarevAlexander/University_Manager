package com.foxminded.university.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.foxminded.university.domain.exceptions.EntityUpdatingException;
import com.foxminded.university.domain.models.Lesson;

@Repository
public class LessonDao implements Dao<Lesson> {

    private JdbcTemplate jdbcTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String QUERY_INSERT = "insert into lessons(name, start_time, duration, teacher_id, group_id) values(?,?,?,?,?)";
    private static final String QUERY_INSERT_LESSON_TO_TIMETABLE = "insert into timetables_lessons (lesson_id, timetable_id)"
            + " select ?, ? where not exists(select * from timetables_lessons where lesson_id=? and timetable_id=?)";
    private static final String QUERY_GET_BY_ID = "select * from lessons where id=?";
    private static final String QUERY_GET_ALL = "select * from lessons";
    private static final String QUERY_GET_LESSONS_OF_TIMETABLE = "select l.* from lessons l left join timetables_lessons tl on l.id=tl.lesson_id where tl.timetable_id=?";
    private static final String QUERY_UPDATE = "update lessons set name=?, start_time=?, duration=?, teacher_id=?, group_id=? where id=?";
    private static final String QUERY_UPDATE_LESSONS_TIMETABLES = "update timetables_lessons set lesson_id=?, timetable_id=? where timetable_id=? and lesson_id=?";
    private static final String QUERY_DELETE = "delete from lessons where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_DURATION = "duration";
    private static final String EXCEPTION_LESSON_NOT_FOUND = "lesson with id=%d not found";
    private static final String EXCEPTION_ALL_LESSONS_NOT_FOUND = "nothing to get. Database has no lessons yet";
    private static final String EXCEPTION_LESSON_CREATE = "could not create a new lesson(%s)";
    private static final String OR = " or ";
    private static final String EXCEPTION_TIMETABLE_NOT_FOUND = "timetable with id=%d not found";
    
    @Autowired
    public LessonDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Lesson lesson) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(lesson), holder);
        int obtainedId = holder.getKey().intValue();
        if (obtainedId != 0) {
            return obtainedId;
        } else {
            throw new EntityNotCreatedException(String.format(EXCEPTION_LESSON_CREATE, lesson.getName()));
        }
    }

    @Override
    public Lesson get(int id) {
        try {
            return jdbcTemplate.queryForObject(QUERY_GET_BY_ID, getRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_LESSON_NOT_FOUND, id), ex);
        }
    }

    @Override
    public List<Lesson> getAll() {
        try {
            return jdbcTemplate.query(QUERY_GET_ALL, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_ALL_LESSONS_NOT_FOUND), ex);
        }
    }

    @Override
    public void update(Lesson lesson) {
        try {
            jdbcTemplate.update(QUERY_UPDATE,
                    lesson.getName(),
                    lesson.getStartTime().format(FORMATTER),
                    lesson.getLessonDurationSecond(),
                    lesson.getTeacher().getId(),
                    lesson.getGroup().getId(),
                    lesson.getId());
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_LESSON_NOT_FOUND, lesson.getId()), ex);
        }
    }

    @Override
    public void remove(int id) {
        try {
            jdbcTemplate.update(QUERY_DELETE, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_LESSON_NOT_FOUND, id), ex);
        }
    }
    
    public void setLessonToTimetable(int lessonId, int timetableId) {
        try {
            jdbcTemplate.update(QUERY_INSERT_LESSON_TO_TIMETABLE, lessonId, timetableId, lessonId, timetableId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_LESSON_NOT_FOUND + OR + EXCEPTION_TIMETABLE_NOT_FOUND, lessonId, timetableId), ex);
        }
    }
    
    public List<Lesson> getLessonsOfTimetable(int timetableId) {
        try {
            return jdbcTemplate.query(QUERY_GET_LESSONS_OF_TIMETABLE, getRowMapper(), timetableId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND, timetableId), ex);
        }
    }
    
    public void updateLessonOfTimetable(int lessonId, int timetableId) {
        try {
            jdbcTemplate.update(QUERY_UPDATE_LESSONS_TIMETABLES, lessonId, timetableId, timetableId, lessonId);
            jdbcTemplate.update(QUERY_INSERT_LESSON_TO_TIMETABLE, lessonId, timetableId, lessonId, timetableId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityUpdatingException(String.format(EXCEPTION_LESSON_NOT_FOUND + OR + EXCEPTION_TIMETABLE_NOT_FOUND, lessonId, timetableId), ex);
        }
    }

    private RowMapper<Lesson> getRowMapper() {
        return (resultSet, rowNum) -> {
            Lesson lesson = new Lesson();
            lesson.setId(resultSet.getInt(COLUMN_ID));
            lesson.setName(resultSet.getString(COLUMN_NAME));
            lesson.setStartTime(LocalDateTime.parse(resultSet.getString(COLUMN_START_TIME), FORMATTER));
            lesson.setLessonDurationSecond(resultSet.getInt(COLUMN_DURATION));
            return lesson;
        };
    }
    
    private PreparedStatementCreator getInsertParametredStatement(Lesson lesson) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, lesson.getName());
                statement.setString(2, lesson.getStartTime().format(FORMATTER));
                statement.setInt(3, lesson.getLessonDurationSecond());
                statement.setInt(4, lesson.getTeacher().getId());
                statement.setInt(5, lesson.getGroup().getId());
                return statement;
            }
        };
    }

}
