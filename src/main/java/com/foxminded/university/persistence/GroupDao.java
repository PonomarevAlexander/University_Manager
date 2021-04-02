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
import com.foxminded.university.domain.models.Group;

@Repository
public class GroupDao implements Dao<Group> {

    private JdbcTemplate jdbcTemplate;

    private static final String QUERY_INSERT = "insert into groups(name) values(?)";
    private static final String QUERY_SELECT_BY_ID = "select * from groups where id=?";
    private static final String QUERY_SELECT_ALL = "select * from groups";
    private static final String QUERY_SELECT_GROUP_BY_STUDENT = "select g.* from groups g left join students_groups sg on g.id=sg.group_id where sg.student_id=?";
    private static final String QUERY_UPDATE = "update groups set name=? where id=?";
    private static final String QUERY_DELETE = "delete from groups where id=?";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    @Autowired
    public GroupDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int add(Group group) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertParametredStatement(group), holder);
        return (int) holder.getKey().longValue();
    }

    @Override
    public Group get(int id) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, getRowMapper(), id);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(QUERY_SELECT_ALL, getRowMapper());
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(QUERY_UPDATE, group.getName(), group.getId());
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(QUERY_DELETE, id);
    }

    public Group getStudentGroup(int studentId) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_GROUP_BY_STUDENT, getRowMapper(), studentId);
    }
    
    private RowMapper<Group> getRowMapper() {
        return (resultSet, rowNum) -> {
            Group group = new Group();
            group.setId(resultSet.getInt(COLUMN_ID));
            group.setName(resultSet.getString(COLUMN_NAME));
            return group;
        };
    }
    
    private PreparedStatementCreator getInsertParametredStatement(Group group) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, group.getName());
                return statement;
            }
        };
    }

    public Group getGroupByLesson(int id) {
        // TODO Auto-generated method stub
        return null;
    }

}
