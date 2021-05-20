package com.foxminded.university.domain.services;

import java.util.List;

public interface Service<T> {
    
    int add(T entity);
    
    T getById(int id);
    
    List<T> getAll();
    
    void update(T entity);
    
    void remove(int id);
}
