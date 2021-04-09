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
    private static final String QUERY_INSERT_DEPARTMENT = "insert into groups(department_id) values(?) where groups.id=? on conflict do nonthing";
    private static final String QUERY_INSERT_TEACHER_AS_GROUPHEAD = "insert into groups(head) values(?) where id=?";
    private static final String QUERY_UPDATE_GROUPHEAD = "update groups set head=? where id=?";
    private static final String QUERY_SELECT_BY_ID = "select * from groups where id=?";
    private static final String QUERY_SELECT_ALL = "select * from groups";
    private static final String QUERY_SELECT_GROUP_BY_STUDENT = "select g.* from groups g left join students s on g.id=s.group_id where s.id=?";
    private static final String QUERY_SELECT_GROUP_BY_LESSON = "select g.* from groups g left join lessons l on l.group_id=g.id where l.id=?";
    private static final String QUERY_SELECT_GROUP_BY_DEPARTMENT = "select * from groups where groups.department_id=?";
    private static final String QUERY_SELECT_GROUP_BY_TEACHER = "select * from groups where head=?";
    private static final String QUERY_UPDATE = "update groups set name=? where id=?";
    private static final String QUERY_UPDATE_DEPARTMENT = "update groups set department_id=? where groups.id=?";
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
    
    public Group getGroupByLesson(int lessonId) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_GROUP_BY_LESSON, getRowMapper(), lessonId);
    }
    
    public List<Group> getGroupsByDepartment(int departmentId) {
        return jdbcTemplate.query(QUERY_SELECT_GROUP_BY_DEPARTMENT, getRowMapper(), departmentId);
    }
    
    public void setDepartmentToGroup(int departmentId, int groupId) {
        jdbcTemplate.update(QUERY_INSERT_DEPARTMENT, departmentId, groupId);
    }
    
    public void updateGroupDepartment(int departmentId, int groupId) {
        jdbcTemplate.update(QUERY_UPDATE_DEPARTMENT, departmentId, groupId);
    }
    
    public Group getGroupRelatedTeacher(int teacherId) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_GROUP_BY_TEACHER, getRowMapper(), teacherId);
    }
    
    public void setTeacherToGroupHead(int teacherId, int groupId) {
        jdbcTemplate.update(QUERY_INSERT_TEACHER_AS_GROUPHEAD, teacherId, groupId);
    }
    
    public void updateGroupHead(int teacherId, int groupId) {
        jdbcTemplate.update(QUERY_UPDATE_GROUPHEAD, teacherId, groupId);
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
}
