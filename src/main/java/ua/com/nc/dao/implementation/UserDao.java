package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao extends GenericAbstractDao<User, Integer> implements IUserDao {

    private final String EMAIL = "EMAIL";
    private final String PASSWORD_HASH = "PASSWORD_HASH";
    private final String FIRSTNAME = "FIRSTNAME";
    private final String LASTNAME = "LASTNAME";
    private final String MANAGER_ID = "MANAGER_ID";
    private final String TABLE_NAME = "USR";
    private final String IS_ACTIVE = "IS_ACTIVE";
    private final String USER_ID = "USER_ID";

    public UserDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                   @Value("${spring.datasource.username}") String DATABASE_USER,
                   @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt(USER_ID);
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT " + USER_ID + ", " + EMAIL + ", " + PASSWORD_HASH + ", " + FIRSTNAME + ", " + LASTNAME + ", " +
                MANAGER_ID + ", " + IS_ACTIVE + " FROM " + TABLE_NAME + " WHERE " + USER_ID + " = ?";
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT " + USER_ID + ", " + EMAIL + ", " + PASSWORD_HASH + ", " + FIRSTNAME + ", " +
                LASTNAME + ", " + MANAGER_ID + ", " + IS_ACTIVE + " FROM " + TABLE_NAME;
    }

    @Override
    protected String getInsertQuery() {
        return " INSERT INTO " + TABLE_NAME + "(" +
                EMAIL + ", " +
                PASSWORD_HASH + ", " +
                FIRSTNAME + ", " +
                LASTNAME + ", " +
                MANAGER_ID + ", " +
                IS_ACTIVE + ")\n" +
                "VALUES\n" +
                "        ( ?, ?, ?, ?, ?, ?) RETURNING " + USER_ID ;
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM " + TABLE_NAME + " \n" +
                "WHERE " + USER_ID + " = ? ";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + TABLE_NAME + " \n" +
                "SET \n" +
                EMAIL + " = ?,\n" +
                PASSWORD_HASH + " = ?,\n" +
                FIRSTNAME + " = ?,\n" +
                LASTNAME + " = ?,\n" +
                MANAGER_ID + " = ?,\n" +
                IS_ACTIVE + " = ?\n" +
                "WHERE " + USER_ID + " = ? ";
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
        statement.setString(2, entity.getPasswordHash());
        statement.setString(3, entity.getFirstname());
        statement.setString(4, entity.getLastname());
        statement.setInt(5, entity.getManagerId());
        statement.setBoolean(6, entity.isActive());
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws SQLException {
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            Integer userId = rs.getInt(USER_ID);
            String email = rs.getString(EMAIL);
            String passwordHash = rs.getString(PASSWORD_HASH);
            String firstname = rs.getString(FIRSTNAME);
            String lastname = rs.getString(LASTNAME);
            Integer managerId = rs.getInt(MANAGER_ID);
            boolean isActive = rs.getBoolean(IS_ACTIVE);
            User user = new User(userId, email, passwordHash, firstname, lastname, managerId, isActive);
            list.add(user);
        }
        return list;
    }
}
