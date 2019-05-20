package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.CourseStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class CourseStatusDaoImpl implements CourseStatus {

    @Value("${status.select-id-by-name}")
    private String getStatusByName;
    @Value("${status.select-name-by-id}")
    private String getStatusById;

    private Connection connection;

    @Autowired
    CourseStatusDaoImpl(DataSource dataSource) throws PersistException {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.trace("Error while setting autocommit false", e);
            throw new PersistException("Error while setting autocommit false", e);
        }
    }

    @Override
    public ua.com.nc.domain.CourseStatus getCourseStatusById(int id) {
        String name;
        String sql = getStatusById;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            name = rs.getString("NAME");
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return ua.com.nc.domain.CourseStatus.valueOf(name.toUpperCase());
    }

    @Override
    public int getIdByName(String name) {
        int id;
        String sql = getStatusByName;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            id = rs.getInt("ID");
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return id;
    }
}
