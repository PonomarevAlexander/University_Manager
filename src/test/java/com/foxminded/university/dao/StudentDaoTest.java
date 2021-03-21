package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.foxminded.university.models.Student;

class StudentDaoTest {
	
	private StudentDao studentDao;
	
	@BeforeEach
	void init() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
			      .addScript("classpath:test_schema.sql")
			      .addScript("classpath:test_data.sql")
			      .build();
		this.studentDao = new StudentDao(dataSource);
	}
	
	@Test
	void testAdd() {
		studentDao.add(new Student("testName4", "testLastName4", 100));
		Student actual = studentDao.get(4);
		assertEquals(4, actual.getId());
		assertEquals("testName4", actual.getName());
		assertEquals("testLastName4", actual.getLastName());
		assertEquals(100, actual.getAge());
		assertTrue(actual.getGroup() == null);
	}
	
	@Test
	void testGet() {
		Student actual = new Student();
		actual = studentDao.get(1);
		assertEquals(1, actual.getId());
		assertEquals("testName1", actual.getName());
		assertEquals("testLastName1", actual.getLastName());
		assertEquals(11, actual.getAge());
	}
	
	@Test
	void testGetAll() {
		List<Student> actual = new ArrayList<>();
		actual = studentDao.getAll();
		assertEquals(3, actual.size());
		assertEquals("testName1", actual.get(0).getName());
		assertEquals("testName2", actual.get(1).getName());
		assertEquals("testName3", actual.get(2).getName());
	}
	
	@Test
	void testUpdate() {
		Student initial = studentDao.get(1);
		assertEquals("testName1", initial.getName());
		assertEquals("testLastName1", initial.getLastName());
		assertEquals(11, initial.getAge());
		
		initial.setName("testName4");
		initial.setLastName("testLastName4");
		initial.setAge(44);
		studentDao.update(initial);
		
		Student actual = studentDao.get(1);
		assertEquals("testName4", actual.getName());
		assertEquals("testLastName4", actual.getLastName());
		assertEquals(44, actual.getAge());
		assertEquals(1, actual.getId());
	}
	
	@Test
	void testRemove() {
		List<Student> initialList = studentDao.getAll();
		assertEquals(3, initialList.size());
		
		studentDao.remove(3);
		List<Student> actualList = studentDao.getAll();
		assertEquals(2, actualList.size());
	}
	
}
