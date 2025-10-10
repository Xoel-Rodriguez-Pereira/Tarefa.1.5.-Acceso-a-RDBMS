package dev.joseluisgs.repositories;

import java.util.List;

public interface CrudRepository<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T item);
    T update(ID id, T item);
    T delete(ID id);
}
