package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.LevelDao;
import ua.com.nc.domain.Level;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class LevelDaoImpl extends AbstractDaoImpl<Level> implements LevelDao {

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

    @Autowired
    public LevelDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
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
        String sql = levelSelectByTrainer;
        log.info(sql + " find all by trainer " + trainerId);
        return getFromSqlById(sql, trainerId);
    }

    @Override
    public int getIdByName(String name) {
        String sql = levelSelectByName;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt("ID");
        } catch (SQLException e) {
            log.trace(e);
            throw new RuntimeException(new SQLException("Level " + name + " not found"));
        }


    }
}
