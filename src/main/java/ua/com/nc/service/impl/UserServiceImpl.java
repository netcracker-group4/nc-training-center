package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.implementation.GenericAbstractDao;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.dao.interfaces.IGroupDao;
import ua.com.nc.dao.interfaces.IRoleDao;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.dto.DtoTeacherAndManager;
import ua.com.nc.dto.DtoUser;
import ua.com.nc.dto.DtoUserProfiles;
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
    private ICourseDao iCourseDao;

    @Override
    public void add(User user) {
        userDao.insert(user);
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
                int courseId = group.getCourseId();
                String courseName = iCourseDao.getEntityById(courseId).getName();
                dtoGroups.add(new DtoGroup(group.getId(), group.getTitle(), courseId, courseName));
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

    @Override
    public void updateUserByAdmin(DtoUserProfiles dtoUserProfiles) {
//        userDao.updateUserByAdmin(user);
//        userDao.commit();

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
