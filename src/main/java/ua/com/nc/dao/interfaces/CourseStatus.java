package ua.com.nc.dao.interfaces;

public interface CourseStatus {
    int getIdByName(String name);

    ua.com.nc.domain.CourseStatus getCourseStatusById(int id);
}
