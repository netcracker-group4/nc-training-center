package ua.com.nc.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    /**
     * adding user to the system
     *
     * @param user   object with firstName, lastName, email, password and role
     * @return saved into database if this user not exist yet and return bad request in case
     * user already exist
     */
    void add(DtoUserSave user);

    /**
     * returns all users that already registered in the system
     *
     * @return list of users
     */
    List<DtoUser> getAll();

    /**
     * returns one user by id
     *
     * @param id    Id of user which need to get
     * @return user
     */
    DtoUserProfiles getById(Integer id);

    /**
     * returns one user by id
     *
     * @param id    Id of user which need to get
     * @return user
     */
    User getEntityById(Integer id);

    /**
     * returns one user by email
     *
     * @param email    Email of user which need to get
     * @return user
     */
    User getByEmail(String email);

    /**
     * update user data
     *
     * @param dtoUserProfiles   object with id, firstName, lastName and dtoManager
     * @return ok status if user update
     */
    void updateUserByAdmin(DtoUserProfiles dtoUserProfiles);

    /**
     * update user activity (active if user has been activated by confirming email)
     *
     * @param user   object with id, active
     * @return ok status if update user's active
     */
    void updateActive(User user);

    /**
     * update showing status of trainer (in case status equals "true" then it will be "false",
     * in case status equals "false" then it will be "true")
     *
     * @param user   object with id, onLandingPage
     * @return ok status if update user's onLandingPage field
     */
    void updateOnLandingPage(User user);

    /**
     * get all managers that present in the system
     *
     * @return all managers
     */
    List<DtoTeacherAndManager> getAllManagers();

    /**
     * get all trainers in the system
     *
     * @return users that are have role "trainer"
     */
    List<DtoTeacherAndManager> getAllTrainers();

    /**
     * activate user in the system that allow him to login in the system
     * (change active field in the data base to true)
     *
     * @param token    user's token
     * @return object which include url that redirect ot login page
     */
    void activateUser(String token);

    /**
     * get all subordinates by manager
     *
     * @param id    id of manager who subordinates gotten
     * @return users that are subordinates of this manager
     */
    List<DtoTeacherAndManager> getSubordinatesOfManager(Integer id);

    /**
     * get all trainers by employee
     *
     * @param id    id of employee
     * @return users that are trainers of this employee
     */
    List<DtoTeacherAndManager> getTrainersOfEmployee(Integer id);

    /**
     * get information about employee's attendance
     *
     * @param userId    user's id
     * @return attendance graph
     */
    Map<String, Double> getAttandanceGraph(Integer userId);

    /**
     * upload user's image
     * save image to the folder on server and save path to this image to data base
     *
     * @param dtoUserSave   object with id, image
     * @return ok status if user's image update
     */
    User uploadImage(DtoUserSave dtoUserSave);

    /**
     * update user password in data base
     *
     * @param changePassword   userId, oldPassword, newPassword
     * @return ok status if user's password update and
     * return bad status if user's old password is incorrect
     */
    void updatePassword(DtoChangePassword changePassword);

    /**
     * send new password to email if this user present and active in data base
     *
     * @param email   user's email
     * @return ok status if user's password is recover and
     * return bad status if user's email don't present in data base or this user don't active
     */
    void recoverPassword(String email);
}
