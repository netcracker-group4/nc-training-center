package ua.com.nc.service.impl;

import com.google.gson.Gson;
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
    public String getLandingPageCourses () {
        List <Course> landingPageCourses = iCourseDao.getLandingPageCourses();
        return new Gson().toJson(landingPageCourses);
    }

    @Override
    public String getLandingPageTrainers () {
        List <User>  landingPageTrainers = iUserDao.getLandingPageTrainers();
        return new Gson().toJson(landingPageTrainers);
    }
}
