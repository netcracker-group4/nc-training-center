package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.GenericDao;
import ua.com.nc.domain.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @param <E> entity typeSqlQueriesProperties
 */

@Log4j2
public abstract class AbstractDaoImpl<E extends Entity> implements GenericDao<E> {

    protected DataSource dataSource;

    AbstractDaoImpl() {
    }

    AbstractDaoImpl(DataSource dataSource) throws PersistException {
        this.dataSource = dataSource;
    }

    /**
     * default implementation of getAll() method on GenericDao interface
     *
     * @return list of all entities in the table
     * @throws PersistException in case of the exception
     */
    @Override
    public List<E> getAll() throws PersistException {
        List<E> list;
        String sql = getSelectQuery();
        log.info("find all   " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.error("Error while getting all", e);
            throw new PersistException(e);
        }
        return list;
    }

    /**
     * default implementation
     *
     * @param id value if the primary key of type Integer in the relation
     * @return single entity in case of success and null in case if entity not found
     * @throws PersistException in case of the exception or if received more then one entity
     */
    @Override
    public E getEntityById(Integer id) throws PersistException {
        List<E> list;
        String sql = getSelectByIdQuery();
        log.info(" select by id with id " + id.toString() + "  " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.error("Error while getting by id", e);
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            log.error("Received more than one record.");
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    /**
     * default implementation
     *
     * @param entity entity whose state will be stored into the record int the database with such id
     * @throws PersistException in case of the exception or if updated more then one record
     */
    @Override
    public void update(E entity) throws PersistException {
        String sql = getUpdateQuery();
        log.info("update with arguments  " + entity.toString() + "  " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, entity);
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new PersistException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    /**
     * default implementation
     *
     * @param id the value of the primary key of the tuple to be deleted
     * @throws PersistException in case of the exception or if deleted more then one record
     */
    @Override
    public void delete(Integer id) throws PersistException {
        String sql = getDeleteQuery();
        log.info("delete with id " + id.toString() + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, id);
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    /**
     * default implementation
     *
     * @param entity object of the entity to be inserted must have mo id set
     * @throws PersistException in case of the exception
     */
    @Override
    public void insert(E entity) throws PersistException {
        if (entity.getId() != null) {
            log.error("trying to insert object that already persist" + entity);
            throw new PersistException("Object is already persist.");
        }
        String sql = getInsertQuery();
        log.info("insert with parameters" + entity.toString() + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, entity);
            ResultSet rs = statement.executeQuery();
            entity.setId(parseId(rs));
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    /**
     * parses returned generated id of new inserted entity
     *
     * @param rs result set returned by the insert query
     * @return parsed id
     * @throws SQLException if there is no value returned
     */
    private Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("ID");
        } else throw new PersistException("No value returned!");
    }

    /**
     * default implementation of  getSelectByIdQuery  throws PersistException
     * if not overridden by extending class
     *
     * @return string
     */
    protected String getSelectByIdQuery() {
        throw new PersistException("SelectByIdQuery is not defined");
    }

    /**
     * default implementation of  getSelectQuery  throws PersistException
     * if not overridden by extending class
     *
     * @return string
     */
    protected String getSelectQuery() {
        throw new PersistException("SelectAllQuery is not defined");
    }

    /**
     * default implementation of  getInsertQuery  throws PersistException
     * if not overridden by extending class
     *
     * @return string
     */
    protected String getInsertQuery() {
        throw new PersistException("InsertQuery is not defined");
    }

    /**
     * default implementation of  getDeleteQuery  throws PersistException
     * if not overridden by extending class
     *
     * @return string
     */
    protected String getDeleteQuery() {
        throw new PersistException("DeleteQuery is not defined");
    }

    /**
     * default implementation of  getUpdateQuery  throws PersistException
     * if not overridden by extending class
     *
     * @return string
     */
    protected String getUpdateQuery() {
        throw new PersistException("UpdateQuery is not defined");
    }

    /**
     * sets id to the statement if it is the first argument
     *
     * @param statement prepared statement
     * @param id        id to be set
     * @throws SQLException in case of error
     */
    void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    /**
     * default implementation of  prepareStatementForInsert  throws PersistException
     * if not overridden by extending class
     *
     * @param statement statement with placeholders without the id
     * @param entity    entity, whose fields to be set to the statement
     * @throws SQLException in case of error
     */
    protected void prepareStatementForInsert(PreparedStatement statement, E entity) throws SQLException {
        throw new PersistException("prepareStatementForInsert method is not defined");
    }

    /**
     * default implementation of  prepareStatementForUpdate  throws PersistException
     * if not overridden by extending class
     *
     * @param statement statement with placeholders with the id as the last parameter
     * @param entity    entity, whose fields to be set to the statement
     * @throws SQLException in case of error
     */
    protected void prepareStatementForUpdate(PreparedStatement statement, E entity) throws SQLException {
        throw new PersistException("prepareStatementForUpdate method is not defined");
    }

    protected abstract List<E> parseResultSet(ResultSet rs) throws SQLException;


    List<E> getFromSqlById(String sql, Integer id) {
        List<E> list;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        return list;
    }

    List<E> getFromSqlByTwoId(String sql, Integer id1, Integer id2) {
        List<E> list;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id1);
            statement.setInt(2, id2);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        return list;
    }

    List<E> getListFromSql(String sql) {
        List<E> list;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        return list;
    }

    E getUniqueFromSqlById(String sql, Integer id) {
        List<E> list = getFromSqlById(sql, id);
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    E getUniqueFromSqlByString(String sql, String string) {
        List<E> list;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, string);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

}
