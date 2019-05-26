package ua.com.nc.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.implementation.UserGroupDaoImpl;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.DateDeserializer;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.dto.DtoUserProfiles;
import ua.com.nc.dto.schedule.DesiredToSave;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.ChatService;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
@Component("customSecuritySecurity")
public class CustomSecurityService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserGroupDaoImpl userGroupDao;
    @Autowired
    private ChatService chatService;


    public boolean hasPermissionToSeeScheduleOf(Authentication authentication, Integer userId) {
        User user = (User) authentication.getPrincipal();
        List<Role> roles = roleDao.findAllByUserId(userId);
        User userToRetrieve = userDao.getEntityById(userId);
        return (roles.contains(Role.TRAINER) ||
                roles.contains(Role.EMPLOYEE)) &&
                ((user.getRoles().contains(Role.ADMIN) || user.getId().equals(userId)) ||
                        (user.getRoles().contains(Role.MANAGER) &&
                                userToRetrieve.getManagerId().equals(user.getId())) ||
                        user.getRoles().contains(Role.TRAINER));
    }

    public boolean hasPermissionToAddLesson(Authentication authentication, String toAdd) {
        User user = (User) authentication.getPrincipal();
        return user.getRoles().contains(Role.ADMIN) || getTrainerId(toAdd).equals(user.getId());
    }

    public boolean hasPermissionToUpdateLesson(Authentication authentication, String toAdd) {
        User user = (User) authentication.getPrincipal();
        DtoLesson toAdd1 = getDtoLesson(toAdd);
        Lesson oldLesson  = lessonDao.getEntityById(toAdd1.getId());
        return user.getRoles().contains(Role.ADMIN) ||
                getTrainerId(toAdd).equals(user.getId()) ||
                oldLesson.getTrainerId().equals(user.getId());
    }

    private Integer getTrainerId(String toAdd) {
        DtoLesson toAdd1 = getDtoLesson(toAdd);
        return courseDao.getCourseByGroup(toAdd1.getGroupId()).getUserId();
    }

    private DtoLesson getDtoLesson(String toAdd) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,
                new DateDeserializer()).create();
        return gson.fromJson(toAdd, DtoLesson.class);
    }

    public boolean hasPermissionToDeleteLesson(Authentication authentication, Integer lessonId) {
        User user = (User) authentication.getPrincipal();
        return user.getRoles().contains(Role.ADMIN) || userDao.getTrainerByCourseId(
                lessonDao.getEntityById(lessonId).getGroupId()).getId().equals(user.getId());
    }

    public boolean hasPermissionToCancelLesson(Authentication authentication, Integer lessonId) {
        User user = (User) authentication.getPrincipal();
        log.info("user" + user);
        Lesson lesson = lessonDao.getEntityById(lessonId);
        User trainerByLesson = userDao.getTrainerByCourseId(
                courseDao.getCourseByGroup(lesson.getGroupId()).getId());
//        log.info("lesson" + lesson);
//        log.info("trainerByLesson" + trainerByLesson);
//        log.info("user.getId().equals(lesson.getTrainerId())" + (user.getId().equals(lesson.getTrainerId())));
//        log.info("trainerByLesson.getId().equals(user.getId())" + (trainerByLesson.getId().equals(user.getId())));
        return user.getRoles().contains(Role.ADMIN)
                || user.getId().equals(lesson.getTrainerId())
                || trainerByLesson.getId().equals(user.getId());
    }

    public boolean hasPermissionToRetrieveLessons(Authentication authentication, Integer groupId) {
        User user = (User) authentication.getPrincipal();
        UserGroup userGroup = userGroupDao.getByUserAndGroup(user.getId(), groupId);
        Integer courseTrainerId = userDao.getTrainerByCourseId(courseDao.getCourseByGroup(groupId).getId()).getId();
        return user.getRoles().contains(Role.ADMIN)
                || user.getRoles().contains(Role.TRAINER)
                || courseTrainerId.equals(user.getId())
                || userGroup != null;
    }

    public boolean hasPermissionToRetrieveGroups(Authentication authentication, Integer employeeId) {
        log.info(employeeId);
        return roleDao.findAllByUserId(employeeId).contains(Role.EMPLOYEE) ||
                roleDao.findAllByUserId(employeeId).contains(Role.TRAINER);
    }

    public boolean canJoinCourse(Authentication authentication, DesiredToSave desiredToSave) throws LogicException {
        User user = (User) authentication.getPrincipal();
        UserGroup userGroup = userGroupDao.getByUserAndCourse(user.getId(), desiredToSave.getCourseId());
        if (userGroup != null) {
            throw new LogicException("You can join course only once");
        }
        return true;
    }

    public boolean canWriteToGroupChat(Integer userId, Integer groupId){
        Group group = groupDao.getByUserIdAndGroupId(userId, groupId);
        User trainer = userDao.getTrainerByCourseId(groupId);
        return group != null || trainer.getId().equals(userId);
    }

    public boolean hasPermissionToUpdateBasicInfo(Authentication authentication, DtoUserProfiles dtoUserProfiles) {
        User user = (User) authentication.getPrincipal();
        return user.getRoles().contains(Role.ADMIN) || dtoUserProfiles.getId().equals(user.getId());
    }

    public boolean hasPermissionToUpdateAttendance(Authentication authentication, Integer attendanceId){
        User authenticatedUser = (User) authentication.getPrincipal();
        User userOfUpdatingLesson = userDao.getByAttendanceId(attendanceId);
        if(userOfUpdatingLesson != null){
            return  authenticatedUser.getRoles().contains(Role.ADMIN) |
                    authenticatedUser.getRoles().contains(Role.TRAINER) |
                    userOfUpdatingLesson.getManagerId() == authenticatedUser.getId() &
                            userOfUpdatingLesson.getId() != authenticatedUser.getId();
        }else{
            return false;
        }

    }

    public boolean hasPermissionToGetChatMessages(Authentication authentication, Integer chatId){
        User user = (User) authentication.getPrincipal();
        if(chatService.getByUserIdAndChatId(user.getId(), chatId) != null){
            return true;
        }else{
            return false;
        }
    }
}
