package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IAbsenceReasonDao;
import ua.com.nc.domain.AbsenceReason;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AbsenceReasonDao extends GenericAbstractDao<AbsenceReason, Integer> implements IAbsenceReasonDao {

    public AbsenceReasonDao(@Value("${spring.datasource.url}") String DATABASE_URL,
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
        return sqlQueriesProperties.getAbsenceReasonSelectById();
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getAbsenceReasonSelectAll();
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getAbsenceReasonInsert();
    }

    @Override
    protected String getDeleteQuery() {
        return sqlQueriesProperties.getAbsenceReasonDelete();
    }

    @Override
    protected String getUpdateQuery() {
        return sqlQueriesProperties.getAbsenceReasonUpdate();
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, AbsenceReason entity) throws SQLException {
        statement.setString(1, entity.getTitle());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, AbsenceReason entity) throws SQLException {
        statement.setString(1, entity.getTitle());
        statement.setInt(2, entity.getId());
    }

    @Override
    protected List<AbsenceReason> parseResultSet(ResultSet rs) throws SQLException {
        List<AbsenceReason> list = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("ID");
            String title = rs.getString("TITLE");
            AbsenceReason reason = new  AbsenceReason(id, title);
            list.add(reason);
        }
        return list;
    }

}
