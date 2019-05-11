package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.*;
import ua.com.nc.service.EmailService;
import ua.com.nc.service.UserService;

import java.util.*;

@Log4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private EmailService emailService;

    @Override
    public void add(DtoUserSave dtoUserSave) {
        User user = new User();
        user.setFirstName(dtoUserSave.getFirstName());
        user.setLastName(dtoUserSave.getLastName());
        user.setPassword(dtoUserSave.getPassword());
        user.setEmail(dtoUserSave.getEmail());
        userDao.insert(user);
        userDao.addUserRole(user.getId(), dtoUserSave.getRole().name());
    }

    @Override
    public List<DtoUser> getAll() {
        List<DtoUser> dtoUsers = new ArrayList<>();
        List<User> users = userDao.getAll();
        if (!users.isEmpty()) {
            for (User user : users) {
//            List<DTOGroup> dtoGroups = new ArrayList<>();
//            List<Group> groups = groupDao.getAllGroupsByStudent(user.getId());
                List<Role> roles = roleDao.findAllByUserId(user.getId());
//            User manager = userDao.getManagerByUser(user.getId());
//            DtoManager dtoManager = null;
//            if (manager != null) {
//                dtoManager = new DtoManager(manager.getFirstName(), manager.getLastName());
//            }
                dtoUsers.add(new DtoUser(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        roles,
                        user.isActive(),
                        user.getImageUrl()));
            }
        }
        return dtoUsers;
    }

    @Override
    public DtoUserProfiles getById(Integer id) {
        User user = userDao.getEntityById(id);
        List<Role> role = roleDao.findAllByUserId(id);
        User manager = userDao.getManagerById(id);
        List<User> teachers = userDao.getAllTrainersById(id);
        List<Group> groups = groupDao.getAllGroupsByStudent(id);
        List<Level> levels = levelDao.getAll();

        DtoTeacherAndManager dtoManager = null;
        if (manager != null) {
            dtoManager = new DtoTeacherAndManager(manager);
        }

        List<DtoTeacherAndManager> dtoTeachers = new ArrayList<>();
        if (teachers != null && teachers.size() != 0) {
            for (User teacher : teachers) {
                dtoTeachers.add(new DtoTeacherAndManager(teacher));
            }
        }

        List<DtoGroup> dtoGroups = new ArrayList<>();
        if (groups != null && groups.size() != 0) {
            for (Group group : groups) {
                int courseId = group.getCourseId();
                Course course = courseDao.getEntityById(courseId);
                String courseName = course.getName();
                dtoGroups.add(new DtoGroup(group.getId(), group.getTitle(), courseId,
                        courseName, course.getUserId(),
                        getLevelName(levels, course.getLevel())));
            }
        }

        DtoUserProfiles dtoUserProfiles = null;
        if (user != null) {
            dtoUserProfiles = new DtoUserProfiles(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getImageUrl(),
                    role,
                    user.isActive(),
                    dtoManager,
                    dtoTeachers,
                    dtoGroups
            );
        }
        return dtoUserProfiles;
    }

    private String getLevelName(List<Level> levels, int levelId) {
        for (Level level : levels) {
            if (level.getId() == levelId) {
                return level.getTitle();
            }
        }
        return "Unknown";
    }

    @Override
    public User getByEmail(String email) {

        return userDao.getByEmail(email);
    }

    @Override
    public void updateUserByAdmin(DtoUserProfiles dtoUserProfiles) {
        User user = userDao.getEntityById(dtoUserProfiles.getId());
        user.setFirstName(dtoUserProfiles.getFirstName());
        user.setLastName(dtoUserProfiles.getLastName());
        user.setManagerId(dtoUserProfiles.getDtoManager().getId());
        userDao.update(user);

    }

    @Override
    public void updateActive(User user) {
        userDao.updateActive(user);
    }

    @Override
    public List<DtoTeacherAndManager> getAllManagers() {
        List<DtoTeacherAndManager> dtoManagers = new ArrayList<>();
        List<User> managers = userDao.getAllManagers();
        for (User manager : managers) {
            dtoManagers.add(new DtoTeacherAndManager(
                    manager.getId(),
                    manager.getFirstName(),
                    manager.getLastName(),
                    manager.getImageUrl(),
                    manager.isActive(),
                    manager.getEmail()
            ));
        }
        return dtoManagers;
    }

    @Override
    public List<DtoTeacherAndManager> getAllTrainers() {
        List<DtoTeacherAndManager> dtoTrainers = new ArrayList<>();
        List<User> trainers = userDao.getAllTrainers();
        for (User trainer : trainers) {
            dtoTrainers.add(new DtoTeacherAndManager(
                    trainer.getId(),
                    trainer.getFirstName(),
                    trainer.getLastName(),
                    trainer.getImageUrl(),
                    trainer.isActive(),
                    trainer.getEmail()
            ));
        }
        return dtoTrainers;
    }

    @Override
    public void addEmployeeByAdmin(DtoMailSender dtoMailSender) {
        User user = new User();
        user.setEmail(dtoMailSender.getTo());

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        dtoMailSender.setText(dtoMailSender.getText() + "/" + token);

        userDao.addUserByAdmin(user);
        emailService.sendSimpleMessage(dtoMailSender);
    }

    @Override
    public boolean activateUser(String token) {
        User user = userDao.getByToken(token);
        if (user == null) {
            return false;
        }
        user.setToken(null);

        return true;
    }

    @Override
    public List<DtoTeacherAndManager> getSubordinatesOfManager(Integer managerId) {
        List<DtoTeacherAndManager> dtoSubordinates = new ArrayList<>();
        List<User> subordinatesOfManager = userDao.getSubordinatesOfManager(managerId);
        for (User subordinate : subordinatesOfManager) {
            dtoSubordinates.add(new DtoTeacherAndManager(subordinate));
        }
        return dtoSubordinates;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with such email not exist");
        } else {
            Set<Role> roles = new HashSet<>(roleDao.findAllByUserId(user.getId()));
            user.setRoles(roles);
            return user;
        }

    }
}
