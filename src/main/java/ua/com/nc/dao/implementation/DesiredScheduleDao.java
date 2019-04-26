package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IDesiredScheduleDao;
import ua.com.nc.domain.DesiredSchedule;
import ua.com.nc.domain.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DesiredScheduleDao extends GenericAbstractDao<DesiredSchedule, Integer> implements IDesiredScheduleDao {

    public DesiredScheduleDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                     @Value("${spring.datasource.username}") String DATABASE_USER,
                     @Value("${spring.datasource.password}") String DATABASE_PASSWORD,
                              SqlQueriesProperties sqlQueriesProperties) throws PersistException {
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
        //TODO getSelectByIdQuery DesiredScheduleDao
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getDesirableScheduleSelectAll();
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getDesirableScheduleInsert();
    }

    @Override
    protected String getDeleteQuery() {
        //TODO getDeleteQuery DesiredScheduleDao
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        //TODO getUpdateQuery DesiredScheduleDao
        return null;
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, DesiredSchedule entity) throws SQLException {
        statement.setInt(1, entity.getUserId());
        statement.setInt(2, entity.getCourseId());
        statement.setString(3, entity.getCronInterval());
        statement.setInt(4, entity.getSuitability());

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, DesiredSchedule entity) throws SQLException {
        //TODO prepareStatementForUpdate DesiredScheduleDao
    }

    @Override
    protected List<DesiredSchedule> parseResultSet(ResultSet rs) throws SQLException {
        List<DesiredSchedule> list = new ArrayList<>();
        while (rs.next()){
            int id = rs.getInt("id");
            int userId = rs.getInt("user_id");
            int courseId = rs.getInt("course_id");
            String cronInterval = rs.getString("cron_interval");
            int suitability = rs.getInt("suitability");
            list.add(new DesiredSchedule(id, userId, courseId, cronInterval, suitability));
        }
        return list;
    }

    @Override
    public List<DesiredSchedule> getAllForCourse(int courseId) {
        List<DesiredSchedule> list;
        String sql = sqlQueriesProperties.getDesirableScheduleSelectByCourseId();
        log(sql, "find all by level");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return list;
    }
}