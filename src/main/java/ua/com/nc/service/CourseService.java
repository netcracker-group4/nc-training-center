package ua.com.nc.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.CourseStatus;

import java.util.Date;

public interface CourseService {
    void add(Course course);

    void update(Course course);

    Course stringToObjCourse(String name, String user, String level, String courseStatus,
                             String imageUrl, String isOnLandingPage, String desc, String startDay, String endDay);
    void add(String name, int userId, String lvl, CourseStatus courseStatus,
             String imageUrl, boolean isLanding, String desc, Date startingDay, Date endingDay);
    String getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception;
    String getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception;
    String getDayIntervals();

    String uploadImage(MultipartFile image);
}
