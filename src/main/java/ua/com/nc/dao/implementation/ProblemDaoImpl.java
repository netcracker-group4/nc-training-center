package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ProblemDao;
import ua.com.nc.domain.Problem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class ProblemDaoImpl extends AbstractDaoImpl<Problem> implements ProblemDao {

    @Value("${problem.create-request}")
    private String createRequest;

    @Value("${problem.select-requests-of-type}")
    private String selectRequestsOfType;

    @Value("${problem.update-type-of-request}")
    private String updateTypeOfRequest;

    @Autowired
    public ProblemDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected List<Problem> parseResultSet(ResultSet rs) throws SQLException {
        List<Problem> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer studentId = rs.getInt("USER_ID");
            String description = rs.getString("TITLE");
            String status = rs.getString("PROBLEM_STATUS_ID");
            String message = rs.getString("DESCRIPTION");
            Problem problem = new Problem(id, studentId, description, status, message);
            list.add(problem);
        }
        log.info("Retrieved Problems from database " + list);
        return list;
    }

    @Override
    public int createRequest(int studentId, String description, String message) {
        String sql = createRequest;
        log.info(sql + " create a request " + description);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setString(2, description);
            statement.setString(3, message);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } throw new PersistException("No result set");
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<Problem> getAllRequestsOfType(String requestType) {
        List<Problem> requests;
        String sql = selectRequestsOfType;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, requestType);
            ResultSet rs = statement.executeQuery();
            requests = parseResultSet(rs);
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e.getMessage());
        }
        log.info(sql + " get all requests of type " + requestType);
        return requests;
    }

    @Override
    public void updateRequestType(int requestId, String requestType) {
        String sql = updateTypeOfRequest;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, requestType);
            statement.setInt(2, requestId);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e.getMessage());
        }
    }
}
