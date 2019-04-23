package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.dao.interfaces.ICourseStatus;
import ua.com.nc.dao.interfaces.ILevelDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.CourseStatus;
import ua.com.nc.service.CourseService;

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
    public void add(String name, int userId, String level, CourseStatus courseStatus,
                    String imageUrl, boolean isLanding, String desc, Date startingDay,Date endingDay){
        //int statusId = statusDao.getIdByName(courseStatus.getName());
        int statusId = 1;
        int lvl = levelDao.getIdByName(level);
        add(new Course(name,lvl,statusId,userId,imageUrl,
                new java.sql.Date(startingDay.getTime()),new java.sql.Date(endingDay.getTime()),isLanding,desc));
    }
}
