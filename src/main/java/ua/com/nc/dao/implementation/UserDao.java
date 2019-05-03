package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:sql_queries.properties")
public class UserDao extends GenericAbstractDao<User, Integer> implements IUserDao {

    private final String USER_ID = "ID";

    @Value("${usr.select-all}")
    private String usrSelectAll;
    @Value("${usr.select-by-id}")
    private String usrSelectById;
    @Value("${usr.select-by-email}")
    private String usrSelectByEmail;
    @Value("${usr.update}")
    private String usrUpdate;
    @Value("${usr.delete}")
    private String usrDelete;
    @Value("${usr.insert}")
    private String usrInsert;
    @Value("${usr.select-all-trainers}")
    private String usrSelectAllTrainers;
    @Value("${usr.select-all-managers}")
    private String usrSelectAllManagers;
    @Value("${usr.select-by-group-id}")
    private String usrSelectByGroupId;
    @Value("${usr.select-on-landing-page}")
    private String usrLandingPage;
    @Value("${usr.select-all-trainers-by-id}")
    private String usrSelectAllTrainersById;
    @Value("${usr.select-manager-by-id}")
    private String usrSelectManagerById;
    @Value("${usr.update-landing-page}")
    private String usrUpdateLandingPage;
    @Value("${usr.update-usr-by-admin}")
    private String usrUpdateUsrByAdmin;
    @Value("${usr.update-change-active}")
    private String usrUpdateChangeActive;
    @Value("${usr.select-ungrouped-by-course-id}")
    private String usrSelectUngroupedByCourseId;
    @Value("${usr.select-all-by-course}")
    private String usrSelectAllByCourse;
    @Value("${course.select-trainer}")
    private String getSelectTrainerByCourseId;

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
        return usrSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return usrSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return usrInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return usrDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return usrUpdate;
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
        String sql = usrSelectByEmail;
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
    public List<User> getTrainersOnCourse(int id){
        List<User> list;
        String sql = getSelectTrainerByCourseId;
        log(sql, "find by course");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }

    @Override
    public List<User> getAllTrainers() {
        List<User> list;
        String sql = usrSelectAllTrainers;
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
    public List<User> getAllManagers() {
        List<User> list;
        String sql = usrSelectAllManagers;
        log(sql, "select all managers");
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
        String sql = usrSelectByGroupId;
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
        String sql = usrSelectUngroupedByCourseId;
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
        String sql = usrSelectAllByCourse;
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
    public void updateUserByAdmin(User user) {
        String sql = usrUpdateUsrByAdmin;
        log (sql, "update user by admin");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException (e);
        }
    }

    @Override
    public void updateActive(User user) {
        String sql = usrUpdateChangeActive;
        log (sql, "change active by admin");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, user.isActive());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException (e);
        }
    }


    @Override
    public List<User> getLandingPageTrainers() {
        List<User> landingPageTrainers;
        String sql = usrLandingPage;
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
        String sql = usrSelectManagerById;
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
        String sql = usrSelectAllTrainersById;
        log(sql, "find all trainers by id");
        List<User> list = getFromQueryById(id, sql);
        if (list == null || list.size() == 0) {
            return null;
        }

        return list;
    }

    @Override
    public void updateTrainerLandingPage(int id, boolean isOnLandingPage) {
        String sql = usrUpdateLandingPage;
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
