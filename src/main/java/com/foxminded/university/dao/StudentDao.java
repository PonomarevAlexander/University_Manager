package com.foxminded.university.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.foxminded.university.models.Student;

@Repository
public class StudentDao implements Dao<Student> {

    private JdbcTemplate jdbcTemplate;

    private static final String QUERY_INSERT = "INSERT INTO students(name, last_name, age) VALUES(?, ?, ?)";
    private static final String QUERY_SELECT_BY_ID = "select * from students where id=?";
    private static final String QUERY_SELECT_ALL = "Select * from students";
    private static final String QUERY_SELECT_STUDENTS_RELATED_GROUP = "select st.* from students st left join groups gr on gr.id=st.group_id where st.group_id=?";
    private static final String QUERY_UPDATE = "update students set name=?, last_name=?, age=? where id=?";
    private static final String QUERY_UPDATE_GROUP_ID = "update students set group_id=? where id=?";
    private static final String QUERY_DELETE_BY_ID = "delete from students where id=?";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_ID = "id";

    private final RowMapper<Student> studentRowMapper = (resultSet, rowNum) -> {
        Student student = new Student();
        student.setId(resultSet.getInt(COLUMN_ID));
        student.setName(resultSet.getString(COLUMN_NAME));
        student.setLastName(resultSet.getString(COLUMN_LAST_NAME));
        student.setAge(resultSet.getInt(COLUMN_AGE));
        return student;
    };

    @Autowired
    public StudentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(Student entity) {
        jdbcTemplate.update(QUERY_INSERT, entity.getName(), entity.getLastName(), entity.getAge());
    }

    @Override
    public Student get(int id) {
        return jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, studentRowMapper, id);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(QUERY_SELECT_ALL, studentRowMapper);
    }

    @Override
    public void update(Student entity) {
        jdbcTemplate.update(QUERY_UPDATE, entity.getName(), entity.getLastName(), entity.getAge(), entity.getId());
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(QUERY_DELETE_BY_ID, id);
    }

    public List<Student> getStudentRelatedGroup(int groupId) {
        return jdbcTemplate.query(QUERY_SELECT_STUDENTS_RELATED_GROUP, studentRowMapper, groupId);
    }

    public void setGroupToStudent(int studentId, int groupId) {
        jdbcTemplate.update(QUERY_UPDATE_GROUP_ID, groupId, studentId);
    }
}
