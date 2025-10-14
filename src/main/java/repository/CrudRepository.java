package repository;

import java.util.List;

public interface CrudRepository<T> {
    List<T> findAll();
    T findById(int id);
    T save(T item);
    T update(int id, T item);
    T delete(int id);
}
