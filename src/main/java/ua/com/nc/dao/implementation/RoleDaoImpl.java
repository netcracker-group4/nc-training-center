package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.RoleDao;
import ua.com.nc.domain.Role;

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
public class RoleDaoImpl implements RoleDao {

    private DataSource dataSource;

    @Value("${role.select-by-user-id}")
    private String findRolesByUserId;

    @Autowired
    RoleDaoImpl(DataSource dataSource) throws PersistException {
       this.dataSource = dataSource;
    }
    @Override
    public List<Role> findAllByUserId(Integer id) {
        List<Role> roles = new ArrayList<>();
        String sql = findRolesByUserId;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
