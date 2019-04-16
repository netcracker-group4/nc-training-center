package ua.com.nc.dao;

import ua.com.nc.domain.User;

public interface UserDao {

    void add(User user);

    User findByUsername(String username);
}
