package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.Entity;

import java.util.List;

public interface GenericDao<E extends Entity<K>, K> extends AutoCloseable {
    List<E> getAll() throws PersistException;

    E getEntityById(K id) throws PersistException;

    void update(E entity) throws PersistException;

    void delete(K id) throws PersistException;

    void insert(E entity) throws PersistException;

    void rollback() throws PersistException;

    void commit() throws PersistException;
}
