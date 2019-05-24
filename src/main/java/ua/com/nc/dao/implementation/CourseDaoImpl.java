package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.domain.Course;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
@PropertySource("classpath:sql_queries.properties")
public class CourseDaoImpl extends AbstractDaoImpl<Course> implements CourseDao {

    @Value("${course.select-all}")
    private String courseSelectAll;
    @Value("${course.select-by-id}")
    private String courseSelectById;
    @Value("${course.select-by-level}")
    private String courseSelectByLevel;
    @Value("${course.select-by-trainer}")
    private String courseSelectByTrainer;
    @Value("${course.update}")
    private String courseUpdate;
    @Value("${course.delete}")
    private String courseDelete;
    @Value("${course.insert}")
    private String courseInsert;
    @Value("${course.select-on-landing-page}")
    private String courseLandingPage;
    @Value("${course.update-landing-page}")
    private String courseUpdateLandingPage;
    @Value("${course.select-course-by-group}")
    private String selectCourseByGroupId;
    @Value("${course.select-course-by-feedback}")
    private String selectCourseByFeedbackId;
    @Value("${course.select-course-by-trainer-and-by-employee}")
    private String courseSelectAllByTrainerAndEmployee;
    @Value("${course.edit}")
    private String editCourse;


    @Autowired
    public CourseDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected String getSelectByIdQuery() {
        return courseSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return courseSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return courseInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return courseDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return courseUpdate;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Course entity) throws SQLException {
        setAllFields(statement, entity);
    }

    private void setAllFields(PreparedStatement statement, Course entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setInt(2, entity.getLevel());
        statement.setInt(3, entity.getCourseStatusId());
        statement.setInt(4, entity.getUserId());
        statement.setString(5, entity.getImageUrl());
        statement.setDate(6, entity.getStartDate());
        statement.setDate(7, entity.getEndDate());
        statement.setBoolean(8, entity.getIsOnLandingPage());
        statement.setString(9, entity.getDescription());
    }


    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Course entity) throws SQLException {
        setAllFields(statement, entity);
        statement.setInt(10, entity.getId());
    }

    @Override
    protected List<Course> parseResultSet(ResultSet rs) throws SQLException {
        List<Course> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("ID");
            String name = rs.getString("NAME");
            int level = rs.getInt("LEVEL");
            int courseStatusId = rs.getInt("course_status_id");
            int userId = rs.getInt("USER_ID");
            String imageUrl = rs.getString("IMAGE_URL");
            Date startDate = rs.getDate("start_date");
            Date endDate = rs.getDate("end_date");
            Boolean isOnLandingPage = rs.getBoolean("is_on_landing_page");
            String description = rs.getString("description");
            Course newCourse = new Course(id, name, level, courseStatusId, userId, imageUrl, startDate, endDate, isOnLandingPage, description);
            list.add(newCourse);
        }
        log.info("Retrieved Courses from database " + list);
        return list;
    }

    @Override
    public List<Course> getAllByLevel(int levelId) {
        String sql = courseSelectByLevel;
        log.info("find all courses by level " + levelId + " " + sql);
        return getFromSqlById(sql, levelId);
    }

    @Override
    public List<Course> getAllByTrainer(int trainerId) {
        String sql = courseSelectByTrainer;
        log.info(sql + " find all courses by trainer " + trainerId);
        return getFromSqlById(sql, trainerId);
    }

    @Override
    public List<Course> getLandingPageCourses() {
        String sql = courseLandingPage;
        log.info("find all courses on landing page " + sql);
        return getListFromSql(sql);
    }

    @Override
    public void updateCourseLandingPage(int id, boolean isOnLandingPage) {
        String sql = courseUpdateLandingPage;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isOnLandingPage);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e.getMessage());
        }
    }

    @Override
    public Course getCourseByGroup(int groupId) {
        String sql = selectCourseByGroupId;
        log.info("find all courses by groupId " + sql);
        return getUniqueFromSqlById(sql, groupId);
    }

    @Override
    public Course getCourseByFeedback(Integer feedbackId) {
        String sql = selectCourseByFeedbackId;
        log.info("find a course by feedbackId " + feedbackId + " " + sql);
        return getUniqueFromSqlById(sql, feedbackId);
    }

    @Override
    public List<Course> getAllCoursesByTrainerAndByEmployee(Integer trainerId, Integer employeeId) {
        String sql = courseSelectAllByTrainerAndEmployee;
        log.info("find all courses by trainerId " + trainerId + " and employeeId " + employeeId + " " + sql);
        return getFromSqlByTwoId(sql, trainerId, employeeId);
    }

    @Override
    public void edit(int id, String name, int lvl, int statusId, boolean isLanding, java.sql.Date starts, java.sql.Date ends, String desc) {
        Course course = getEntityById(id);
        String imageUrl = course.getImageUrl();
        Course c = new Course(id,name,lvl,statusId,course.getUserId(),imageUrl,starts,ends,isLanding,desc);
        update(c);
    }
}