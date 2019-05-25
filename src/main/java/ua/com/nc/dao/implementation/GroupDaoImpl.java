package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.domain.Group;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
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
        log.info("Retrieved groups from database " + groups);
        return groups;
    }

    @Override
    public List<Group> getAllGroupsOfCourse(Integer courseId) {
        String sql = groupSelectByCourse;
        log.info("find all groups by courseId " + courseId + "  " + sql);
        return getFromSqlById(sql, courseId);
    }

    @Override
    public int getNumberOfEmployeesInGroup(Integer groupId) {
        String sql = groupSelectNumberOfEmployees;
        log.info("select number of employees for a groupId " + groupId + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
            ResultSet rs = statement.executeQuery();
            int numberOfEmployees = (rs.next()) ? rs.getInt(1) : 0;
            log.info("retrieved from database number of employees for a group " + numberOfEmployees);
            return numberOfEmployees;
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<Group> getAllGroupsByStudent(Integer studentId) {
        String sql = groupSelectByEmployee;
        log.info("select all groups by studentId " + studentId + " " + sql);
        return getFromSqlById(sql, studentId);
    }

    @Override
    public List<Group> getGroupByTrainerId(Integer trainerId) {
        String sql = groupSelectByTrainerId;
        log.info("select all groups for trainer " + trainerId + " " + sql );
        return getFromSqlById(sql, trainerId);
    }


    @Override
    public Group getByUserIdAndGroupId(Integer userId, Integer groupId) {
        List<Group> groups = null;
        String sql = selectByUserIdAndGroupId;
        try(    Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectByUserIdAndGroupId)) {
            statement.setInt(1, userId);
            statement.setInt(2, groupId);
            ResultSet resultSet = statement.executeQuery();
            groups = parseResultSet(resultSet);
        } catch (SQLException e) {
            log.error(e + "select group by group id and user id");
        }
        if(groups != null & groups.size() > 0){
            return groups.get(0);
        }else {
            return null;
        }
    }
}
