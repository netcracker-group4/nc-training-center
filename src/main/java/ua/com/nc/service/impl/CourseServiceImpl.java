package ua.com.nc.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.domain.schedule.ParsedSchedule;
import ua.com.nc.domain.schedule.ScheduleForUser;
import ua.com.nc.service.CourseService;

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

    //TODO Create all implementations for this bean, then uncomment 1st line of add(...) mthd
    private ICourseStatus statusDao;

    @Override
    public void add(Course course) {
        courseDao.insert(course);
        courseDao.commit();
    }

    @Override
    public void add(String name, int userId, String level, CourseStatus courseStatus,
                    String imageUrl, boolean isLanding, String desc, Date startingDay, Date endingDay) {
        //int statusId = statusDao.getIdByName(courseStatus.getName());
        int statusId = 1;
        System.err.println(level.trim());
        int lvl = levelDao.getIdByName(level.trim());
        Course course = new Course(name, lvl, statusId, userId, imageUrl,
                new java.sql.Date(startingDay.getTime()), new java.sql.Date(endingDay.getTime()), isLanding, desc);
        System.err.println(course.toString());
        add(course);
    }

    private int startOfDay = 8;
    private int endOfDay = 21;


    @Override
    public String getDesiredScheduleForCourse(int courseId) throws Exception {
        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getAllForCourse(courseId);
        System.out.println(desiredScheduleList);
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        for (User user : iUserDao.getAllForCourse(courseId)) {
            scheduleForUsers.add(new ScheduleForUser(user,
                    parseSchedules(desiredScheduleList, iSuitabilityDao.getAll()),
                    startOfDay, endOfDay));
        }
        return new Gson().toJson(scheduleForUsers);
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
