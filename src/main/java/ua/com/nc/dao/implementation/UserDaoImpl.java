package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class UserDaoImpl extends AbstractDaoImpl<User> implements UserDao {


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
    @Value("${usr.select-subordinates-by-manager}")
    private String getSelectSubordinatesByManager;
    @Value("${usr.select-students-absent-on-lesson-with-no-reason}")
    private String selectStudentsAbsentOnLessonWithNoReason;
    @Value("${usr.select-admin}")
    private String getAdmin;
    @Value("${lesson.select-lesson-trainer}")
    private String getLessonTrainer;
    @Value("${usr.select-trainer-by-feedback}")
    private String usrSelectTrainerByFeedback;
    @Value("${urs.insert-user-role}")
    private String usrInsertUserRole;
    @Value("${usr.insert-user-by-admin}")
    private String usrInsertUserByAdmin;
    @Value("${usr.select-trainer-by-group-id}")
    private String getSelectTrainerByGroupId;
    @Value("${usr.select-by-token}")
    private String usrSelectByToken;

    @Autowired
    public UserDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
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
    protected void prepareStatementForInsert(PreparedStatement statement, User entity) throws SQLException {
        setAllFields(statement, entity);
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User entity) throws SQLException {
        setAllFields(statement, entity);
        statement.setInt(9, entity.getId());
    }

    private void setAllFields(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1, entity.getEmail());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getFirstName());
        statement.setString(4, entity.getLastName());
        statement.setString(5, entity.getToken());
        statement.setObject(6, entity.getCreated());
        statement.setObject(7, entity.getManagerId(), Types.INTEGER);
        statement.setBoolean(8, entity.isActive());
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws SQLException {
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            Integer userId = rs.getInt("ID");
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
        String sql = usrSelectByEmail;
        log.info(sql + " find by email " + email);
        return getUniqueFromSqlByString(sql, email);
    }

    @Override
    public User getByToken(String token) {
        String sql = usrSelectByToken;
        log.info(sql + " find by token " + token);
        return getUniqueFromSqlByString(sql, token);
    }

    @Override
    public List<User> getTrainersOnCourse(int id) {
        String sql = getSelectTrainerByCourseId;
        log.info(sql + "find by course");
        List<User> trainers = getFromSqlById(sql, id);
        if (trainers == null || trainers.size() == 0) {
            return null;
        }
        return trainers;
    }

    @Override
    public User getTrainerByFeedback(Integer id) {
        List<User> list;
        String sql = usrSelectTrainerByFeedback;
        log.info(sql + " find trainer by feedback " + id);
        list = getFromSqlById(sql, id);
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public void addUserRole(Integer userId, String roleName) {
        String sql = usrInsertUserRole;
        log.info(sql + " insert user " + userId + " role " + roleName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, roleName);
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void addUserByAdmin(User user) {
        String sql = usrInsertUserByAdmin;
        log.info(sql + " insert user " + user.getEmail() + " by admin");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getToken());
            statement.setString(3, "firstname");
            statement.setString(4, "lastname");
            statement.setString(5, "ss");
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<User> getAllTrainers() {
        String sql = usrSelectAllTrainers;
        log.info(sql + "select all trainers");
        return getListFromSql(sql);
    }


    @Override
    public List<User> getAllManagers() {
        String sql = usrSelectAllManagers;
        log.info(sql + "select all managers");
        return getListFromSql(sql);
    }


    @Override
    public List<User> getByGroupId(Integer id) {
        List<User> users;
        String sql = usrSelectByGroupId;
        users = getFromSqlById(sql, id);
        return users;
    }

    @Override
    public List<User> getUngroupedByCourse(Integer courseId) {
        List<User> allUsersForCourse;
        String sql = usrSelectUngroupedByCourseId;
        log.info(sql + " select Ungrouped users for a course " + courseId);
        return getFromSqlById(sql, courseId);
    }

    @Override
    public List<User> getAllForCourse(int courseId) {
        List<User> allUsersForCourse;
        String sql = usrSelectAllByCourse;
        log.info(sql + " select all users for a course " + courseId);
        return getFromSqlById(sql, courseId);
    }

    @Override
    public void updateUserByAdmin(User user) {
        String sql = usrUpdateUsrByAdmin;
        log.info(sql + " update user by admin user= " + user.toString());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setInt(3, user.getManagerId());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void updateActive(User user) {
        String sql = usrUpdateChangeActive;
        log.info(sql + "change active by admin user= " + user.toString());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, user.isActive());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }


    @Override
    public List<User> getLandingPageTrainers() {
        List<User> landingPageTrainers;
        String sql = usrLandingPage;
        log.info(sql + "select trainers on landing page");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
        log.info(sql + " find manager by user id = " + id);
        return getUniqueFromSqlById(sql, id);
    }

    @Override
    public List<User> getAllTrainersById(Integer id) {
        String sql = usrSelectAllTrainersById;
        log.info(sql + " find all trainers by id = " + id);
        List<User> list = getFromSqlById(sql, id);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }

    @Override
    public void updateTrainerLandingPage(int id, boolean isOnLandingPage) {
        String sql = usrUpdateLandingPage;
        log.info(sql + " update trainer on landing page id = " + id + " isOnLandingPage = " + isOnLandingPage);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isOnLandingPage);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public User getTrainerByGroupId(Integer groupId) {
        String sql = getSelectTrainerByCourseId;
        log.info(sql + "trainer by group id = " + groupId);
        return getUniqueFromSqlById(sql, groupId);
    }


    @Override
    public List<User> getSubordinatesOfManager(Integer managerId) {
        String sql = getSelectSubordinatesByManager;
        log.info(" subordinates by manager id = " + managerId + "   " + sql);
        return getFromSqlById(sql, managerId);
    }


    public HashMap<User, User> getStudentsAbsentWitNoReason(int lessonId) {
        List<User> students = new ArrayList<>();
        String sql = selectStudentsAbsentOnLessonWithNoReason;
        log.info(sql + " selectStudentsAbsentOnLessonWithNoReason " + lessonId);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, lessonId);
            ResultSet rs = statement.executeQuery();
            students = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
        }
        HashMap<User, User> absentUsersAndTheirManagers = new HashMap<>();
        for (User student : students) {
            User manager = getManagerById(student.getManagerId());
            absentUsersAndTheirManagers.put(student, manager);
        }
        return absentUsersAndTheirManagers;
    }

    public User getAdmin() {
        List<User> admin = new ArrayList<>();
        String sql = getAdmin;
        log.info(sql + " getAdmin");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            admin = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
        }
        if (admin == null || admin.size() == 0) {
            return null;
        }
        if (admin.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return admin.get(0);
    }

    public User getLessonTrainer(int lessonId) {
        String sql = this.getLessonTrainer;
        log.info("get Trainer of lesson " + lessonId);
        return getUniqueFromSqlById(sql, lessonId);
    }

}
