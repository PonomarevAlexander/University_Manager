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

import com.foxminded.university.models.Lesson;

class LessonDaoTest {

    private LessonDao lessonDao;

    private static final String TEST_NAME_1 = "testName1";
    private static final String TEST_NAME_4 = "testName4";
    private static final String TEST_TIME_1 = "2021-01-11 11:11:11";
    private static final String TEST_TIME_4 = "2021-04-14 14:14:14";
    private static final int TEST_DURATION = 5400;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach()
    void init() {
        DataSource testDataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test_schema.sql").addScript("classpath:test_data.sql").build();

        lessonDao = new LessonDao(testDataSource);
    }

    @Test
    void testAdd() {
        lessonDao.add(new Lesson(TEST_NAME_4, LocalDateTime.parse(TEST_TIME_4, FORMATTER), TEST_DURATION));
        Lesson actual = lessonDao.get(4);

        assertEquals(4, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(TEST_TIME_4, actual.getStartTime().format(FORMATTER));
        assertEquals(TEST_DURATION, actual.getLessonDurationSecond());
    }

    @Test
    void testGet() {
        Lesson actual = lessonDao.get(1);

        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_1, actual.getName());
        assertEquals(TEST_TIME_1, actual.getStartTime().format(FORMATTER));
        assertEquals(TEST_DURATION, actual.getLessonDurationSecond());
    }

    @Test
    void testGetAll() {
        List<Lesson> lessonsList = lessonDao.getAll();
        assertEquals(3, lessonsList.size());
    }

    @Test
    void testUpdate() {
        Lesson initial = lessonDao.get(1);
        initial.setName(TEST_NAME_4);
        initial.setStartTime(LocalDateTime.parse(TEST_TIME_4, FORMATTER));
        initial.setLessonDurationSecond(TEST_DURATION);

        lessonDao.update(initial);
        Lesson actual = lessonDao.get(1);

        assertEquals(1, actual.getId());
        assertEquals(TEST_NAME_4, actual.getName());
        assertEquals(LocalDateTime.parse(TEST_TIME_4, FORMATTER), actual.getStartTime());
        assertEquals(TEST_DURATION, actual.getLessonDurationSecond());
    }

    @Test
    void testRemove() {
        List<Lesson> initial = lessonDao.getAll();
        lessonDao.remove(1);
        List<Lesson> actual = lessonDao.getAll();

        assertEquals(3, initial.size());
        assertEquals(2, actual.size());
    }
}
