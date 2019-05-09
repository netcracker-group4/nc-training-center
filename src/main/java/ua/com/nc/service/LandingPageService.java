package ua.com.nc.service;


import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;

import java.util.List;

public interface LandingPageService {

    List<Course> getLandingPageCourses();

    List<User> getLandingPageTrainers();

    void updateCourseLandingPage(int id, boolean isOnLandingPage);

    void updateTrainerLandingPage(int id, boolean isOnLandingPage);
}