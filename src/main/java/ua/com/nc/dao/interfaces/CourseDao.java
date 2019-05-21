package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Course;

import java.sql.Date;
import java.util.List;

public interface CourseDao extends GenericDao<Course> {
    List<Course> getAllByLevel(int levelId);

    List<Course> getAllByTrainer(int levelId);

    List<Course> getLandingPageCourses();

    void updateCourseLandingPage(int id, boolean isOnLandingPage);

    Course getCourseByGroup(int id);

    Course getCourseByFeedback(Integer feedbackId);

    List<Course> getAllCoursesByTrainerAndByEmployee(Integer trainerId, Integer employeeId);

    void edit(int id, String name, int lvl, int statusId, boolean isLanding, Date starts, Date ends, String desc);
}
