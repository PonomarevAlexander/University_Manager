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

import com.foxminded.university.domain.exceptions.DaoException;
import com.foxminded.university.domain.models.Timetable;

@Repository
public class TimetableDao implements Dao<Timetable> {

    private JdbcTemplate jdbcTemplate;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String QUERY_INSERT = "insert into timetables(creation_date, description) values(?, ?)";
    private static final String QUERY_INSERT_TIMETABLE_TO_GROUP = "insert into timetables_groups(timetable_id, group_id) values(?, ?)";
    private static final String QUERY_INSERT_TIMETABLE_TO_TEACHER = "insert into timetables_teachers(timetable_id, teacher_id) values(?, ?)";
    private static final String QUERY_SELECT_BY_ID = "select * from timetables where id=?";
    private static final String QUERY_SELECT_ALL = "select * from timetables";
    private static final String QUERY_SELECT_TIMETABLE_BY_TEACHER = "select t.* from timetables t left join timetables_teachers tt on t.id=tt.timetable_id where tt.teacher_id=?";
    private static final String QUERY_SELECT_TIMETABLE_BY_GROUP = "select t.* from timetables t left join timetables_groups tg on t.id=tg.timetable_id where tg.group_id=?";
    private static final String QUERY_UPDATE = "update timetables set creation_date=?, description=? where id=?";
    private static final String QUERY_UPDATE_TIMETABLES_GROUPS = "update timetables_groups set timetable_id=? where group_id=?";
    private static final String QUERY_UPDATE_TIMETABLES_TEACHERS = "update timetables_teachers set timetable_id=? where teacher_id=?";
    private static final String QUERY_DELETE_BY_ID = "delete from timetables where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CREATION_DATE = "creation_date";
    private static final String EXCEPTION_TEACHER_NOT_FOUND = "teacher with id=%d not found";
    private static final String EXCEPTION_ALL_TIMETABLE_NOT_FOUND = "nothing to get. Database has no timetables yet";
    private static final String EXCEPTION_TIMETABLE_NOT_FOUND = "timetable with id=%d not found";
    private static final String EXCEPTION_GROUP_NOT_FOUND = "group with id=%d not found";
    private static final String EXCEPTION_TIMETABLE_CREATE = "could not create a new timetable";
    private static final String OR = " or ";

    @Autowired
    public TimetableDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Timetable timetable) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(timetable), holder);
        int obtainedId = (int) holder.getKeys().get("id");
        if (obtainedId != 0) {
            return obtainedId;
        } else {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_CREATE));
        }
    }

    @Override
    public Timetable get(int id) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND, id), ex);
        }
    }

    @Override
    public List<Timetable> getAll() {
        try {
            return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_ALL_TIMETABLE_NOT_FOUND), ex);
        }
    }

    @Override
    public void update(Timetable timetable) {
        try {
            jdbcTemplate.update(QUERY_UPDATE, timetable.getCreationDate().format(FORMATTER), timetable.getDescription(), timetable.getId());
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND, timetable.getId()), ex);
        }
    }

    @Override
    public void remove(int id) {
        try {
            jdbcTemplate.update(QUERY_DELETE_BY_ID, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND, id), ex);
        }
    }
    
    public Timetable getTimetableRelatedTeacher(int teacherId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_TIMETABLE_BY_TEACHER, getRowMapper(), teacherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TEACHER_NOT_FOUND, teacherId), ex);
        }
    }
    
    public void setTimetableToTeacher(int timetableId, int teacherId) {
        try {
            jdbcTemplate.update(QUERY_INSERT_TIMETABLE_TO_TEACHER, timetableId, teacherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND + OR + EXCEPTION_TEACHER_NOT_FOUND, timetableId, teacherId), ex);
        }
    }
    
    public Timetable getTimetableRelatedGroup(int groupId) {
        try {
            return jdbcTemplate.queryForObject(QUERY_SELECT_TIMETABLE_BY_GROUP, getRowMapper(), groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_GROUP_NOT_FOUND,  groupId), ex);
        }
    }
    
    public void setTimetableToGroup(int timetableId, int groupId) {
        try {
            jdbcTemplate.update(QUERY_INSERT_TIMETABLE_TO_GROUP, timetableId, groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND + OR + EXCEPTION_GROUP_NOT_FOUND, timetableId, groupId), ex);
        }
    }
    
    public void updateTimetableRelatedGroup(int timetableId, int groupId) {
        try {
            jdbcTemplate.update(QUERY_UPDATE_TIMETABLES_GROUPS, timetableId, groupId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND + OR + EXCEPTION_GROUP_NOT_FOUND, timetableId, groupId), ex);
        }
    }
    
    public void updateTimetableRelatedTeacher(int timetableId, int teacherId) {
        try {
            jdbcTemplate.update(QUERY_UPDATE_TIMETABLES_TEACHERS, timetableId, teacherId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException(String.format(EXCEPTION_TIMETABLE_NOT_FOUND + OR + EXCEPTION_TEACHER_NOT_FOUND, timetableId, teacherId), ex);
        }
    }
    
    private RowMapper<Timetable> getRowMapper() {
        return (resultSet, rowNum) -> {
            Timetable timetable = new Timetable();
            timetable.setId(resultSet.getInt(COLUMN_ID));
            timetable.setCreationDate(LocalDateTime.parse(resultSet.getString(COLUMN_CREATION_DATE), FORMATTER));
            timetable.setDescription(resultSet.getString(COLUMN_DESCRIPTION));
            return timetable;
        };
    }
    
    private PreparedStatementCreator getInsertParametredStatement(Timetable timetable) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, timetable.getCreationDate().format(FORMATTER));
                statement.setString(2, timetable.getDescription());
                return statement;
            }
        };
    }

}
