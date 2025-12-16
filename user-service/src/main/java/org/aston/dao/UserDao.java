package org.aston.dao;

import java.util.List;
import java.util.Optional;

public interface UserDao<T> {
    Optional<T> get(long id);
    List<T> getAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}
