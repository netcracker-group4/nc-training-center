package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.domain.Course;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class CourseDao extends GenericAbstractDao<Course, Integer> implements ICourseDao {

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


    public CourseDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                     @Value("${spring.datasource.username}") String DATABASE_USER,
                     @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("ID");
        } else throw new PersistException("No value returned!");
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
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
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
    public Course getCourseById (int id) {

        String sql = getSelectByIdQuery ();
        List <Course> list = new ArrayList ();
        log.info (sql + " get course by id " + id);
        try (PreparedStatement statement = connection.prepareStatement (sql)) {
            statement.setInt (1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet (rs);
        } catch (Exception e) {
            log.trace(e);
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record with id " + id);
        }
        if (list.size () == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Course> getAllByLevel(int levelId) {
        String sql = courseSelectByLevel;
        log.info(sql + "find all by level " + levelId);
        return getFromQueryWithId(levelId, sql);
    }

    private List<Course> getFromQueryWithId(int levelId, String sql) {
        List<Course> list;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, levelId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public List<Course> getAllByTrainer(int trainerId) {
        String sql = courseSelectByTrainer;
        log.info(sql + " find all by trainer " + trainerId);
        return getFromQueryWithId(trainerId, sql);
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
            throw new PersistException(e);
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
            throw new PersistException(e);
        }
    }

}