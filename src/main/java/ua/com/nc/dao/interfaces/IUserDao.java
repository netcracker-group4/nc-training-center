package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.User;

import java.util.List;
import java.util.TreeMap;

public interface IUserDao extends GenericDao<User, Integer> {
    User getByEmail(String email);

    User getByToken(String token);

    List<User> getAllTrainers();

    List<User> getAllManagers();

    List<User> getLandingPageTrainers();

    User getManagerById(Integer id);

    List<User> getAllTrainersById(Integer id);

    List<User> getByGroupId(Integer id);

    List<User> getUngroupedByCourse(Integer id);

    void updateTrainerLandingPage(int id, boolean isOnLandingPage);

    List<User> getAllForCourse(int courseId);

    void updateUserByAdmin(User user);

    void updateActive(User user);

    List<User> getTrainersOnCourse(int id);

    User getTrainerByFeedback(Integer id);

    void addUserRole(Integer userId, String roleName);

    void addUserByAdmin(User user);

    public TreeMap<User, User> getStudentsAbsentWitNoReason(int lessonId);

    User getAdmin();

    User getLessonTrainer(int lessonId);


    User getTrainerByGroupId(Integer groupId);
}
