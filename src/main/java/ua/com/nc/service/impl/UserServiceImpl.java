package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Attendance;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;
import ua.com.nc.service.AttendanceService;
import ua.com.nc.service.EmailService;
import ua.com.nc.service.UserService;

import java.time.OffsetDateTime;
import java.util.*;

@Log4j2
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
    @Autowired
    private AttendanceStatusDao statusDao;
    @Autowired
    private AttendanceService attendanceService;

    @Override
    public void add(DtoUserSave dtoUserSave) {
        User user = new User();
        user.setFirstName(dtoUserSave.getFirstName());
        user.setLastName(dtoUserSave.getLastName());
        user.setPassword(dtoUserSave.getPassword());
        user.setEmail(dtoUserSave.getEmail());
        user.setCreated(OffsetDateTime.now());

        if (dtoUserSave.getRole() != null) {
            userDao.addUserRole(user.getId(), dtoUserSave.getRole().name());
            user.setActive(true);
            userDao.insert(user);
        } else {
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userDao.insert(user);

            DtoMailSender dtoMailSender = new DtoMailSender();
            dtoMailSender.setTo(user.getEmail());
            dtoMailSender.setSubject("Invite");
            dtoMailSender.setText("http://localhost:8080/users/activate/" + token);
            emailService.sendSimpleMessage(dtoMailSender);
        }
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

        DtoTeacherAndManager dtoManager = null;
        if (manager != null) {
            dtoManager = new DtoTeacherAndManager(manager);
        }

        DtoUserProfiles dtoUserProfiles = null;
        if (user != null) {
            dtoUserProfiles = new DtoUserProfiles(
                    user,
                    role,
                    user.isActive(),
                    dtoManager
            );
        }
        return dtoUserProfiles;
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

//    @Override
//    public void addEmployeeByAdmin(DtoMailSender dtoMailSender) {
//        User user = new User();
//        user.setEmail(dtoMailSender.getTo());
//
//        String token = UUID.randomUUID().toString();
//        user.setToken(token);
//        dtoMailSender.setText(dtoMailSender.getText() + "/" + token);
//
//        userDao.addUserByAdmin(user);
//        emailService.sendSimpleMessage(dtoMailSender);
//    }

    @Override
    public boolean activateUser(String token) {
        User user = userDao.getByToken(token);
        if (user == null) {
            return false;
        }
        user.setToken(null);
        user.setActive(true);
        userDao.updateActive(user);

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
    public List<DtoTeacherAndManager> getTrainersOfEmployee(Integer id) {
        List<User> teachers = userDao.getAllTrainersById(id);
        List<DtoTeacherAndManager> dtoTeachers = new ArrayList<>();
        if (teachers != null && teachers.size() != 0) {
            for (User teacher : teachers) {
                dtoTeachers.add(new DtoTeacherAndManager(teacher));
            }
        }
        return dtoTeachers;
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

    @Override
    public Map<String, Double> getAttandanceGraph(int userId) {
        List<Group> groups = groupDao.getAllGroupsByStudent(userId);
        Map<String, Integer> att = new HashMap<>();
        statusDao.getAll().forEach(r -> {
                    att.put(r.getTitle().toLowerCase(), 0);
                }
        );
        int allLessons = 0;
        for (Group g : groups) {
            List<Attendance> at = attendanceService.getAttendanceByStudentIdAndGroupId(userId, g.getId());
            if (at != null && !at.isEmpty()) {
                for (Attendance a : at) {
                    String status = a.getStatus().toLowerCase();
                    att.put(status, att.get(status) + 1);
                    allLessons++;
                }
            }
        }
        final int a = allLessons;
        Map<String, Double> result = new HashMap<>();
        att.forEach((key, value) -> result.put(key, (value * 100.0) / a));
        return result;
    }
}
