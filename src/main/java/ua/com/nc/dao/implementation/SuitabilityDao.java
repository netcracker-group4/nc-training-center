package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ISuitabilityDao;
import ua.com.nc.domain.Suitability;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class SuitabilityDao extends GenericAbstractDao<Suitability, Integer> implements ISuitabilityDao {

    public SuitabilityDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                     @Value("${spring.datasource.username}") String DATABASE_USER,
                     @Value("${spring.datasource.password}") String DATABASE_PASSWORD, SqlQueriesProperties sqlQueriesProperties) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        setSqlQueriesProperties(sqlQueriesProperties);
    }


    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("ID");
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        //TODO Suitability select by id query
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getSuitabilitySelectAll();
    }

    @Override
    protected String getInsertQuery() {
        //TODO Suitability insert query
        return null;
    }

    @Override
    protected String getDeleteQuery() {
        //TODO Suitability delete query
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        //TODO Suitability update query
        return null;
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Suitability entity) throws SQLException {
        //TODO suitability dao prepareStatementForInsert
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Suitability entity) throws SQLException {
        //TODO suitability dao prepareStatementForUpdate
    }

    @Override
    protected List<Suitability> parseResultSet(ResultSet rs) throws SQLException {
        List<Suitability> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("ID");
            String title = rs.getString("title");
            int priority = rs.getInt("priority");
            String color = rs.getString("color");
            list.add(new Suitability(id, title, color,priority));
        }
        return list;
    }
}
