package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.FeedbackDao;
import ua.com.nc.domain.Feedback;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
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
        statement.setObject(5, entity.getTime());
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
            Timestamp timestamp = rs.getTimestamp("date_time");
            Feedback feedback = new Feedback(id, studentId, trainerId,
                    courseId, text, timestamp);
            feedbacks.add(feedback);
        }
        log.debug("Retrieved Feedbacks from database " + feedbacks);
        return feedbacks;
    }

    @Override
    public List<Feedback> getAllByUserId(Integer userId) {
        String sql = feedbackSelectAllByUser;
        log.debug("select all feedbacks by userId " + userId + " " + sql);
        return getFromSqlById(sql, userId);
    }


    @Override
    public List<Feedback> getAllByTrainerIdAndUserId(Integer userId, Integer trainerId) {
        String sql = feedbackSelectAllByTrainerIdAndByUserId;
        log.debug("select all feedbacks by trainer " + trainerId + " and by employee " + userId  + " " +sql);
        return getFromSqlByTwoId(sql, userId, trainerId);
    }
}
