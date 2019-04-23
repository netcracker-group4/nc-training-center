package ua.com.nc.service;


public interface LandingPageService {

    String getLandingPageCourses ();

    String getLandingPageTrainers ();

    void updateLandingPage (int id, boolean isOnLandingPage);

}