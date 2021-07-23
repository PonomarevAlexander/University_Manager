package com.foxminded.university.persistence;

import org.springframework.data.repository.CrudRepository;

import com.foxminded.university.domain.models.Group;

public interface GroupRepository extends CrudRepository<Group, Integer> {}
