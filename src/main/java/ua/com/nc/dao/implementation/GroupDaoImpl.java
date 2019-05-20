package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.domain.Group;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class GroupDaoImpl extends AbstractDaoImpl<Group> implements GroupDao {


    @Value("${group.select-all}")
    private String groupSelectAll;
    @Value("${group.select-by-id}")
    private String groupSelectById;
    @Value("${group.update}")
    private String groupUpdate;
    @Value("${group.delete}")
    private String groupDelete;
    @Value("${group.insert}")
    private String groupInsert;
    @Value("${group.select-by-course}")
    private String groupSelectByCourse;
    @Value("${group.select-number-of-employees}")
    private String groupSelectNumberOfEmployees;
    @Value("${group.select-by-employee}")
    private String groupSelectByEmployee;
    @Value("${group.select-by-trainer-id}")
    private String groupSelectByTrainerId;
    @Value("${group.delete-student}")
    private String deleteUserFromGroup;
    @Value("${group.select-by-user-id-and-group-id}")
    private String selectByUserIdAndGroupId;



    @Autowired
    public GroupDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }


    @Override
    protected String getSelectByIdQuery() {
        return groupSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return groupSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return groupInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return groupDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return groupUpdate;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Group entity) throws SQLException {
        statement.setInt(1, entity.getCourseId());
        statement.setString(2, entity.getTitle());
    }


    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Group entity) throws SQLException {
        statement.setInt(1, entity.getCourseId());
        statement.setString(2, entity.getTitle());
        statement.setInt(3, entity.getId());
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs) throws SQLException {
        List<Group> groups = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            int courseId = rs.getInt(2);
            String title = rs.getString(3);
            Group group = new Group(id, courseId, title);
            groups.add(group);
        }
        return groups;
    }

    @Override
    public List<Group> getAllGroupsOfCourse(int courseId) {
        String sql = groupSelectByCourse;
        log.info(sql + "find all by courseid" + courseId);
        return getFromSqlById(sql, courseId);
    }

    @Override
    public int getNumberOfEmployeesInGroup(int groupId) {
        String sql = groupSelectNumberOfEmployees;
        log.info(sql + " select number of emp for a group" + groupId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet rs = statement.executeQuery();
            return (rs.next()) ?  rs.getInt(1) : 0;
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<Group> getAllGroupsByStudent(int studentId) {
        String sql = groupSelectByEmployee;
        log.info(sql + "select all groups for student " + studentId);
        return getFromSqlById(sql, studentId);
    }

    @Override
    public Group getGroupById(Integer groupId) {
        String sql = groupSelectById;
        log.info(sql + "select group by id " + groupId);
        return getUniqueFromSqlById(sql, groupId);
    }

    @Override
    public List<Group> getGroupByTrainerId(Integer trainerId) {
        String sql = groupSelectByTrainerId;
        log.info(sql + "select all groups for trainer " + trainerId);
        return getFromSqlById(sql, trainerId);
    }

    @Override
    public void deleteUserFromGroup(Integer id, Integer userId) {
        try (PreparedStatement statement = connection.prepareStatement(deleteUserFromGroup)) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            statement.executeQuery();
        } catch (SQLException e) {
            log.trace(e);
        }
    }

    @Override
    public Group getByUserIdAndGroupId(Integer userId, Integer groupId) {
        List<Group> groups = null;
        String sql = selectByUserIdAndGroupId;
        try(PreparedStatement statement = connection.prepareStatement(selectByUserIdAndGroupId)) {
            statement.setInt(1, userId);
            statement.setInt(2, groupId);
            ResultSet resultSet = statement.executeQuery();
            groups = parseResultSet(resultSet);
        } catch (SQLException e) {
            log.trace(e + "select group by group id and user id");
        }
        if(groups != null & groups.size() > 0){
            return groups.get(0);
        }else {
            return null;
        }
    }
}
