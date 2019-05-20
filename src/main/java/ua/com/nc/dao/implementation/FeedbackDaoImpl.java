package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.FeedbackDao;
import ua.com.nc.domain.Feedback;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class FeedbackDaoImpl extends AbstractDaoImpl<Feedback> implements FeedbackDao {

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
    @Value("${feedback.select-all-by-trainer-id-and-user-id}")
    private String feedbackSelectAllByTrainerIdAndByUserId;

    @Autowired
    public FeedbackDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
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
    protected void prepareStatementForInsert(PreparedStatement statement, Feedback entity) throws SQLException {
        setAllFields(statement, entity);
    }

    private void setAllFields(PreparedStatement statement, Feedback entity) throws SQLException {
        statement.setInt(1, entity.getStudentId());
        statement.setInt(2, entity.getTrainerId());
        statement.setInt(3, entity.getCourseId());
        statement.setString(4, entity.getText());
        statement.setObject(5, entity.getTimeDate());
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
//            Date timeDate = rs.getDate("date_time");
            OffsetDateTime offsetDateTime = rs.getObject("date_time", OffsetDateTime.class);
            Feedback feedback = new Feedback(id, studentId, trainerId, courseId, text, offsetDateTime);
            feedbacks.add(feedback);
        }
        return feedbacks;
    }

    @Override
    public List<Feedback> getAllByUserId(Integer userId) {
        List<Feedback> feedbacks;
        String sql = feedbackSelectAllByUser;
        log.info(sql + " select all feedback by user " + userId);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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

    @Override
    public List<Feedback> getAllByTrainerIdAndUserId(Integer userId, Integer trainerId) {
        List<Feedback> feedbacks;
        String sql = feedbackSelectAllByTrainerIdAndByUserId;
        log.info(sql + " select all feedback by trainer " + trainerId + " and by user " + userId);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, trainerId);
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
