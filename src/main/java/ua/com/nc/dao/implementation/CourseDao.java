package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.domain.Course;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao extends GenericAbstractDao<Course, Integer> implements ICourseDao {

    public CourseDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                   @Value("${spring.datasource.username}") String DATABASE_USER,
                   @Value("${spring.datasource.password}") String DATABASE_PASSWORD, SqlQueriesProperties sqlQueriesProperties) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        setSqlQueriesProperties(sqlQueriesProperties);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt("ID");
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        return sqlQueriesProperties.getCourseSelectById();
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getCourseSelectAll();
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getCourseInsert();
    }

    @Override
    protected String getDeleteQuery() {
        return sqlQueriesProperties.getCourseDelete();
    }

    @Override
    protected String getUpdateQuery() {
        return sqlQueriesProperties.getCourseUpdate();
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
}
