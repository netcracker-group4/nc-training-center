package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.CourseStatus;

import java.util.List;

public interface ICourseStatus {
    int getIdByName(String name);

    CourseStatus getCourseStatusById(int id);
}
