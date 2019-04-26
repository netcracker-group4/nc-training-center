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
            String email = rs.getString("EMAIL");
            String passwordHash = rs.getString("PASSWORD");
            String firstName = rs.getString("FIRST_NAME");
            String lastName = rs.getString("LAST_NAME");
            Integer managerId = rs.getInt("MANAGER_ID");
            String imageUrl = rs.getString("IMAGE_URL");
            boolean isActive = rs.getBoolean("IS_ACTIVE");
            User user = new User(userId, email, passwordHash, firstName,
                    lastName, managerId, imageUrl, isActive);
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

    @Override
    public List<User> getAllTrainers() {
        List<User> list;
        String sql = sqlQueriesProperties.getUsrSelectAllTrainers();
        log(sql, "select all trainers");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }

    @Override
    public List<User> getByGroupId(Integer id) {
        List<User> users;
        String sql = sqlQueriesProperties.getUsrSelectByGroupId();
        users = getFromQueryById(id, sql);
        return users;
    }

    private List<User> getFromQueryById(Integer id, String sql) {
        List<User> users;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            users = parseResultSet(rs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
        return users;
    }

    @Override
    public List<User> getUngroupedByCourse(Integer courseId) {
        List<User> allUsersForCourse;
        String sql = sqlQueriesProperties.getUsrSelectUngroupedByCourseId();
        log(sql, "select Ungrouped users for a course");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setInt(2, courseId);
            ResultSet rs = statement.executeQuery();
            allUsersForCourse = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (allUsersForCourse.size() == 0) {
            return null;
        }
        return allUsersForCourse;
    }

    @Override
    public List<User> getAllForCourse(int courseId) {
        List<User> allUsersForCourse;
        String sql = "select distinct usr.* from usr join desirable_schedule ug on usr.id = ug.user_id  where course_id = ?";
        log(sql, "select all users for a course");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            ResultSet rs = statement.executeQuery();
            allUsersForCourse = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (allUsersForCourse.size() == 0) {
            return null;
        }
        return allUsersForCourse;
    }


    @Override
    public List<User> getLandingPageTrainers() {
        List<User> landingPageTrainers;
        String sql = sqlQueriesProperties.getUsrLandingPage();
        log(sql, "select trainers on landing page");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            landingPageTrainers = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (landingPageTrainers.size() == 0) {
            return null;
        }
        return landingPageTrainers;
    }

    @Override
    public User getManagerById(Integer id) {
        String sql = sqlQueriesProperties.getUsrSelectManagerById();
        log(sql, "find manager by user id");
        List<User> list = getFromQueryById(id, sql);
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public List<User> getAllTrainersById(Integer id) {
        String sql = sqlQueriesProperties.getUsrSelectAllTrainersById();
        log(sql, "find all trainers by id");
        List<User> list = getFromQueryById(id, sql);
        if (list == null || list.size() == 0) {
            return null;
        }

        return list;
    }

    @Override
    public void updateTrainerLandingPage(int id, boolean isOnLandingPage) {
        String sql = sqlQueriesProperties.getUsrUpdateLandingPage();
        log(sql, "update trainer on landing page");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isOnLandingPage);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
    }


}
