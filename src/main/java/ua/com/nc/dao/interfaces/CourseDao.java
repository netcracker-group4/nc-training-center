package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Course;

import java.util.List;

public interface CourseDao extends GenericDao<Course> {
    List<Course> getAllByLevel(int levelId);

    List<Course> getAllByTrainer(int levelId);

    List<Course> getLandingPageCourses();

    void updateCourseLandingPage(int id, boolean isOnLandingPage);

    Course getCourseByGroup(int id);

}
