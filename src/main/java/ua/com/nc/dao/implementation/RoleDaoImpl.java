package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.RoleDao;
import ua.com.nc.domain.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class RoleDaoImpl implements RoleDao {

    Connection connection;

    @Value("${role.select-by-user-id}")
    private String findRolesByUserId;

    public RoleDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
                       @Value("${spring.datasource.username}") String DATABASE_USER,
                       @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
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
    public List<Role> findAllByUserId(Integer id) {
        List<Role> roles = new ArrayList<>();
        String sql = findRolesByUserId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                roles.add(role);
            }
        } catch (SQLException e) {
            log.error("Sql exception in role DAO ", e);
        }
        return roles;
    }

}
