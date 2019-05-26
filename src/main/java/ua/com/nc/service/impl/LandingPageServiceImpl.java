package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;
import ua.com.nc.service.LandingPageService;

import java.util.List;

@Log4j2
@Service
public class LandingPageServiceImpl implements LandingPageService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Course> getLandingPageCourses() {
        return courseDao.getLandingPageCourses();
    }

    @Override
    public List<User> getLandingPageTrainers() {
        return userDao.getLandingPageTrainers();
    }

    @Override
    public void updateCourseLandingPage(Integer id, boolean isOnLandingPage) {
        courseDao.updateCourseLandingPage(id, isOnLandingPage);
    }

    @Override
    public void updateTrainerLandingPage(Integer id, boolean isOnLandingPage) {
        userDao.updateTrainerLandingPage(id, isOnLandingPage);
    }
}
