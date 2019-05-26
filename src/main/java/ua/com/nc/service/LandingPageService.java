package ua.com.nc.service;


import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;

import java.util.List;

public interface LandingPageService {

    List<Course> getLandingPageCourses();

    List<User> getLandingPageTrainers();

    void updateCourseLandingPage(Integer id, boolean isOnLandingPage);

    void updateTrainerLandingPage(Integer id, boolean isOnLandingPage);
}