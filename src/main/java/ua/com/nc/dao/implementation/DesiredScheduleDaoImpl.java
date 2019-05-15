package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.DesiredScheduleDao;
import ua.com.nc.domain.DesiredSchedule;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class DesiredScheduleDaoImpl extends AbstractDaoImpl<DesiredSchedule> implements DesiredScheduleDao {

    @Value("${desirable.schedule.select-all}")
    private String desirableScheduleSelectAll;
    @Value("${desirable.schedule.insert}")
    private String desirableScheduleInsert;
    @Value("${desirable.schedule.select-by-course-id}")
    private String desirableScheduleSelectByCourseId;
    @Value("${desirable.schedule.select-ungrouped-by-course-id}")
    private String desirableScheduleSelectUngroupedByCourseId;
    @Value("${desirable.schedule.select-by-group-id}")
    private String desirableScheduleSelectByGroupId;
    @Value("${desirable.schedule.select-by-usr-group-id}")
    private String desirableScheduleSelectByUsrGroupId;

    @Autowired
    public DesiredScheduleDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
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
    protected void prepareStatementForInsert(PreparedStatement statement, DesiredSchedule entity) throws SQLException {
        statement.setInt(1, entity.getUserId());
        statement.setInt(2, entity.getCourseId());
        statement.setString(3, entity.getCronInterval());
        statement.setInt(4, entity.getSuitability());

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
        return getFromSqlById(sql, courseId);
    }

    @Override
    public List<DesiredSchedule> getAllForGroup(int groupId) {
        String sql = desirableScheduleSelectByGroupId;
        log.info(sql + "" + "find all by groupId " + groupId);
        return getFromSqlById(sql, groupId);
    }

    @Override
    public List<DesiredSchedule> getUngroupedForCourse(int courseId) {
        String sql = desirableScheduleSelectUngroupedByCourseId;
        log.info(sql + "" + "find all by groupId " + courseId);
        return getFromSqlById(sql, courseId);
    }


    @Override
    public List<DesiredSchedule> getByUsrGroupId(Integer id) {
        String sql = desirableScheduleSelectByUsrGroupId;
        log.info(sql + "" + "find all by groupId " + id);
        return getFromSqlById(sql, id);
    }
}
