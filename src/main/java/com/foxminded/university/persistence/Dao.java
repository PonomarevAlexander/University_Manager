package com.foxminded.university.persistence;

import java.util.List;

public interface Dao<T> {

    void add(T entity);

    T get(int id);

    List<T> getAll();

    void update(T entity);

    void remove(int id);

}
