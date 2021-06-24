package com.foxminded.university.persistence;

import org.springframework.data.repository.CrudRepository;
import com.foxminded.university.domain.models.Department;

public interface DepartmentsRepository extends CrudRepository<Department, Integer> {}
