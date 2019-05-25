package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.schedule.*;
import ua.com.nc.service.DesiredScheduleService;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class DesiredScheduleServiceImpl implements DesiredScheduleService {
    @Autowired
    private DesiredScheduleDao desiredScheduleDao;
    @Autowired
    private SuitabilityDao suitabilityDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserGroupDao userGroupDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Value("${start.of.day}")
    private Integer startOfDay;
    @Value("${end.of.day}")
    private Integer endOfDay;


    @Override
    public int update(GroupSchedule groupSchedule) {
        Group groupToUpdate = groupDao.getEntityById(groupSchedule.getId());
        groupToUpdate.setTitle(groupSchedule.getName());
        groupDao.update(groupToUpdate);
        userGroupDao.deleteAllForGroup(groupToUpdate.getId());
        updateStudentsForGroup(groupSchedule, groupToUpdate);
        return groupToUpdate.getId();
    }


    @Override
    public boolean delete(int groupId) {
        userGroupDao.deleteAllForGroup(groupId);
        groupDao.delete(groupId);
        return true;
    }


    @Override
    public int add(GroupSchedule groupSchedule) {
        Group groupToInsert = new Group(groupSchedule.getCourseId(), groupSchedule.getName());
        groupDao.insert(groupToInsert);
        updateStudentsForGroup(groupSchedule, groupToInsert);
        return groupToInsert.getId();
    }


    @Override
    public void invertAttending(Integer userGroupId) {
        UserGroup userGroup = userGroupDao.getEntityById(userGroupId);
        userGroup.setAttending(!userGroup.isAttending());
        userGroupDao.update(userGroup);
    }

    private void updateStudentsForGroup(GroupSchedule groupSchedule, Group group) {
        List<ScheduleForUser> newUsers = groupSchedule.getGroupScheduleList();
        for (ScheduleForUser newUser : newUsers) {
            int userId = newUser.getUserId();
            int courseId = group.getCourseId();
            UserGroup oldUserGroupForThisCourse = userGroupDao.getByUserAndCourse(userId, courseId);
            oldUserGroupForThisCourse.setGroupId(group.getId());
            userGroupDao.update(oldUserGroupForThisCourse);
        }
    }


    @Override
    public List<ScheduleForUser> getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception {
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        for (User user : userDao.getUngroupedByCourse(courseId)) {
            ScheduleForUser scheduleForUser = getScheduleForUserForCourse(courseId, user);
            scheduleForUsers.add(scheduleForUser);
        }
        return scheduleForUsers;
    }


    private ScheduleForUser getScheduleForUserForCourse(int courseId, User user) throws Exception {
        List<Suitability> suitabilities = suitabilityDao.getAll();
        UserGroup byUserAndCourse = userGroupDao.getByUserAndCourse(user.getId(), courseId);
        return getScheduleForUserAndCourseGroup(user, suitabilities, byUserAndCourse);
    }

    private ScheduleForUser getScheduleForUserAndCourseGroup(
            User user, List<Suitability> suitabilities, UserGroup byUserAndCourse) throws Exception {
        Integer id = byUserAndCourse.getId();
        List<DesiredSchedule> desiredScheduleByUsrGroupId = desiredScheduleDao.getByUsrGroupId(id);
        List<ParsedSchedule> desiredScheduleList = parseSchedules(
                desiredScheduleByUsrGroupId, suitabilities);
        return new ScheduleForUser(id, user,
                byUserAndCourse.isAttending(),
                desiredScheduleList, startOfDay, endOfDay);
    }

    @Override
    public List<GroupSchedule> getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception {
        List<GroupSchedule> scheduleForGroupsForCourse = new ArrayList<>();
        List<Suitability> all = suitabilityDao.getAll();
        List<Group> allGroupsForCourse = groupDao.getAllGroupsOfCourse(courseId);
        for (Group group : allGroupsForCourse) {
            GroupSchedule groupSchedule = getGroupSchedule(courseId, group);
            scheduleForGroupsForCourse.add(groupSchedule);
        }
        return scheduleForGroupsForCourse;
    }

    private GroupSchedule getGroupSchedule(int courseId, Group group) throws Exception {
        List<ScheduleForUser> scheduleForUsersInGroup = new ArrayList<>();
        for (User user : userDao.getByGroupId(group.getId())) {
            ScheduleForUser scheduleForUser = getScheduleForUserForCourse(courseId, user);
            scheduleForUsersInGroup.add(scheduleForUser);
        }
        return new GroupSchedule(group.getId(), group.getTitle(),
                scheduleForUsersInGroup, courseId);
    }


    private List<ParsedSchedule> parseSchedules(List<DesiredSchedule> desiredSchedules, List<Suitability> suitabilities)
            throws Exception {
        List<ParsedSchedule> result = new ArrayList<>();
        for (DesiredSchedule desiredSchedule : desiredSchedules) {
            result.add(new ParsedSchedule(desiredSchedule, suitabilities));
        }
        return result;
    }

    @Override
    public List<String> getDayIntervals() {
        List<String> dayIntervals = new ArrayList<>();
        for (int i = startOfDay; i < endOfDay; i++) {
            int halfDay = 12;
            int hours = (i % halfDay == 0) ? 12 : i % halfDay;
            String appendix = (i < 12) ? "am" : "pm";
            dayIntervals.add(hours + appendix);
        }
        return dayIntervals;
    }

    @Override
    public List<ScheduleForUser> getDesiredScheduleForGroup(int groupId) throws Exception {
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        List<Suitability> suitabilities = suitabilityDao.getAll();
        for (User user : userDao.getByGroupId(groupId)) {
            UserGroup byUserAndGroup = userGroupDao.getByUserAndGroup(user.getId(), groupId);
            ScheduleForUser scheduleForUser = getScheduleForUserAndCourseGroup(user,
                    suitabilities, byUserAndGroup);
            scheduleForUsers.add(scheduleForUser);
        }
        return scheduleForUsers;
    }

    @Override
    public void saveDesired(Integer userId, DesiredToSave desiredToSave) {
        Integer courseId = desiredToSave.getCourseId();
        saveUsrGroup(userId, courseId);
        for (ScheduleForDay scheduleForDay : desiredToSave.getForDays()) {
            saveDesiredScheduleForDay(userId, courseId, scheduleForDay);
        }
    }

    private void saveUsrGroup(Integer userId, Integer courseId) {
        if (userGroupDao.getByUserAndCourse(userId, courseId) == null) {
            UserGroup userGroup = new UserGroup(userId, null, courseId, true);
            userGroupDao.insert(userGroup);
        }
    }

    private void saveDesiredScheduleForDay(Integer id, Integer courseId, ScheduleForDay scheduleForDay) {
        for (int i = startOfDay; i < endOfDay; i++) {
            int notSpecifiedByUser = -1;
            int numberInArray = i - startOfDay;
            if (scheduleForDay.getArray()[numberInArray] != notSpecifiedByUser) {
                String cronInterval = buildCronInterval(scheduleForDay, i);
                DesiredSchedule desiredSchedule = new DesiredSchedule(id, courseId,
                        cronInterval, scheduleForDay.getArray()[numberInArray]);
                desiredScheduleDao.insert(desiredSchedule);
            }
        }
    }

    private String buildCronInterval(ScheduleForDay scheduleForDay, int i) {
        return "0 " + i + " 0 " + (i + 1) + " " + scheduleForDay.getDay();
    }

}