package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.model.User;
import ua.com.nc.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private IUserDao userDao;

    @Autowired
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add(User user) {
        try {
            userDao.insert(user);
            userDao.commit();
            userDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
