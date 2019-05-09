package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IFeedbackDao;
import ua.com.nc.domain.Feedback;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class FeedbackDao extends GenericAbstractDao<Feedback, Integer> implements IFeedbackDao {

    @Value("${feedback.select-all}")
    private String feedbackSelectAll;
    @Value("${feedback.select-by-id}")
    private String feedbackSelectById;
    @Value("${feedback.insert}")
    private String feedbackInsert;
    @Value("${feedback.update}")
    private String feedbackUpdate;
    @Value("${feedback.delete}")
    private String feedbackDelete;
    @Value("${feedback.select-all-by-user-id}")
    private String feedbackSelectAllByUser;

    public FeedbackDao(@Value("${spring.datasource.url}") String DATABASE_URL,
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
        return feedbackSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return feedbackSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return feedbackInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return feedbackDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return feedbackUpdate;
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Feedback entity) throws SQLException {
        setAllFields(statement, entity);
    }

    private void setAllFields(PreparedStatement statement, Feedback entity) throws SQLException {
        statement.setInt(1, entity.getStudentId());
        statement.setInt(2, entity.getTrainerId());
        statement.setInt(3, entity.getCourseId());
        statement.setString(4, entity.getText());
        statement.setDate(5, entity.getTimeDate());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Feedback entity) throws SQLException {
        setAllFields(statement, entity);
        statement.setInt(6, entity.getId());
    }

    @Override
    protected List<Feedback> parseResultSet(ResultSet rs) throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer studentId = rs.getInt("user_id");
            Integer trainerId = rs.getInt("trainer_id");
            Integer courseId = rs.getInt("course_id");
            String text = rs.getString("text");
            Date timeDate = rs.getDate("date_time");
            Feedback lesson = new Feedback(id, studentId, trainerId, courseId, text, timeDate);
            feedbacks.add(lesson);
        }
        return feedbacks;
    }

    @Override
    public List<Feedback> getAllByUserId(Integer userId) {
        List<Feedback> feedbacks;
        String sql = feedbackSelectAllByUser;
        log.info(sql + " select all feedback by user " + userId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            feedbacks = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (feedbacks.size() == 0) {
            return null;
        }
        return feedbacks;
    }
}
