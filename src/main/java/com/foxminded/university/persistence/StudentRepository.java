package com.foxminded.university.persistence;

import org.springframework.data.repository.CrudRepository;
import com.foxminded.university.domain.models.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {}
