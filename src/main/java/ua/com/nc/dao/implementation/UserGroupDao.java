package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IUserGroupDao;
import ua.com.nc.domain.User;
import ua.com.nc.domain.UserGroup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserGroupDao extends GenericAbstractDao<UserGroup, Integer> implements IUserGroupDao {

    public UserGroupDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                          @Value("${spring.datasource.username}") String DATABASE_USER,
                          @Value("${spring.datasource.password}") String DATABASE_PASSWORD, SqlQueriesProperties sqlQueriesProperties) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        setSqlQueriesProperties(sqlQueriesProperties);
    }


    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected String getSelectByIdQuery() {
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return null;
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getUserGroupInsert();
    }

    @Override
    protected String getDeleteQuery() {
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        return sqlQueriesProperties.getUserGroupUpdate();
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
    statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, UserGroup entity) throws SQLException {
        statement.setInt(1, entity.getUserId());
        statement.setInt(2, entity.getGroupId());
        statement.setBoolean(3, entity.isAttending());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, UserGroup entity) throws SQLException {
        statement.setInt(1, entity.getGroupId());
        statement.setBoolean(2, entity.isAttending());
        statement.setInt(3, entity.getId());
    }

    @Override
    protected List<UserGroup> parseResultSet(ResultSet rs) throws SQLException {
        List<UserGroup> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            int userId = rs.getInt("user_id");
            int groupId = rs.getInt("group_id");
            boolean isAttending = rs.getBoolean("is_attending");
            UserGroup userGroup = new UserGroup(id, userId, groupId, isAttending);
            list.add(userGroup);
        }
        return list;
    }

    @Override
    public void deleteAllForGroup(int groupId) {
        String sql = sqlQueriesProperties.getUserGroupDeleteForGroup();
        log(sql, "LOG deleteAllForGroup");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setId(statement, groupId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
    }

    @Override
    public UserGroup getByUserAndCourse(int userId, int courseId) {
        String sql = sqlQueriesProperties.getUserGroupSelectByUsrAndCourse();
        log(sql, "getByUserAndCourse");
        List<UserGroup> list;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setInt(2, userId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list.size() > 1){
            throw new PersistException("Returned more than one record");
        }
        if (list.size() == 0 ){
            return null;
        }
        return list.iterator().next();
    }


}
