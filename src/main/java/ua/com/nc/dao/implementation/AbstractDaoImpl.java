package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
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

@Log4j
public abstract class AbstractDaoImpl<E extends Entity> implements GenericDao<E> {

    Connection connection;

    AbstractDaoImpl() {
    }

    AbstractDaoImpl(DataSource dataSource) throws PersistException {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.trace("Error while getting connection ", e);
            throw new PersistException("Error while getting connection ", e);
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
    public E getEntityById(Integer id) throws PersistException {
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
    public void delete(Integer id) throws PersistException {
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

    private Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("ID");
        } else throw new PersistException("No value returned!");
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

    protected String getSelectByIdQuery(){
        throw new PersistException("SelectByIdQuery is not defined");
    }

    protected String getSelectQuery(){
        throw new PersistException("SelectAllQuery is not defined");
    }

    protected String getInsertQuery(){
        throw new PersistException("InsertQuery is not defined");
    }

    protected String getDeleteQuery(){
        throw new PersistException("DeleteQuery is not defined");
    }

    protected String getUpdateQuery(){
        throw new PersistException("UpdateQuery is not defined");
    }

    void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    protected void prepareStatementForInsert(PreparedStatement statement, E entity) throws SQLException{
        throw new PersistException("prepareStatementForInsert method is not defined");
    }

    protected void prepareStatementForUpdate(PreparedStatement statement, E entity) throws SQLException{
        throw new PersistException("prepareStatementForUpdate method is not defined");
    }

    protected abstract List<E> parseResultSet(ResultSet rs) throws SQLException;


    List<E> getFromSqlById(String sql, Integer id) {
        List<E> list;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return list;
    }

    List<E> getListFromSql(String sql) {
        List<E> list;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, string);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
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