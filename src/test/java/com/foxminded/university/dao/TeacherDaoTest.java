package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.foxminded.university.models.Teacher;

class TeacherDaoTest {

	TeacherDao teacherDao;
	
	private static final String TEST_NAME_1 = "testName1";
	private static final String TEST_NAME_2 = "testName2";
	private static final String TEST_NAME_4 = "testName4";
	private static final String TEST_LAST_NAME_1 = "testLastName1";
	private static final String TEST_LAST_NAME_2 = "testLastName2";
	private static final String TEST_LAST_NAME_4 = "testLastName4";
	private static final String TEST_SCHEMA = "classpath:test_schema.sql";
	private static final String TEST_DATA= "classpath:test_data.sql";
	
	@BeforeEach
	void init() {
		DataSource testDataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript(TEST_SCHEMA)
				.addScript(TEST_DATA)
				.build();
		
		teacherDao = new TeacherDao(testDataSource);
	}
	
	@Test
	void testAdd() {
		teacherDao.add(new Teacher(TEST_NAME_4, TEST_LAST_NAME_4));
		Teacher actual = teacherDao.get(4);
		assertEquals(4, actual.getId());
		assertEquals(TEST_NAME_4, actual.getName());
		assertEquals(TEST_LAST_NAME_4, actual.getLastName());
	}
	
	@Test
	void testGet() {
		Teacher actual = teacherDao.get(2);
		assertEquals(2, actual.getId());
		assertEquals(TEST_NAME_2, actual.getName());
		assertEquals(TEST_LAST_NAME_2, actual.getLastName());
	}
	
	@Test
	void testGetAll() {
		List<Teacher> actual = teacherDao.getAll();
		assertEquals(3, actual.size());
	}
	
	@Test
	void testUpdate() {
		Teacher teacher = teacherDao.get(1);
		assertEquals(1, teacher.getId());
		assertEquals(TEST_NAME_1, teacher.getName());
		assertEquals(TEST_LAST_NAME_1, teacher.getLastName());
		
		teacher.setName(TEST_NAME_4);
		teacher.setLastName(TEST_LAST_NAME_4);
		teacherDao.update(teacher);
		
		Teacher actual = teacherDao.get(1);
		assertEquals(1, actual.getId());
		assertEquals(TEST_NAME_4, actual.getName());
		assertEquals(TEST_LAST_NAME_4, actual.getLastName());
	}
	
	@Test
	void testRemove() {
		List<Teacher> initial = teacherDao.getAll();
		teacherDao.remove(1);
		List<Teacher> actual = teacherDao.getAll();
		
		assertEquals(3, initial.size());
		assertEquals(2, actual.size());
	}
}
