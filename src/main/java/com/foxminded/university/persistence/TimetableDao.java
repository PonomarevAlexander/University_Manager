package com.foxminded.university.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.foxminded.university.domain.models.Timetable;

@Repository
public class TimetableDao implements Dao<Timetable> {

    private JdbcTemplate jdbcTemplate;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String QUERY_INSERT = "insert into timetables(creation_date) values(?)";
    private static final String QUERY_SELECT_BY_ID = "select * from timetables where id=?";
    private static final String QUERY_SELECT_ALL = "select * from timetables";
    private static final String QUERY_SELECT_TIMETABLE_BY_TEACHER = "select t.* from timetables t left join timetables_teachers tt on t.id=tt.timetable_id where tt.teacher_id=?";
    private static final String QUERY_UPDATE = "update timetables set creation_date=?";
    private static final String QUERY_DELETE_BY_ID = "delete from timetables where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CREATION_DATE = "creation_date";

    @Autowired
    public TimetableDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(Timetable timetable) {
        jdbcTemplate.update(QUERY_INSERT, timetable.getCreationDate().format(FORMATTER));
    }

    @Override
    public Timetable get(int id) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
    }

    @Override
    public List<Timetable> getAll() {
        return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
    }

    @Override
    public void update(Timetable timetable) {
        jdbcTemplate.update(QUERY_UPDATE, timetable.getCreationDate().format(FORMATTER));
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(QUERY_DELETE_BY_ID, id);
    }
    
    public Timetable getTimetableRelatedTeacher(int teacherId) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_TIMETABLE_BY_TEACHER, getRowMapper(), teacherId);
    }
    
    private RowMapper<Timetable> getRowMapper() {
        return (resultSet, rowNum) -> {
            Timetable timetable = new Timetable();
            timetable.setId(resultSet.getInt(COLUMN_ID));
            timetable.setCreationDate(LocalDateTime.parse(resultSet.getString(COLUMN_CREATION_DATE), FORMATTER));
            return timetable;
        };
    }

}
