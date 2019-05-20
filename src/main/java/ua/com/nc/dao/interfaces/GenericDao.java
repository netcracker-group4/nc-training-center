package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.Entity;

import java.util.List;

public interface GenericDao<E extends Entity>{
    List<E> getAll() throws PersistException;

    E getEntityById(Integer id) throws PersistException;

    void update(E entity) throws PersistException;

    void delete(Integer id) throws PersistException;

    void insert(E entity) throws PersistException;
}
