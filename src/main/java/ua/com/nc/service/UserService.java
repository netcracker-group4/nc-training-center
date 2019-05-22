package ua.com.nc.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    void add(DtoUserSave user);

    List<DtoUser> getAll();

    DtoUserProfiles getById(Integer id);

    User getEntityById(Integer id);

    User getByEmail(String email);

    void updateUserByAdmin(DtoUserProfiles dtoUserProfiles);

    void updateActive(User user);

    List<DtoTeacherAndManager> getAllManagers();

    List<DtoTeacherAndManager> getAllTrainers();

    boolean activateUser(String token);

    List<DtoTeacherAndManager> getSubordinatesOfManager(Integer id);

    List<DtoTeacherAndManager> getTrainersOfEmployee(Integer id);

    Map<String, Double> getAttandanceGraph(int userId);

    void uploadImage(DtoUserSave dtoUserSave);

    void updatePassword(DtoChangePassword changePassword);

    //List<User> getAllTrainers();
}
