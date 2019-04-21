package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.User;
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
        userDao.insert(user);
        userDao.commit();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("User with such email not exist");
        }else{
            return user;
        }

    }
}
