package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IGroupDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GroupDao extends GenericAbstractDao<Group,Integer> implements IGroupDao {

    public GroupDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                     @Value("${spring.datasource.username}") String DATABASE_USER,
                     @Value("${spring.datasource.password}") String DATABASE_PASSWORD, SqlQueriesProperties sqlQueriesProperties) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        setSqlQueriesProperties(sqlQueriesProperties);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt(1);
        } else throw new PersistException("No value returned!");
    }

    @Override
    protected String getSelectByIdQuery() {
        return sqlQueriesProperties.getGroupSelectById();
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getGroupeSelectAll();
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getGroupInsert();
    }

    @Override
    protected String getDeleteQuery() {
        return sqlQueriesProperties.getGroupDelete();
    }

    @Override
    protected String getUpdateQuery() {
        return sqlQueriesProperties.getGroupUpdate();
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Group entity) throws SQLException {
        statement.setInt(1,entity.getCourseId());
        statement.setString(2,entity.getTitle());
    }


    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Group entity) throws SQLException {
        statement.setInt(1,entity.getCourseId());
        statement.setString(2,entity.getTitle());
        statement.setInt(3,entity.getId());
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs) throws SQLException {
        List<Group> groups = new ArrayList<>();
        while (rs.next()){
            int id = rs.getInt(1);
            int courseId = rs.getInt(2);
            String title = rs.getString(3);
            Group group = new Group(id,courseId,title);
            groups.add(group);
        }
        return groups;
    }

    @Override
    public List<Group> getAllGroupsOfCourse(int courseId) {
        String sql = sqlQueriesProperties.getGroupSelectByCourse();
        log(sql, "find all by level");
        return getFromQueryWithId(courseId, sql);
    }

    @Override
    public int getNumberOfEmployeesInGroup(int groupId) {
        String sql = sqlQueriesProperties.getGroupSelectNumberOfEmployees();
        log(sql, "select number of emp for a group");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet rs = statement.executeQuery();
            //although it is not an id, this query returns only one number, so this method can parse it
             return parseId(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
    }

    @Override
    public List<Group> getAllGroupsByStudent(int studentId) {
        String sql = sqlQueriesProperties.getGroupSelectByEmployee();
        log(sql, "select all groups");
        return getFromQueryWithId(studentId, sql);
    }

    private List<Group> getFromQueryWithId(int studentId, String sql) {
        List<Group> groups;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            ResultSet rs = statement.executeQuery();
            groups = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return groups;
    }

    public List<Group> getGroupByTrainerId(Integer id) {
        String sql = sqlQueriesProperties.getGroupSelectByTrainerId();
        return getFromQueryWithId(id, sql);
    }

}
