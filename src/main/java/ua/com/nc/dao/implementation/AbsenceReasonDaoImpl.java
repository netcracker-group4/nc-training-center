package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AbsenceReasonDao;
import ua.com.nc.domain.AbsenceReason;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class AbsenceReasonDaoImpl extends AbstractDaoImpl<AbsenceReason> implements AbsenceReasonDao {

    @Value("${absence_reason.select-all}")
    private String absenceReasonSelectAll;
    @Value("${absence_reason.select-by-id}")
    private String absenceReasonSelectById;
    @Value("${absence_reason.update}")
    private String absenceReasonUpdate;
    @Value("${absence_reason.delete}")
    private String absenceReasonDelete;
    @Value("${absence_reason.insert}")
    private String absenceReasonInsert;


    public AbsenceReasonDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
                                @Value("${spring.datasource.username}") String DATABASE_USER,
                                @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("ID");
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        return absenceReasonSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return absenceReasonSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return absenceReasonInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return absenceReasonDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return absenceReasonUpdate;
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
        while (rs.next()) {
            Integer id = rs.getInt("ID");
            String title = rs.getString("TITLE");
            AbsenceReason reason = new AbsenceReason(id, title);
            list.add(reason);
        }
        return list;
    }

}
