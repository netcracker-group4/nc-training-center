package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.Entity;

import java.util.List;

/**
 * basic generic interface for all dao-s classes
 * includes basic crud operations
 * @param <E> domain object that must extend Entity ant that represents an relation from the database,
 *           therefore it must have an Integer  primary key
 */
public interface GenericDao<E extends Entity>{
    /**
     * represents the query "select * from table"
     * @return the list of all records converted into objects of the type of param E
     * of the relation of the database
     * @throws PersistException if any exception occurred while retrieving the data
     */
    List<E> getAll() throws PersistException;

    /**
     * returns one object from the database identified by the primary key 'id'
     * @param id value if the primary key of type Integer in the relation
     * @return one object ot the type E that is identified by the argument id
     * @throws PersistException if any exception occurred while retrieving the data
     * or received more then one tuple
     */
    E getEntityById(Integer id) throws PersistException;

    /**
     * represents the query "update table set ... where id = ?"
     * updates all fields of the record in the database
     * @param entity entity whose state will be stored into the record int the database with such id
     * @throws PersistException if any exception occurred while updating the entity
     */
    void update(E entity) throws PersistException;

    /**
     * represents the query "delete from table where id = ?"
     * completely deleted the object from the database
     * @param id the value of the primary key of the record to be deleted
     * @throws PersistException if any exception occurred while deleting the entity
     */
    void delete(Integer id) throws PersistException;

    /**
     * represents the query "insert into table (...) values (...) returning id"
     * inserts new entity which is has no id yet into the table and sets the id
     * with the value of generated id that the database returned
     * @param entity object of the entity to be inserted must have mo id set
     * @throws PersistException if entity in not new (already has an id) or
     * any exception occurred while deleting the entity
     */
    void insert(E entity) throws PersistException;
}
