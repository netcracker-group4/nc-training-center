package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.LessonDao;
import ua.com.nc.domain.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
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

    @Value("${lesson.archive}")
    private String archiveLessonQuery;

    @Value("${lesson.select-by-user-id}")
    private String selectByUserId;

    public LessonDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
                         @Value("${spring.datasource.username}") String DATABASE_USER,
                         @Value("${spring.datasource.password}") String DATABASE_PASSWORD)
            throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("id");
        } else throw new PersistException("No value returned!");
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
        String[] intervalElems = entity.getDuration().split(":");
        statement.setString(5, intervalElems[0] + "h " + intervalElems[1] + "m");
        statement.setBoolean(6, entity.isCanceled());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lesson entity) throws SQLException {
        setAllFields(statement, entity);
        statement.setInt(7, entity.getId());
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
            String duration = rs.getString("duration");
            Lesson lesson = new Lesson(id, groupId, topic, trainerId, timeDate, time, duration, isCanceled);
            lessons.add(lesson);
        }
        return lessons;
    }


    @Override
    public List<Lesson> getByGroupIdAndUserId(Integer groupId, Integer userId) {
        List<Lesson> lessons;
        String sql = getSelectByGroupIdAndUserId;
        log.info("getByGroupIdAndUserId groupId " + groupId + " userId " + userId + "   " + sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            lessons = parseResultSetForAttendance(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return lessons;
    }

    @Override
    public List<Lesson> getByGroupId(Integer groupId) {
        String sql = selectByGroupId;
        log.info("getByGroupId groupId " + groupId + "  " + sql);
        return getFromSqlById(sql, groupId);
    }

    @Override
    public void archiveLesson(Integer lessonId) {
        String sql = archiveLessonQuery;
        log.info(sql + "  archiveLesson " + lessonId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, lessonId);
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
    public List<Lesson> getByUser(int userId) {
        String sql = selectByUserId;
        log.info("getByUserId userId " + userId + "  " + sql);
        return getFromSqlById(sql, userId);
    }

    protected List<Lesson> parseResultSetForAttendance(ResultSet rs) throws SQLException {
        List<Lesson> lessons = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer groupId = rs.getInt("group_id");
            String topic = rs.getString("topic");
            Integer trainerId = rs.getInt("trainer_id");
            Date timeDate = rs.getDate("time_date");
            String absenceReason = rs.getString("absence_reason");
            String absenceStatus = rs.getString("absence_status");
            Lesson lesson = new Lesson(id, groupId, topic, trainerId, timeDate, absenceReason, absenceStatus);
            lessons.add(lesson);
        }
        return lessons;
    }

}


