package ua.com.nc.service;

import ua.com.nc.domain.Course;
import ua.com.nc.domain.CourseStatus;

import java.util.Date;

public interface CourseService {
    void add(Course course);
    void add(String name, int userId, String lvl, CourseStatus courseStatus,
             String imageUrl, boolean isLanding, String desc, Date startingDay, Date endingDay);
}
