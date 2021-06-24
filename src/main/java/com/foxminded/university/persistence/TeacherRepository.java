package com.foxminded.university.persistence;

import org.springframework.data.repository.CrudRepository;

import com.foxminded.university.domain.models.Teacher;

public interface TeacherRepository extends CrudRepository<Teacher, Integer>{}
