package ua.com.nc.dao.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.GenericDao;
import ua.com.nc.domain.Entity;

import java.sql.*;
import java.util.List;

/**
 * @param <E> entity typesqlQueriesProperties
 * @param <K> type of entity's primary key
 */


public abstract class GenericAbstractDao<E extends Entity<K>, K> implements GenericDao<E, K> {

    Connection connection;
    protected static final Logger log = Logger.getLogger(GenericAbstractDao.class);
//    SqlQueriesProperties sqlQueriesProperties;
//
//    @Autowired
//    public void setSqlQueriesProperties(SqlQueriesProperties sqlQueriesProperties) {
//        this.sqlQueriesProperties = sqlQueriesProperties;
//    }

    GenericAbstractDao() {
    }

    @Autowired
    GenericAbstractDao(String DATABASE_URL,
                       String DATABASE_USER,
                       String DATABASE_PASSWORD) throws PersistException {
        try {
            this.connection = DriverManager.getConnection(DATABASE_URL,
                    DATABASE_USER, DATABASE_PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.trace("Error while setting autocommit false", e);
            throw new PersistException("Error while setting autocommit false", e);
        }
    }

    @Override
    public List<E> getAll() throws PersistException {
        List<E> list;
        String sql = getSelectQuery();
        log.info(sql + "   find all");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace("Error while getting all", e);
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public E getEntityById(K id) throws PersistException {
        List<E> list;
        String sql = getSelectByIdQuery();
        log.info(sql + " select by id with id " + id.toString());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace("Error while getting by id", e);
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            log.trace("Received more than one record.");
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public void update(E entity) throws PersistException {
        String sql = getUpdateQuery();
        log.info(sql + "  update with arguments " + entity.toString());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, entity); // заполнение аргументов запроса оставим на совесть потомков
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new PersistException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(K id) throws PersistException {
        String sql = getDeleteQuery();
        log.info(sql + " delete with id " + id.toString());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, id); // заполнение аргументов запроса оставим на совесть потомков
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void insert(E entity) throws PersistException {
        if (entity.getId() != null) {
            throw new PersistException("Object is already persist.");
        }
        String sql = getInsertQuery();
        log.info(sql + " insert with parameters" + entity.toString());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, entity);
            ResultSet rs = statement.executeQuery();
            entity.setId(parseId(rs));
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    protected abstract K parseId(ResultSet rs) throws SQLException;

    @Override
    public void rollback() throws PersistException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.trace("Error while rolling back", e);
            throw new PersistException("Error while rolling back", e);
        }
    }


    public void commit() throws PersistException {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.trace("Error while committing back", e);
            throw new PersistException("Error while committing back", e);
        }
    }

    @Override
    public void close() throws PersistException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            log.trace("Error while closing connection", e);
            throw new PersistException("Error while closing connection", e);
        }
    }

    protected abstract String getSelectByIdQuery();

    protected abstract String getSelectQuery();

    protected abstract String getInsertQuery();

    protected abstract String getDeleteQuery();

    protected abstract String getUpdateQuery();

    protected abstract void setId(PreparedStatement statement, K id) throws SQLException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, E entity) throws SQLException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, E entity) throws SQLException;

    protected abstract List<E> parseResultSet(ResultSet rs) throws SQLException;

}
