package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.CourseStatusDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class CourseStatusDaoImpl implements CourseStatusDao {

    @Value("${status.select-id-by-name}")
    private String getStatusByName;
    @Value("${status.select-name-by-id}")
    private String getStatusById;

    private final DataSource dataSource;

    @Autowired
    CourseStatusDaoImpl(DataSource dataSource) throws PersistException {
        this.dataSource = dataSource;
    }

    @Override
    public ua.com.nc.domain.CourseStatus getCourseStatusById(int id) {
        String name;
        String sql = getStatusById;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            name = rs.getString("NAME");
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        return ua.com.nc.domain.CourseStatus.valueOf(name.toUpperCase());
    }

    @Override
    public int getIdByName(String name) {
        int id;
        String sql = getStatusByName;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            id = rs.getInt("ID");
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
        return id;
    }
}
