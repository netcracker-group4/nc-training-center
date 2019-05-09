package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.DesiredScheduleDao;
import ua.com.nc.domain.DesiredSchedule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class DesiredScheduleDaoImpl extends GenericAbstractDao<DesiredSchedule, Integer> implements DesiredScheduleDao {

    @Value("${desirable.schedule.select-all}")
    private String desirableScheduleSelectAll;
    @Value("${desirable.schedule.insert}")
    private String desirableScheduleInsert;
    @Value("${desirable.schedule.select-by-course-id}")
    private String desirableScheduleSelectByCourseId;
    @Value("${desirable.schedule.select-by-group-id}")
    private String desirableScheduleSelectByGroupId;

    public DesiredScheduleDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
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
        //TODO getSelectByIdQuery DesiredScheduleDaoImpl
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return desirableScheduleSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return desirableScheduleInsert;
    }

    @Override
    protected String getDeleteQuery() {
        //TODO getDeleteQuery DesiredScheduleDaoImpl
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        //TODO getUpdateQuery DesiredScheduleDaoImpl
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
    protected void prepareStatementForUpdate(PreparedStatement statement, DesiredSchedule entity) {
        //TODO prepareStatementForUpdate DesiredScheduleDaoImpl
    }

    @Override
    protected List<DesiredSchedule> parseResultSet(ResultSet rs) throws SQLException {
        List<DesiredSchedule> list = new ArrayList<>();
        while (rs.next()) {
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
        String sql = desirableScheduleSelectByCourseId;
        log.info(sql + "" + "find all by courseId " + courseId);
        return getFromSqlById(courseId, sql);
    }

    @Override
    public List<DesiredSchedule> getAllForGroup(int groupId) {
        String sql = desirableScheduleSelectByGroupId;
        log.info(sql + "" + "find all by groupId " + groupId);
        return getFromSqlById(groupId, sql);
    }

    private List<DesiredSchedule> getFromSqlById(int groupId, String sql) {
        List<DesiredSchedule> list;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return list;
    }
}
