package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.CourseStatus;
import ua.com.nc.dto.DtoCourse;
import ua.com.nc.service.CourseService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private FiletransferServiceImpl fileService;

    @Override
    public void add(Course course) {
        courseDao.insert(course);
    }

    @Override
    public void update(Course course) {
        courseDao.update(course);
    }

    @Override
    public void edit(int id, String name, String level, String courseStatus, String isOnLandingPage, String desc, String startDay, String endDay) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CourseStatus status = CourseStatus.valueOf(courseStatus);
        Date starts = new Date();
        Date ends = starts;
        try {
            starts = format.parse(startDay);
            ends = format.parse(endDay);
        } catch (ParseException e) {
            log.trace(e);
        }
        int lvl = levelDao.getIdByName(level.trim());
        int statusId = status.ordinal();
        boolean isLanding = Boolean.parseBoolean(isOnLandingPage);
        courseDao.edit(id, name, lvl, statusId, isLanding,
                new java.sql.Date(starts.getTime()),
                new java.sql.Date(ends.getTime()), desc);
    }

    @Override
    public Course stringToObjCourse(String name, String user, String level,
                                    String courseStatus, String imageUrl, String isOnLandingPage,
                                    String desc, String startDay, String endDay) {
        //int statusId = statusDao.getIdByName(courseStatus.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int userId = 1;
        CourseStatus status = CourseStatus.valueOf(courseStatus);
        boolean isLanding = Boolean.parseBoolean(isOnLandingPage);
        Date startingDay = new Date();
        Date endingDay = startingDay;
        try {
            startingDay = format.parse(startDay);
            endingDay = format.parse(endDay);
        } catch (ParseException e) {
            log.trace(e);
        }
        int statusId = status.ordinal();
        int lvl = levelDao.getIdByName(level.trim());

        return new Course(name, lvl, statusId, userId, imageUrl,
                new java.sql.Date(startingDay.getTime()), new java.sql.Date(endingDay.getTime()),
                isLanding, desc);
    }


    /**
     * @return path to the image + it`s name.
     */
    @Override
    public String uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                String name = image.getOriginalFilename();
                String rootPath = "src/main/resources/img/";
                File dir = new File(rootPath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String filePath = dir.getAbsolutePath() + File.separator + name;
                int i =1;
                while (fileService.downloadFileFromServer(filePath)!=null){
                    filePath = dir.getAbsolutePath() + File.separator + name+" ("+i+")";
                }
                File uploadedFile = new File(filePath);
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                    ) {
                        stream.write(bytes);
                    }
                try(FileInputStream stream = new FileInputStream(filePath)) {
                    String path = "/img";
                    filePath = path + "/" + name;
                    fileService.uploadFileToServer(path, name, stream);

                    return filePath;
                }
            } catch (Exception e) {
                log.trace(e);
            }
        }
        return null;
    }

    @Override
    public String uploadImage(MultipartFile image, int courseId) {
        String filePath = uploadImage(image);
        Course course = courseDao.getEntityById(courseId);
        course.setImageUrl(filePath);
        courseDao.update(course);
        return filePath;
    }

    @Override
    public List<DtoCourse> getAllByTrainerAndEmployee(Integer trainerId, Integer employeeId) {
        List<Course> courses = courseDao.getAllCoursesByTrainerAndByEmployee(trainerId, employeeId);
        List<DtoCourse> dtoCourses = new ArrayList<>();
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                DtoCourse dtoCourse = new DtoCourse();
                dtoCourse.setId(course.getId());
                dtoCourse.setName(course.getName());
                dtoCourses.add(dtoCourse);
            }
        }

        return dtoCourses;
    }


}
