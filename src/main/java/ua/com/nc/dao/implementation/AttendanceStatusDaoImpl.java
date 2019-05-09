package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AttendanceStatusDao;
import ua.com.nc.domain.AttendanceStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class AttendanceStatusDaoImpl extends GenericAbstractDao<AttendanceStatus, Integer> implements AttendanceStatusDao {

    @Value("${attendance_status.select-all}")
    private String attendanceStatusSelectAll;

    public AttendanceStatusDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
                                   @Value("${spring.datasource.username}") String DATABASE_USER,
                                   @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }


    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected String getSelectByIdQuery() {
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return attendanceStatusSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return null;
    }

    @Override
    protected String getDeleteQuery() {
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {

    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, AttendanceStatus entity) throws SQLException {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, AttendanceStatus entity) throws SQLException {

    }

    @Override
    protected List<AttendanceStatus> parseResultSet(ResultSet rs) throws SQLException {
        List<AttendanceStatus> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("ID");
            String title = rs.getString("TITLE");
            AttendanceStatus reason = new AttendanceStatus(id, title);
            list.add(reason);
        }
        return list;
    }

    @Override
    public List<AttendanceStatus> getAll() {
        List<AttendanceStatus> list;
        String sql = attendanceStatusSelectAll;
        log.info(sql + "select all attendance status");
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
}
