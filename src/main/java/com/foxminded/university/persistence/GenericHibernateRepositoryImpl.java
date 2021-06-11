package com.foxminded.university.persistence;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericHibernateRepositoryImpl<T> extends AbstractHibernateRepository<T> implements UniversityRepository<T> {}
