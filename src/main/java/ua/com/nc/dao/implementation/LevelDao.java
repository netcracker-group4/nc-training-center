package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ILevelDao;
import ua.com.nc.domain.Level;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class LevelDao extends GenericAbstractDao<Level, Integer> implements ILevelDao {

    @Value("${level.select-all}")
    private String levelSelectAll;
    @Value("${level.select-by-id}")
    private String levelSelectById;
    @Value("${level.select-by-name}")
    private String levelSelectByName;
    @Value("${level.update}")
    private String levelUpdate;
    @Value("${level.delete}")
    private String levelDelete;
    @Value("${level.insert}")
    private String levelInsert;
    @Value("${level.select-by-trainer}")
    private String levelSelectByTrainer;

    public LevelDao(@Value("${spring.datasource.url}") String DATABASE_URL,
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
        return levelSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return levelSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return levelInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return levelDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return levelUpdate;
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
        while (rs.next()) {
            int id = rs.getInt("ID");
            String title = rs.getString("TITLE");
            Level level = new Level(id, title);
            levels.add(level);
        }
        return levels;
    }


    @Override
    public List<Level> getAllByTrainer(int trainerId) {
        List<Level> list;
        String sql = levelSelectByTrainer;
        log.info(sql + " find all by trainer " + trainerId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public int getIdByName(String name) {
        String sql = levelSelectByName;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt("ID");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(new SQLException("Level " + name + " not found"));
        }


    }
}
