package ua.com.nc.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.dto.schedule.ParsedSchedule;
import ua.com.nc.dto.schedule.ScheduleForUser;
import ua.com.nc.service.CourseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private ICourseDao courseDao;
    @Autowired
    private ILevelDao levelDao;
    @Autowired
    private IDesiredScheduleDao desiredScheduleDao;
    @Autowired
    private ISuitabilityDao iSuitabilityDao;
    @Autowired
    private IUserDao iUserDao;
    @Autowired
    private IGroupDao iGroupDao;
@Autowired
    private IUserGroupDao iUserGroupDao;

    //TODO Create all implementations for this bean, then uncomment 1st line of add(...) mthd
    private ICourseStatus statusDao;

    @Override
    public void add(Course course) {
        courseDao.insert(course);
        courseDao.commit();
    }

    @Override
    public void update(Course course) {
        courseDao.update(course);
        courseDao.commit();
    }

    @Override
    public Course stringToObjCourse(String name, String user, String level, String courseStatus,
                                    String imageUrl, String isOnLandingPage, String desc, String startDay, String endDay) {
        //int statusId = statusDao.getIdByName(courseStatus.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int userId = 1;
//        CourseStatus status = CourseStatus.valueOf(courseStatus);
        CourseStatus status = CourseStatus.ENDED;
        boolean isLanding = Boolean.parseBoolean(isOnLandingPage);
        Date startingDay = new Date();
        Date endingDay = startingDay;
        try {
            startingDay = format.parse(startDay);
            endingDay = format.parse(endDay);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        int statusId = 1;
        System.err.println(level.trim());
        int lvl = levelDao.getIdByName(level.trim());

        return new Course(name, lvl, statusId, userId, imageUrl,
                new java.sql.Date(startingDay.getTime()), new java.sql.Date(endingDay.getTime()), isLanding, desc);
    }

    @Override
    public void add(String name, int userId, String lvl, CourseStatus courseStatus, String imageUrl, boolean isLanding, String desc, Date startingDay, Date endingDay) {

    }

    private int startOfDay = 8;
    private int endOfDay = 21;


    @Override
    public String getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception {
        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getAllForCourse(courseId);
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        for (User user : iUserDao.getUngroupedByCourse(courseId)) {
            scheduleForUsers.add(new ScheduleForUser(user,
                    parseSchedules(desiredScheduleList, iSuitabilityDao.getAll()),
                    startOfDay, endOfDay));
        }
        return new Gson().toJson(scheduleForUsers);
    }

    @Override
    public String getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception {
        List<GroupSchedule> scheduleForGroupsForCourse = new ArrayList<>();
        List<ParsedSchedule> desiredScheduleList = parseSchedules(
                        desiredScheduleDao.getAllForCourse(courseId),
                        iSuitabilityDao.getAll());
        List<Group> allGroupsForCourse = iGroupDao.getAllGroupsOfCourse(courseId);
        for (Group group : allGroupsForCourse) {
            List<ScheduleForUser> scheduleForUsersInGroup = new ArrayList<>();
            for (User user : iUserDao.getByGroupId(group.getId())) {
                scheduleForUsersInGroup.add(new ScheduleForUser(user,
                        desiredScheduleList, startOfDay, endOfDay));
            }
            scheduleForGroupsForCourse.add(new GroupSchedule(group.getId(), group.getTitle(),scheduleForUsersInGroup, courseId));
        }
        return new Gson().toJson(scheduleForGroupsForCourse);
    }


    private List<ParsedSchedule> parseSchedules(List<DesiredSchedule> desiredSchedules, List<Suitability> suitabilities) throws Exception {
        List<ParsedSchedule> result = new ArrayList<>();
        for (DesiredSchedule desiredSchedule : desiredSchedules) {
            result.add(new ParsedSchedule(desiredSchedule, suitabilities));
        }
        return result;
    }

    @Override
    public String getDayIntervals() {
        List<String> dayIntervals = new ArrayList<>();
        for (int i = startOfDay; i < endOfDay; i++) {
            int halfDay = 12;
            int hours = (i % halfDay == 0) ? 12 : i % halfDay;
            String appendix = (i < 12) ? "am" : "pm";
            dayIntervals.add(hours + appendix);
        }
        return new Gson().toJson(dayIntervals);
    }

}
