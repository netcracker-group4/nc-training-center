package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ICourseStatus;
import ua.com.nc.domain.CourseStatus;

import java.sql.*;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class CourseStatusDao implements ICourseStatus {

    @Value("${status.select-id-by-name")
    private String getStatusByName;
    @Value("${status.select-name-by-id}")
    private String getStatusById;

    private Connection connection;

    public CourseStatusDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                           @Value("${spring.datasource.username}") String DATABASE_USER,
                           @Value("${spring.datasource.password}") String DATABASE_PASSWORD) {
        try {
            this.connection = DriverManager.getConnection(DATABASE_URL,
                    DATABASE_USER, DATABASE_PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistException("Error while setting autocommit false", e);
        }
    }

    @Override
    public CourseStatus getCourseStatusById(int id) {
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
        return CourseStatus.valueOf(name.toUpperCase());
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
