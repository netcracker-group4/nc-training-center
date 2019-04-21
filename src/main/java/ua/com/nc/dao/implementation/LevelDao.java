package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ILevelDao;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Level;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LevelDao  extends GenericAbstractDao<Level, Integer> implements ILevelDao {

    public LevelDao(@Value("${spring.datasource.url}") String DATABASE_URL,
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
        return sqlQueriesProperties.getLevelSelectById();
    }

    @Override
    protected String getSelectQuery() {
        return sqlQueriesProperties.getLevelSelectAll();
    }

    @Override
    protected String getInsertQuery() {
        return sqlQueriesProperties.getLevelInsert();
    }

    @Override
    protected String getDeleteQuery() {
        return sqlQueriesProperties.getLevelDelete();
    }

    @Override
    protected String getUpdateQuery() {
        return sqlQueriesProperties.getLevelUpdate();
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Level entity) throws SQLException {
        statement.setString(1, entity.getTitle());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Level entity) throws SQLException {
        statement.setString(1, entity.getTitle());
        statement.setInt(2, entity.getId());
    }

    @Override
    protected List<Level> parseResultSet(ResultSet rs) throws SQLException {
        List<Level> levels = new ArrayList<>();
        while (rs.next()){
            int id = rs.getInt("ID");
            String title = rs.getString("TITLE");
            Level level = new Level(id, title);
            levels.add(level);
        }
        return levels;
    }
}
