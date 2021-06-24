package com.foxminded.university.persistence;

import org.springframework.data.repository.CrudRepository;

import com.foxminded.university.domain.models.Lesson;

public interface LessonRepository extends CrudRepository<Lesson, Integer> {}
