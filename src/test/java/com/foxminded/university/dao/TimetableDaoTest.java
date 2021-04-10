package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.persistence.TimetableDao;

class TimetableDaoTest {

    private TimetableDao timetableDao;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String TEST_TIME_1 = "2021-01-11 11:11:11";
    private static final String TEST_TIME_4 = "2021-04-14 14:14:14";
    private static final String TEST_SCHEMA = "classpath:test_schema.sql";
    private static final String TEST_DATA = "classpath:test_data.sql";

    @BeforeEach
    void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(TEST_SCHEMA)
                .addScript(TEST_DATA)
                .build();

        timetableDao = new TimetableDao(dataSource);
    }

    @Test
    void testAdd() {
        Timetable timetable = new Timetable();
        timetable.setCreationDate(LocalDateTime.parse(TEST_TIME_4, FORMATTER));
        timetableDao.add(timetable);
        Timetable actual = timetableDao.get(4);

        assertEquals(4, actual.getId());
        assertEquals(TEST_TIME_4, actual.getCreationDate().format(FORMATTER));
    }

    @Test
    void testGet() {
        Timetable actual = timetableDao.get(1);

        assertEquals(1, actual.getId());
        assertEquals(TEST_TIME_1, actual.getCreationDate().format(FORMATTER));
    }

    @Test
    void testGetAll() {
        List<Timetable> actual = timetableDao.getAll();
        assertEquals(3, actual.size());
    }

    @Test
    void testUpdate() {
        Timetable initial = timetableDao.get(1);
        assertEquals(1, initial.getId());
        assertEquals(TEST_TIME_1, initial.getCreationDate().format(FORMATTER));

        initial.setCreationDate(LocalDateTime.parse(TEST_TIME_4, FORMATTER));
        timetableDao.update(initial);

        Timetable actual = timetableDao.get(1);
        assertEquals(1, actual.getId());
        assertEquals(TEST_TIME_4, actual.getCreationDate().format(FORMATTER));
    }

    @Test
    void testRemove() {
        List<Timetable> initial = timetableDao.getAll();
        timetableDao.remove(1);
        List<Timetable> actual = timetableDao.getAll();

        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }
}
