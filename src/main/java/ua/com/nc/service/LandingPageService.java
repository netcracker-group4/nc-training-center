package ua.com.nc.service;


public interface LandingPageService {

    String getLandingPageCourses ();

    String getLandingPageTrainers ();

    void updateCourseLandingPage (int id, boolean isOnLandingPage);

    void updateTrainerLandingPage (int id, boolean isOnLandingPage);
}