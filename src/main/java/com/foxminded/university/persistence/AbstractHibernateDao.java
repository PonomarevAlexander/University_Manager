package com.foxminded.university.persistence;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHibernateDao<T> {

    private Class<T> clazz;
    private SessionFactory sessionFactory;
    
    public void add(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }
    
    public T get(int id) {
        return sessionFactory.getCurrentSession().get(clazz, id);
    }
    
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + clazz.getName()).list();
    }
    
    public void update(T entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }
    
    public void remove(int id) {
        sessionFactory.getCurrentSession().delete(get(id));
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    
}
