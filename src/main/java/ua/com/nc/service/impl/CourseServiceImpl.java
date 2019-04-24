package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.dao.interfaces.ICourseStatus;
import ua.com.nc.dao.interfaces.ILevelDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.CourseStatus;
import ua.com.nc.service.CourseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private ICourseDao courseDao;
    @Autowired
    private ILevelDao levelDao;
    //TODO Create all implementations for this bean, then uncomment 1st line of add(...) mthd
    private ICourseStatus statusDao;

    @Override
    public void add(Course course) {
        courseDao.insert(course);
        courseDao.commit();
    }

    @Override
    public void update(Course course){
        courseDao.update(course);
        courseDao.commit();
    }

    @Override
    public Course stringToObjCourse(String name, String user, String level, String courseStatus,
                    String imageUrl, String isOnLandingPage, String desc, String startDay,String endDay){
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

        return new Course(name,lvl,statusId,userId,imageUrl,
                new java.sql.Date(startingDay.getTime()),new java.sql.Date(endingDay.getTime()),isLanding,desc);
    }
}
