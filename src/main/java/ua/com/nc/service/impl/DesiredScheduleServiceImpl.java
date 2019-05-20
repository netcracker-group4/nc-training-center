package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private AttendanceStatusDao statusDao;
    @Autowired
    private AttendanceDao attendanceDao;

    private int startOfDay = 8;
    private int endOfDay = 21;


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
            UserGroup oldUserGroupForThisCourse = userGroupDao.getByUserAndCourse(newUser.getUserId(), group.getCourseId());
            if (oldUserGroupForThisCourse == null) {
                //if this user has not been grouped yet for this course
                userGroupDao.insert(new UserGroup(newUser.getUserId(), group.getId(), true));
            } else {
                //if this user already has been saved as a part of another group for this course
                oldUserGroupForThisCourse.setGroupId(group.getId());
                userGroupDao.update(oldUserGroupForThisCourse);
            }
        }
    }


    @Override
    public List<ScheduleForUser> getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception {
//        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getUngroupedForCourse(courseId);
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        for (User user : userDao.getUngroupedByCourse(courseId)) {
            UserGroup byUserAndCourse = userGroupDao.getByUserAndCourse(user.getId(), courseId);
            List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getByUsrGroupId(byUserAndCourse.getId());
            scheduleForUsers.add(new ScheduleForUser(byUserAndCourse.getId(), user,
                    byUserAndCourse.isAttending(),
                    parseSchedules(desiredScheduleList, suitabilityDao.getAll()),
                    startOfDay, endOfDay));
        }
        return scheduleForUsers;
    }

    @Override
    public List<GroupSchedule> getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception {
        List<GroupSchedule> scheduleForGroupsForCourse = new ArrayList<>();
        List<Suitability> all = suitabilityDao.getAll();
        List<Group> allGroupsForCourse = groupDao.getAllGroupsOfCourse(courseId);
        for (Group group : allGroupsForCourse) {
            List<ScheduleForUser> scheduleForUsersInGroup = new ArrayList<>();
            for (User user : userDao.getByGroupId(group.getId())) {
                UserGroup byUserAndCourse = userGroupDao.getByUserAndCourse(user.getId(), courseId);
                List<ParsedSchedule> desiredScheduleList = parseSchedules(
                        desiredScheduleDao.getByUsrGroupId(byUserAndCourse.getId()), all);
                scheduleForUsersInGroup.add(new ScheduleForUser(byUserAndCourse.getId(), user,
                        byUserAndCourse.isAttending(),
                        desiredScheduleList, startOfDay, endOfDay));
            }
            scheduleForGroupsForCourse.add(new GroupSchedule(group.getId(), group.getTitle(),
                    scheduleForUsersInGroup, courseId));
        }
        return scheduleForGroupsForCourse;
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
        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getAllForGroup(groupId);
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        List<Suitability> all = suitabilityDao.getAll();
        for (User user : userDao.getByGroupId(groupId)) {
            UserGroup byUserAndGroup = userGroupDao.getByUserAndGroup(user.getId(), groupId);
            if (byUserAndGroup != null) {
                scheduleForUsers.add(new ScheduleForUser(byUserAndGroup.getId(), user,
                        byUserAndGroup.isAttending(),
                        parseSchedules(desiredScheduleList, all),
                        startOfDay, endOfDay));
            }
        }
        return scheduleForUsers;
    }
//  @Override
//    public List<ScheduleForUser> getDesiredScheduleForGroup(int groupId) throws Exception {
//        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getAllForGroup(groupId);
//        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
//        List<Suitability> all = suitabilityDao.getAll();
//        for (User user : userDao.getByGroupId(groupId)) {
//            UserGroup byUserAndGroup = userGroupDao.getByUserAndGroup(user.getId(), groupId);
//            if (byUserAndGroup != null) {
//                scheduleForUsers.add(new ScheduleForUser(byUserAndGroup.getId(), user,
//                        byUserAndGroup.isAttending(),
//                        parseSchedules(desiredScheduleList, all),
//                        startOfDay, endOfDay));
//            }
//        }
//        return scheduleForUsers;
//    }


    @Override
    public String saveDesired(Integer id, DesiredToSave desiredToSave) {
        Integer courseId = desiredToSave.getCourseId();
        UserGroup userGroup = userGroupDao.getByUserAndCourse(id, courseId);
        if (userGroup == null) {
            userGroup = new UserGroup(id, null, courseId, true);
            userGroupDao.insert(userGroup);
        }
        for (ScheduleForDay scheduleForDay : desiredToSave.getForDays()) {
            saveForDay(id, courseId, scheduleForDay);
        }
        return "saved";
    }

    private void saveForDay(Integer id, Integer courseId, ScheduleForDay scheduleForDay) {
        for (int i = startOfDay; i < endOfDay; i++) {
            if (scheduleForDay.getArray()[i - startOfDay] != -1) {
                String cronInterval = "0 " + i + " 0 " + (i + 1) + " " + scheduleForDay.getDay();
                DesiredSchedule desiredSchedule = new DesiredSchedule(id, courseId,
                        cronInterval, scheduleForDay.getArray()[i - startOfDay]);
                log.info("before saving");
                log.info(desiredSchedule);
                desiredScheduleDao.insert(desiredSchedule);
            }
        }
    }

}
