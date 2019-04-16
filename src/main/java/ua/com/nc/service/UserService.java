package ua.com.nc.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.nc.domain.User;

public interface UserService extends UserDetailsService {
    void add(User user);

}
