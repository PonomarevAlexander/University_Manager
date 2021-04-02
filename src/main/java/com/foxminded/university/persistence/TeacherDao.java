package com.foxminded.university.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.foxminded.university.domain.models.Teacher;

@Repository
public class TeacherDao implements Dao<Teacher> {

    private JdbcTemplate jdbcTemplate;
    
    private static final String QUERY_INSERT = "insert into teachers(name, last_name) values(?, ?)";
    private static final String QUERY_INSERT_TEACHER_TO_GROUP = "insert into groups_teachers(group_id, teacher_id) values(?, ?)";
    private static final String QUERY_GET_BY_ID = "select * from teachers where id=?";
    private static final String QUERY_GET_ALL = "select * from teachers";
    private static final String QUERY_GET_TEACHER_BY_LESSON = "select t.* from teachers t left join teachers_lessons tl on t.id=tl.teachers_id where tl.lesson_id=?";
    private static final String QUERY_GET_TEACHER_BY_GROUP = "select t.* from teachers t left join groups_teachers gt on t.id=gt.teacher_id where gt.group_id=?";
    private static final String QUERY_UPDATE = "update teachers set name=?, last_name=? where id=?";
    private static final String QUERY_UPDATE_TEACHERS_GROUPS = "update groups_teachers set teacher_id=? where group_id=?";
    private static final String QUERY_DELETE = "delete from teachers where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LAST_NAME = "last_name";

    @Autowired
    public TeacherDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Teacher teacher) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(teacher), holder);
        return (int) holder.getKey().longValue();
    }

    @Override
    public Teacher get(int id) {
        return jdbcTemplate.queryForObject(QUERY_GET_BY_ID, getRowMapper(), id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(QUERY_GET_ALL, getRowMapper());
    }

    @Override
    public void update(Teacher teacher) {
        jdbcTemplate.update(QUERY_UPDATE, teacher.getName(), teacher.getLastName(), teacher.getId());
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(QUERY_DELETE, id);
    }
    
    public Teacher getTeacherByLessonId(int lessonId) {
        return jdbcTemplate.queryForObject(QUERY_GET_TEACHER_BY_LESSON, getRowMapper(), lessonId);
    }
    
    public Teacher getGroupTeacher(int groupId) {
        return jdbcTemplate.queryForObject(QUERY_GET_TEACHER_BY_GROUP, getRowMapper(), groupId);
    }
    
    public void assignTeacherToGroup(int teacherId, int groupId) {
        jdbcTemplate.update(QUERY_INSERT_TEACHER_TO_GROUP, groupId, teacherId);
    }
    
    public void updateGroupTeacher(int teacherId, int groupId) {
        jdbcTemplate.update(QUERY_UPDATE_TEACHERS_GROUPS, teacherId, groupId);
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
