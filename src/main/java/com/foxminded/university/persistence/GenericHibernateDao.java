package com.foxminded.university.persistence;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericHibernateDao<T> extends AbstractHibernateDao<T> implements Dao<T> {

    //there is generic implementation
}
