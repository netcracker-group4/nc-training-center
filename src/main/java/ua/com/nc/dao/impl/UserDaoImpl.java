package ua.com.nc.dao.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.UserDao;
import ua.com.nc.domain.User;
import ua.com.nc.mapper.UserRowMapper;


@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void add(User user) {
        String insertUserQuery = "insert into usr (email, firstname, lastname, password) values (?, ?, ?, ?)";

        jdbcTemplate.update(insertUserQuery,
                            user.getUsername(),
                            user.getFirstname(),
                            user.getLastname(),
                            user.getPassword()
        );
    }

    @Override
    public User findByUsername(String username) {
        String findByUsernameQuery = "select user_id, email, password from usr where email = ?;";

        User user = null;

        user = jdbcTemplate.queryForObject(findByUsernameQuery, new Object[] { username },new UserRowMapper());

        if(user == null){
            user = new User();
        }
        return user;
    }
}
