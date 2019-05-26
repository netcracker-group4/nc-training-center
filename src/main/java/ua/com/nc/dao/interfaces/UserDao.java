package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface UserDao extends GenericDao<User> {
    User getByEmail(String email);

    User getByToken(String token);

    int getIdByName(String name);

    List<User> getAllTrainers();

    List<User> getAllManagers();

    List<User> getLandingPageTrainers();

    User getManagerByEmployeeId(Integer id);

    List<User> getEmployeeTrainersByEmployeeId(Integer id);

    List<User> getByGroupId(Integer id);

    List<User> getUngroupedByCourse(Integer id);

    void updateTrainerLandingPage(int id, boolean isOnLandingPage);

    void updateActive(User user);

    List<User> getTrainersOnCourse(int id);

    User getTrainerByFeedback(Integer id);

    void addUserRole(Integer userId, String roleName);

    HashMap<User, User> getStudentsAbsentWitNoReason(int lessonId);

    User getAdmin();

    User getLessonTrainer(int lessonId);

    User getTrainerByCourseId(Integer courseId);

    List<User> getSubordinatesOfManager(Integer managerId);

    List<User> getStudentsByLessonId(Integer lessonId);

    void updateImage(User user);

    void updatePassword(User user);

}
