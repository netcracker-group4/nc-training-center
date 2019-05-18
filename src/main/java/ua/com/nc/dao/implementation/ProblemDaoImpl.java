package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ProblemDao;
import ua.com.nc.domain.Problem;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class ProblemDaoImpl extends AbstractDaoImpl<Problem> implements ProblemDao {

    @Value("${problem.create-request}")
    private String createRequest;

    @Autowired
    public ProblemDaoImpl (DataSource dataSource) throws PersistException {
        super (dataSource);
    }
    @Override
    protected List <Problem> parseResultSet(ResultSet rs) throws SQLException {
        List<Problem> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer studentId = rs.getInt("USER_ID");
            String description = rs.getString("TITLE");
            String status = rs.getString("PROBLEM_STATUS_ID");
            String message = rs.getString("DESCRIPTION");
            Problem problem = new Problem (id, studentId, description, status, message);
            list.add(problem);
        }
        return list;
    }

    @Override
    public void createRequest (int studentId, String description, String message) {
        String sql = createRequest;
        log.info (sql + " create a request " + description);
        try (PreparedStatement statement = connection.prepareStatement (sql)) {
            statement.setInt (1, studentId);
            statement.setString(2, description);
            statement.setString (3, message);
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace (e);
            throw new PersistException (e);
        }
    }


}
