package ua.com.nc.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.domain.Course;
import ua.com.nc.dto.DtoCourse;

import java.io.InputStream;
import java.util.List;

public interface CourseService {
    void add(Course course);

    void update(Course course);

    Course stringToObjCourse(String name, String user, String level, String courseStatus,
                             String imageUrl, String isOnLandingPage, String desc, String startDay, String endDay);


    String uploadImage(MultipartFile image);
    String uploadImage(MultipartFile image,int courseId);

    List<DtoCourse> getAllByTrainerAndEmployee(Integer trainerId, Integer employeeId);

    void edit(int id, String name, String level, String courseStatus, String isOnLandingPage, String desc, String startDay, String endDay);

    InputStream getImage(String imageName);
}
