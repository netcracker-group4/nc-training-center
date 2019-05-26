package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Repository
@PropertySource("classpath:sql_queries.properties")
public class UserDaoImpl extends AbstractDaoImpl<User> implements UserDao {


    @Value("${usr.select-all}")
    private String usrSelectAll;
    @Value("${usr.select-by-id}")
    private String usrSelectById;
    @Value("${usr.select-id-by-name}")
    private String usrSelectIdByName;
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
    @Value("${usr.update-image}")
    private String usrUpdateImage;
    @Value("${usr.update-password}")
    private String usrUpdatePassword;
    @Value("${usr.select-students-by-lesson-id}")
    private String getSelectStudentsByLessonId;
    @Value("${usr.select-by-attendance-id}")
    private String getSelectByAttendanceId;

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
            String token = rs.getString("TOKEN");
            Timestamp created = rs.getTimestamp("CREATED");
            Integer managerId = rs.getInt("MANAGER_ID");
            String imageUrl = rs.getString("IMAGE_URL");
            boolean isActive = rs.getBoolean("IS_ACTIVE");
            User user = new User(userId, email, passwordHash, firstName,
                    lastName, token, created, managerId, imageUrl, isActive);
            list.add(user);
        }
        log.info("Retrieved Users from database " + list);
        return list;
    }

    @Override
    public User getByEmail(String email) {
        String sql = usrSelectByEmail;
        log.info("find user by email " + email + " " + sql);
        return getUniqueFromSqlByString(sql, email);
    }

    @Override
    public User getByToken(String token) {
        String sql = usrSelectByToken;
        log.info("find user by token " + token + " " + sql);
        return getUniqueFromSqlByString(sql, token);
    }

    @Override
    public List<User> getTrainersOnCourse(int courseId) {
        String sql = getSelectTrainerByCourseId;
        log.info("find trainers by courseId " + courseId + " " + sql);
        return getFromSqlById(sql, courseId);
    }

    @Override
    public User getTrainerByFeedback(Integer feedbackId) {
        String sql = usrSelectTrainerByFeedback;
        log.info("find trainer by feedbackId " + feedbackId + " " + sql);
        return getUniqueFromSqlById(sql, feedbackId);
    }

    @Override
    public void addUserRole(Integer userId, String roleName) {
        String sql = usrInsertUserRole;
        log.info(" insert user " + userId + " role " + roleName + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, roleName);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public int getIdByName(String name) {
        String sql = usrSelectIdByName;
        log.info(" select user " + sql + name);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            return rs.getInt("ID");
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<User> getAllTrainers() {
        String sql = usrSelectAllTrainers;
        log.info("select all trainers " + sql);
        return getListFromSql(sql);
    }


    @Override
    public List<User> getAllManagers() {
        String sql = usrSelectAllManagers;
        log.info("select all managers " + sql);
        return getListFromSql(sql);
    }


    @Override
    public List<User> getByGroupId(Integer groupId) {
        String sql = usrSelectByGroupId;
        log.info("select employees by groupId " + sql);
        return getFromSqlById(sql, groupId);
    }

    @Override
    public List<User> getUngroupedByCourse(Integer courseId) {
        String sql = usrSelectUngroupedByCourseId;
        log.info("select ungrouped employees for a course " + courseId + " " + sql);
        return getFromSqlById(sql, courseId);
    }

    @Override
    public void updateActive(User user) {
        String sql = usrUpdateChangeActive;
        log.info("change active by admin user" + user.toString() + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, user.isActive());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }


    @Override
    public List<User> getLandingPageTrainers() {
        String sql = usrLandingPage;
        log.info("select trainers on landing page " + sql);
        return getListFromSql(sql);
    }

    @Override
    public User getManagerByEmployeeId(Integer employeeId) {
        String sql = usrSelectManagerById;
        log.info("find manager by employeeId " + employeeId + " " + sql);
        return getUniqueFromSqlById(sql, employeeId);
    }

    @Override
    public List<User> getEmployeeTrainersByEmployeeId(Integer employeeId) {
        String sql = usrSelectAllTrainersById;
        log.info("find all trainers by employeeId " + employeeId + " " + sql);
        return getFromSqlById(sql, employeeId);
    }

    @Override
    public void updateTrainerLandingPage(int id, boolean isOnLandingPage) {
        String sql = usrUpdateLandingPage;
        log.info(" update trainer on landing page id " + id + " isOnLandingPage " + isOnLandingPage + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isOnLandingPage);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public User getTrainerByCourseId(Integer courseId) {
        String sql = getSelectTrainerByCourseId;
        log.info("find trainer by courseId " + courseId + " " + sql);
        return getUniqueFromSqlById(sql, courseId);
    }


    @Override
    public List<User> getSubordinatesOfManager(Integer managerId) {
        String sql = getSelectSubordinatesByManager;
        log.info("find subordinates by managerId = " + managerId + "  " + sql);
        return getFromSqlById(sql, managerId);
    }

    @Override
    public List<User> getStudentsByLessonId(Integer lessonId) {
        String sql = getSelectStudentsByLessonId;
        log.info("find students by lessonId = " + lessonId + "  " + sql);
        return getFromSqlById(sql, lessonId);
    }

    @Override
    public void updateImage(User user) {
        String sql = usrUpdateImage;
        log.info("upload image" + user.toString() + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getImageUrl());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void updatePassword(User user) {
        String sql = usrUpdatePassword;
        log.info("update password" + user.toString() + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public User getByAttendanceId(Integer attendanceId) {
        String sql = getSelectByAttendanceId;
        log.info(sql + "select user by attendance id " + attendanceId);
        return getUniqueFromSqlById(sql, attendanceId);
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
            log.error(e);
        }
        HashMap<User, User> absentUsersAndTheirManagers = new HashMap<>();
        for (User student : students) {
            User manager = getManagerByEmployeeId(student.getManagerId());
            absentUsersAndTheirManagers.put(student, manager);
        }
        return absentUsersAndTheirManagers;
    }

    public User getAdmin() {
        String sql = getAdmin;
        log.info("find admin " + sql);
        List<User> admin = getListFromSql(sql);
        if (admin == null || admin.size() == 0) {
            return null;
        }
        if (admin.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return admin.get(0);
    }

    public User getLessonTrainer(int lessonId) {
        String sql = getLessonTrainer;
        log.info("find trainer of lesson by lessonId" + lessonId + " " + sql);
        return getUniqueFromSqlById(sql, lessonId);
    }

}
