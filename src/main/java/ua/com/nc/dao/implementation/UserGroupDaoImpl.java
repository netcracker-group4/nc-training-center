package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.UserGroupDao;
import ua.com.nc.domain.UserGroup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import static jdk.nashorn.internal.objects.NativeMath.log;
@Log4j2
@Repository
@PropertySource("classpath:sql_queries.properties")
public class UserGroupDaoImpl extends AbstractDaoImpl<UserGroup> implements UserGroupDao {

    @Value("${usr_group.delete-all-for-group}")
    private String userGroupDeleteForGroup;
    @Value("${usr_group.select-by-usr-and-course}")
    private String userGroupSelectByUsrAndCourse;
    @Value("${usr_group.insert}")
    private String userGroupInsert;
    @Value("${usr_group.update}")
    private String userGroupUpdate;
    @Value("${usr_group.select-by-group}")
    private String userGroupSelectByGroup;
    @Value("${usr_group.select-by-usr-and-group}")
    private String userGroupSelectByUsrAndGroup;
    @Value("${usr_group.delete-all-for-user}")
    private String userGroupDeleteForUser;
    @Value("${usr_group.select-by-id}")
    private String userGroupSelectById;
    @Value("${usr_group.select-by-user-id}")
    private String userGroupSelectByUserId;

    @Autowired
    public UserGroupDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected String getSelectByIdQuery() {
        return userGroupSelectById;
    }

    @Override
    protected String getInsertQuery() {
        return userGroupInsert;
    }

    @Override
    protected String getUpdateQuery() {
        return userGroupUpdate;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, UserGroup entity) throws SQLException {
        statement.setInt(1, entity.getUserId());
        // if groupId is null then users has not been grouped yet
        if (entity.getGroupId() == null || entity.getGroupId() == 0) {
            statement.setNull(2, java.sql.Types.INTEGER);
        } else {
            statement.setInt(2, entity.getGroupId());
        }
        statement.setBoolean(3, entity.isAttending());
        statement.setInt(4, entity.getCourseId());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, UserGroup entity) throws SQLException {
        // if groupId is null then users has not been grouped yet
        if (entity.getGroupId() == null || entity.getGroupId() == 0) {
            statement.setNull(1, java.sql.Types.INTEGER);
        } else {
            statement.setInt(1, entity.getGroupId());
        }
        statement.setBoolean(2, entity.isAttending());
        statement.setInt(3, entity.getId());
    }

    @Override
    protected List<UserGroup> parseResultSet(ResultSet rs) throws SQLException {
        List<UserGroup> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer userId = rs.getInt("user_id");
            Integer groupId = rs.getInt("group_id");
            Integer courseId = rs.getInt("course_id");
            boolean isAttending = rs.getBoolean("is_attending");
            UserGroup userGroup = new UserGroup(id, userId, groupId, courseId, isAttending);
            list.add(userGroup);
        }
        log.debug("Retrieved UserGroups from database " + list);
        return list;
    }

    @Override
    public void deleteAllForGroup(Integer groupId) {
        String sql = userGroupDeleteForGroup;
        log.debug("delete employees from group  " + groupId + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, groupId);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }


    @Override
    public UserGroup getByUserAndCourse(Integer userId, Integer courseId) {
        String sql = userGroupSelectByUsrAndCourse;
        log.debug("get UserGroup by userId " + userId + " courseId " + courseId + " " + sql);
        return getUniqueByTwoId(sql, courseId, userId);
    }

    private UserGroup getUniqueByTwoId(String sql, Integer id1, Integer id2) {
        List<UserGroup> list = getFromSqlByTwoId(sql, id1, id2);
        if (list.size() > 1) {
            throw new PersistException("Returned more than one record");
        }
        if(list.isEmpty()){
            return null;
        }
        return list.iterator().next();
    }

    @Override
    public UserGroup getByUserAndGroup(Integer userId, Integer groupId) {
        String sql = userGroupSelectByUsrAndGroup;
        log.debug("UserAndGroup by userId " + userId + " groupId " + groupId + " " + sql);
        return getUniqueByTwoId(sql, groupId, userId);
    }

    @Override
    public List<UserGroup> getByUser(Integer userId) {
        String sql = userGroupSelectByUserId;
        log.debug("get all userGroups by userId " + userId);
        return getFromSqlById(sql, userId);
    }
}
