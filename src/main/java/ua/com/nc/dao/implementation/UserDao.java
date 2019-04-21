package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao extends GenericAbstractDao<User, Integer> implements IUserDao {

    private final String USER_ID = "ID";

    public UserDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                   @Value("${spring.datasource.username}") String DATABASE_USER,
                   @Value("${spring.datasource.password}") String DATABASE_PASSWORD, SqlQueriesProperties sqlQueriesProperties) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        setSqlQueriesProperties(sqlQueriesProperties);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt(USER_ID);
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        return sqlQueriesProperties.getUsrSelectById();
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getUsrSelectAll();
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getUsrInsert();
    }

    @Override
    protected String getDeleteQuery() {
        return sqlQueriesProperties.getUsrDelete();
    }

    @Override
    protected String getUpdateQuery() {
        return sqlQueriesProperties.getUsrUpdate();
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User entity) throws SQLException {
        setAllFields(statement, entity);
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User entity) throws SQLException {
        setAllFields(statement, entity);
        statement.setInt(7, entity.getId());
    }

    private void setAllFields(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getEmail());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getFirstName());
        statement.setString(4, entity.getLastName());
        statement.setObject(5, entity.getManagerId(), Types.INTEGER);
        statement.setBoolean(6, entity.isActive());
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws SQLException {
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            Integer userId = rs.getInt(USER_ID);
            String EMAIL = "EMAIL";
            String email = rs.getString(EMAIL);
            String PASSWORD_HASH = "PASSWORD";
            String passwordHash = rs.getString(PASSWORD_HASH);
            String FIRSTNAME = "FIRST_NAME";
            String firstname = rs.getString(FIRSTNAME);
            String LASTNAME = "LAST_NAME";
            String lastname = rs.getString(LASTNAME);
            String MANAGER_ID = "MANAGER_ID";
            Integer managerId = rs.getInt(MANAGER_ID);
            String IS_ACTIVE = "IS_ACTIVE";
            boolean isActive = rs.getBoolean(IS_ACTIVE);
            User user = new User(userId, email, passwordHash, firstname, lastname, managerId, isActive);
            list.add(user);
        }
        return list;
    }

    @Override
    public User getByEmail(String email) {
        List<User> list;
        String sql = sqlQueriesProperties.getUsrSelectByEmail();
        log(sql, "find by email");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

}
