package ua.com.nc.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoTeacherAndManager;
import ua.com.nc.dto.DtoUser;
import ua.com.nc.dto.DtoUserProfiles;

import java.util.List;

public interface UserService extends UserDetailsService {
    void add(User user);

    List<DtoUser> getAll();

    DtoUserProfiles getById(Integer id);

    void updateUserByAdmin(DtoUserProfiles dtoUserProfiles);

    void updateActive(User user);

    List<DtoTeacherAndManager> getAllManagers();

}
