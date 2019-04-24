package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.User;

import java.util.List;

public interface IUserDao extends GenericDao<User, Integer> {
    User getByEmail(String email);
    List<User> getAllTrainers ();
    List<User> getLandingPageTrainers ();
    List<User> getByGroupId(Integer id);
}
