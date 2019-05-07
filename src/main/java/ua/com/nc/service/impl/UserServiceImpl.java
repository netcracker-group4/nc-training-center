package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.*;
import ua.com.nc.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IGroupDao groupDao;
    @Autowired
    private IRoleDao roleDao;
    @Autowired
    private IUserGroupDao userGroupDao;
    @Autowired
    private IFeedbackDao feedbackDao;

    @Override
    public void add(DtoUserSave dtoUserSave) {
        User user = new User();
        user.setFirstName(dtoUserSave.getFirstName());
        user.setLastName(dtoUserSave.getLastName());
        user.setPassword(dtoUserSave.getPassword());
        user.setEmail(dtoUserSave.getEmail());

        userDao.insert(user);
        userDao.addUserRole(user.getId(), dtoUserSave.getRole().name());

        userDao.commit();
    }

    @Override
    public List<DtoUser> getAll() {
        List<DtoUser> dtoUsers = new ArrayList<>();
        List<User> users = userDao.getAll();

        for (User user : users) {
//            List<DTOGroup> dtoGroups = new ArrayList<>();
//            List<Group> groups = groupDao.getAllGroupsByStudent(user.getId());
            List<Role> roles = roleDao.findAllByUserId(user.getId());
//            User manager = userDao.getManagerByUser(user.getId());
//            DtoManager dtoManager = null;
//            if (manager != null) {
//                dtoManager = new DtoManager(manager.getFirstName(), manager.getLastName());
//            }
            if (user != null) {
                dtoUsers.add(new DtoUser(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        roles,
                        user.isActive()));
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
        List<Feedback> feedbacks = feedbackDao.getAllByUserId(id);

        DtoTeacherAndManager dtoManager = null;
        if (manager != null) {
            dtoManager = new DtoTeacherAndManager(
                    manager.getId(),
                    manager.getFirstName(),
                    manager.getLastName()
            );
        }

        List<DtoTeacherAndManager> dtoTeachers = new ArrayList<>();
        if (teachers != null && teachers.size() != 0) {
            for (User teacher : teachers) {
                dtoTeachers.add(new DtoTeacherAndManager(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName()
                ));
            }
        }

        List<DtoGroup> dtoGroups = new ArrayList<>();
        if (groups != null && groups.size() != 0) {
            for (Group group : groups) {
                dtoGroups.add(new DtoGroup(group.getId(), group.getTitle()));
            }
        }

        List<DtoFeedback> dtoFeedbacks = new ArrayList<>();
        if (feedbacks != null && feedbacks.size() != 0) {
            for (Feedback feedback : feedbacks) {
                User teacher = userDao.getTrainerByFeedback(feedback.getId());
                DtoTeacherAndManager dtoTeacher = new DtoTeacherAndManager(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName()
                );

                dtoFeedbacks.add(new DtoFeedback(
                        feedback.getId(),
                        dtoTeacher,
                        feedback.getText(),
                        feedback.getTimeDate()
                ));

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
                    dtoGroups,
                    dtoFeedbacks
            );
        }
        return dtoUserProfiles;
    }

    @Override
    public void updateUserByAdmin(DtoUserProfiles dtoUserProfiles) {
        User user = new User();
        user.setId(dtoUserProfiles.getId());
        user.setFirstName(dtoUserProfiles.getFirstName());
        user.setLastName(dtoUserProfiles.getLastName());
        user.setManagerId(dtoUserProfiles.getDtoManager().getId());

        userGroupDao.deleteAllForUser(user.getId());

        for (DtoGroup dtoGroup : dtoUserProfiles.getGroups()) {
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(user.getId());
            userGroup.setGroupId(dtoGroup.getId());

            userGroupDao.insert(userGroup);
        }
        userDao.updateUserByAdmin(user);

        userGroupDao.commit();
        userDao.commit();

    }

    @Override
    public void updateActive(User user) {
        userDao.updateActive(user);
        userDao.commit();
    }

    @Override
    public List<DtoTeacherAndManager> getAllManagers() {
        List<DtoTeacherAndManager> dtoManagers = new ArrayList<>();
        List<User> managers = userDao.getAllManagers();
        for (User manager : managers) {
            dtoManagers.add(new DtoTeacherAndManager(
                    manager.getId(),
                    manager.getFirstName(),
                    manager.getLastName()
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
                    trainer.getLastName()
            ));
        }
        return dtoTrainers;
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
