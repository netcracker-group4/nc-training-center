package ua.com.nc.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.nc.model.User;

public interface UserService extends UserDetailsService {
    void add(User user);

}
