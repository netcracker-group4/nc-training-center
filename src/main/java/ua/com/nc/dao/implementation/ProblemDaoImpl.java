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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class ProblemDaoImpl extends AbstractDaoImpl<Problem> implements ProblemDao {

    @Value("${problem.select-all-by-user}")
    private String selectAllByUser;

    @Value("${problem.select-all-requests}")
    private String selectAllQuery;

    @Value ("${problem.insert}")
    private String insertQuery;

    @Value ("${problem.update-request")
    private String updateQuery;

    @Autowired
    public ProblemDaoImpl (DataSource dataSource) throws PersistException {
        super (dataSource);
    }

    public String getSelectQuery () { return selectAllQuery; }

    public String getInsertQuery () {return insertQuery; }

    public String getUpdateQuery () { return updateQuery; }

    private void setAllFields (PreparedStatement statement, Problem entity) throws SQLException {
        statement.setInt(1, entity.getStudentId());
        statement.setString(2, entity.getDescription());
        statement.setInt (3, entity.getStatus());
        statement.setString(4, entity.getMessage());
    }

    protected void prepareStatementForInsert (PreparedStatement statement, Problem entity) throws SQLException {
        setAllFields (statement, entity);
    }

    protected void prepareStatementForUpdate (PreparedStatement statement, Problem entity) throws SQLException {
        setAllFields (statement, entity);
        statement.setInt(5, entity.getId());
    }


    @Override
    protected List <Problem> parseResultSet(ResultSet rs) throws SQLException {
        List<Problem> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer studentId = rs.getInt("USER_ID");
            String description = rs.getString("TITLE");
            Integer status = rs.getInt("PROBLEM_STATUS_ID");
            String message = rs.getString("DESCRIPTION");
            Integer chatId = rs.getInt("CHAT_ID");
            Problem problem = new Problem (studentId, description, message, status, chatId);
            list.add(problem);
        }
        log.info("Retrieved Problems from database " + list);
        return list;
    }

    @Override
    public List<Problem> getRequestsByUserId (int userId) {
        String sql = selectAllByUser;
        return getFromSqlById(sql, userId);
    }
}
