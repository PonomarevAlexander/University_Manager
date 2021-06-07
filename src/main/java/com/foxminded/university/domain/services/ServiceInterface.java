package com.foxminded.university.domain.services;

import java.util.List;

public interface ServiceInterface<T> {
    
    void add(T entity);
    
    T getById(int id);
    
    List<T> getAll();
    
    void update(T entity);
    
    void remove(int id);
}
