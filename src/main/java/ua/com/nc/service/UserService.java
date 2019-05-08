package ua.com.nc.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;

import java.util.List;

public interface UserService extends UserDetailsService {
    void add(DtoUserSave user);

    List<DtoUser> getAll();

    DtoUserProfiles getById(Integer id);

    void updateUserByAdmin(DtoUserProfiles dtoUserProfiles);

    void updateActive(User user);

    List<DtoTeacherAndManager> getAllManagers();
    List<DtoTeacherAndManager> getAllTrainers();

    void addEmployeeByAdmin(DtoMailSender dtoMailSender);

    //List<User> getAllTrainers();
}
