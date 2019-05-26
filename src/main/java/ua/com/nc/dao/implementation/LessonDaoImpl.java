package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.LessonDao;
import ua.com.nc.domain.Lesson;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
@PropertySource("classpath:sql_queries.properties")
public class LessonDaoImpl extends AbstractDaoImpl<Lesson> implements LessonDao {

    @Value("${lesson.select-all}")
    private String lessonSelectAll;
    @Value("${lesson.select-by-id}")
    private String lessonSelectById;
    @Value("${lesson.insert}")
    private String lessonInsert;
    @Value("${lesson.update}")
    private String lessonUpdate;
    @Value("${lesson.delete}")
    private String lessonDelete;
    @Value("${lesson.select-by-group-id}")
    private String selectByGroupId;
    @Value("${lesson.select-by-group-id-and-user-id}")
    private String getSelectByGroupIdAndUserId;
    @Value("${lesson.select-by-employee-id}")
    private String selectByEmployeeId;
    @Value("${lesson.select-by-trainer-id}")
    private String selectByTrainerId;

    @Autowired
    public LessonDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected String getSelectByIdQuery() {
        return lessonSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return lessonSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return lessonInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return lessonDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return lessonUpdate;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Lesson entity) throws SQLException {
        setAllFields(statement, entity);
    }

    private void setAllFields(PreparedStatement statement, Lesson entity) throws SQLException {
        statement.setInt(1, entity.getGroupId());
        statement.setString(2, entity.getTopic());
        statement.setInt(3, entity.getTrainerId());
        statement.setTimestamp(4, entity.getTime());
        statement.setString(5, entity.getIntervalString());
        statement.setBoolean(6, entity.isCanceled());
        statement.setBoolean(7, entity.isArchived());
        statement.setBoolean(8, entity.isPerformed());
    }


    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lesson entity) throws SQLException {
        setAllFields(statement, entity);
        statement.setInt(9, entity.getId());
    }

    @Override
    protected List<Lesson> parseResultSet(ResultSet rs) throws SQLException {
        List<Lesson> lessons = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer groupId = rs.getInt("group_id");
            String topic = rs.getString("topic");
            Integer trainerId = rs.getInt("trainer_id");
            Date timeDate = rs.getDate("time_date");
            Timestamp time = rs.getTimestamp("time_date");
            boolean isCanceled = rs.getBoolean("is_canceled");
            boolean isPerformed = rs.getBoolean("performed");
            boolean isArchived = rs.getBoolean("is_archived");
            String duration = rs.getString("duration");
            Lesson lesson = new Lesson(id, groupId, topic, trainerId,
                    timeDate, time, duration, isCanceled,
                    isPerformed, isArchived);
            lessons.add(lesson);
        }
        log.debug("Retrieved lessons from database " + lessons);
        return lessons;
    }


    @Override
    public List<Lesson> getByGroupIdAndUserId(Integer groupId, Integer userId) {
        String sql = getSelectByGroupIdAndUserId;
        log.debug("getByGroupIdAndUserId groupId " + groupId + " userId " + userId + "   " + sql);
        List<Lesson> lessons;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            lessons = parseResultSetForAttendance(rs);
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        return lessons;
    }

    @Override
    public List<Lesson> getByGroupId(Integer groupId) {
        String sql = selectByGroupId;
        log.debug("getByGroupId groupId " + groupId + "  " + sql);
        return getFromSqlById(sql, groupId);
    }

    @Override
    public List<Lesson> getByEmployee(Integer employeeId) {
        String sql = selectByEmployeeId;
        log.debug("getByUserId employeeId " + employeeId + "  " + sql);
        return getFromSqlById(sql, employeeId);
    }

    private List<Lesson> parseResultSetForAttendance(ResultSet rs) throws SQLException {
        List<Lesson> lessons = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer groupId = rs.getInt("group_id");
            String topic = rs.getString("topic");
            Integer trainerId = rs.getInt("trainer_id");
            Date timeDate = rs.getDate("time_date");
            String absenceReason = rs.getString("absence_reason");
            String absenceStatus = rs.getString("absence_status");
            Lesson lesson = new Lesson(id, groupId, topic, trainerId,
                    timeDate, absenceReason, absenceStatus);
            lessons.add(lesson);
        }
        return lessons;
    }

    @Override
    public List<Lesson> getByTrainer(Integer userId) {
        String sql = selectByTrainerId;
        log.debug("getByUserId  " + userId + "  " + sql);
        return getFromSqlById(sql, userId);
    }
}


