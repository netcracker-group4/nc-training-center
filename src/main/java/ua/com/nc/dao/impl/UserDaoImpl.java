package ua.com.nc.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.UserDao;
import ua.com.nc.model.User;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void add(User user) {
        jdbcTemplate.update(
                "insert into users (name, password) values (?, ?)",
                user.getName(), user.getPassword()
        );
    }
}
