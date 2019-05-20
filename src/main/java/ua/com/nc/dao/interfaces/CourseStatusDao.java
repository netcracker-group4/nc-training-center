package ua.com.nc.dao.interfaces;

public interface CourseStatusDao {

    int getIdByName(String name);

    ua.com.nc.domain.CourseStatus getCourseStatusById(int id);
}
