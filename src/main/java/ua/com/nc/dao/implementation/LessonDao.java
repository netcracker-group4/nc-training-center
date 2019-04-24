package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ILessonDao;
import ua.com.nc.domain.Lesson;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class LessonDao extends GenericAbstractDao<Lesson, Integer> implements ILessonDao {

    @Value("${lesson.select-by-group-id}")
    private String selectByGroupId;

    @Value(("${lesson.select-by-group-id-and-user-id}"))
    private String getSelectByGroupIdAndUserId;

    public LessonDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                   @Value("${spring.datasource.username}") String DATABASE_USER,
                   @Value("${spring.datasource.password}") String DATABASE_PASSWORD, SqlQueriesProperties sqlQueriesProperties) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        setSqlQueriesProperties(sqlQueriesProperties);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("id");
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return null;
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
    protected void prepareStatementForInsert(PreparedStatement statement, Lesson entity) throws SQLException {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Lesson entity) throws SQLException {

    }

    @Override
    protected List<Lesson> parseResultSet(ResultSet rs) throws SQLException {
        List<Lesson> lessons = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer groupId = rs.getInt("group_id");
            String topic = rs.getString("topic");
            Integer trainerId = rs.getInt("trainer_id");
            OffsetDateTime timeDate = rs.getObject("time_date", OffsetDateTime.class);
            String absenceReason = rs.getString("absence_reason");
            String absenceStatus = rs.getString("absence_status");
            Lesson lesson = new Lesson(id, groupId, topic, trainerId, timeDate, absenceReason, absenceStatus);
            lessons.add(lesson);
        }
        return lessons;
    }


    @Override
    public List<Lesson> getByGroupIdAndUserId(Integer groupId, Integer userId) {
        List<Lesson> lessons;
        String sql = getSelectByGroupIdAndUserId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            lessons = parseResultSet(rs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
        return lessons;
    }

}
