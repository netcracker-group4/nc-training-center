package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Course;

import java.util.List;

public interface ICourseDao extends GenericDao<Course, Integer> {
    List<Course> getAllByLevel(int levelId);

    List<Course> getAllByTrainer(int levelId);

    List<Course> getLandingPageCourses();

    void updateCourseLandingPage(int id, boolean isOnLandingPage);

    Course getCourseById (int id);
}
