package ua.com.nc.dao.interfaces;

import ua.com.nc.model.User;

public interface IUserDao extends GenericDao<User, Integer> {
    User getByEmail(String email);
}
