package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;
import ua.com.nc.service.LandingPageService;

import java.util.List;

@Service
public class LandingPageServiceImpl implements LandingPageService {

    @Autowired
    private ICourseDao iCourseDao;

    @Autowired
    private IUserDao iUserDao;

    @Override
    public List<Course> getLandingPageCourses() {
        return iCourseDao.getLandingPageCourses();
    }

    @Override
    public List<User> getLandingPageTrainers() {
        return iUserDao.getLandingPageTrainers();
    }

    @Override
    public void updateCourseLandingPage(int id, boolean isOnLandingPage) {
        iCourseDao.updateCourseLandingPage(id, isOnLandingPage);
        iCourseDao.commit();
    }

    @Override
    public void updateTrainerLandingPage(int id, boolean isOnLandingPage) {
        iUserDao.updateTrainerLandingPage(id, isOnLandingPage);
        iUserDao.commit();
    }
}
