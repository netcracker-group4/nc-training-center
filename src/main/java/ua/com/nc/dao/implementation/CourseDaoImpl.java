package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.domain.Course;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
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
        return list;
    }

    @Override
    public List<Course> getAllByLevel(int levelId) {
        String sql = courseSelectByLevel;
        log.info(sql + "find all by level " + levelId);
        return getFromSqlById(sql, levelId);
    }

    @Override
    public List<Course> getAllByTrainer(int trainerId) {
        String sql = courseSelectByTrainer;
        log.info(sql + " find all by trainer " + trainerId);
        return getFromSqlById(sql, trainerId);
    }

    @Override
    public List<Course> getLandingPageCourses() {
        List<Course> landingPageCourses;
        String sql = courseLandingPage;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            landingPageCourses = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e.getMessage());
        }
        log.info(sql + " find all on landing page");
        return landingPageCourses;
    }

    @Override
    public void updateCourseLandingPage(int id, boolean isOnLandingPage) {
        String sql = courseUpdateLandingPage;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isOnLandingPage);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e.getMessage());
        }
    }

    @Override
    public Course getCourseByGroup(int id) {
        String sql = selectCourseByGroupId;
        Course course;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            course = parseResultSet(rs).get(0);
        } catch (SQLException e) {
            log.trace(e);
            throw new PersistException(e.getMessage());
        }
        return course;
    }

    @Override
    public Course getCourseByFeedback(Integer feedbackId) {
        List<Course> courses;
        String sql = selectCourseByFeedbackId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, feedbackId);
            ResultSet rs = statement.executeQuery();
            courses = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistException(e.getMessage());
        }
        if (courses == null || courses.size() == 0) {
            return null;
        }
        if (courses.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return courses.get(0);
    }

    @Override
    public List<Course> getAllCourseByTrainerAndByEmployee(Integer trainerId, Integer employeeId) {
        List<Course> courses;
        String sql = courseSelectAllByTrainerAndEmployee;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerId);
            statement.setInt(2, employeeId);
            ResultSet rs = statement.executeQuery();
            courses = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistException(e.getMessage());
        }
        if (courses == null || courses.size() == 0) {
            return null;
        }
        return courses;
    }


}