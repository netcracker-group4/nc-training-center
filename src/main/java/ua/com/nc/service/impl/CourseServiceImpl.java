package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Course;
import ua.com.nc.dto.DtoCourse;
import ua.com.nc.service.CourseService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    private DesiredScheduleDao desiredScheduleDao;
    @Autowired
    private SuitabilityDao suitabilityDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserGroupDao userGroupDao;

    private CourseStatus statusDao;

    @Override
    public void add(Course course) {
        courseDao.insert(course);
    }

    @Override
    public void update(Course course) {
        courseDao.update(course);
    }

    @Override
    public Course stringToObjCourse(String name, String user, String level,
                                    String courseStatus, String imageUrl, String isOnLandingPage,
                                    String desc, String startDay, String endDay) {
        //int statusId = statusDao.getIdByName(courseStatus.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int userId = 1;
//        CourseStatus status = CourseStatus.valueOf(courseStatus);
        ua.com.nc.domain.CourseStatus status = ua.com.nc.domain.CourseStatus.ENDED;
        boolean isLanding = Boolean.parseBoolean(isOnLandingPage);
        Date startingDay = new Date();
        Date endingDay = startingDay;
        try {
            startingDay = format.parse(startDay);
            endingDay = format.parse(endDay);
        } catch (ParseException e) {
            log.trace(e);
        }
        int statusId = 1;
        int lvl = levelDao.getIdByName(level.trim());

        return new Course(name, lvl, statusId, userId, imageUrl,
                new java.sql.Date(startingDay.getTime()), new java.sql.Date(endingDay.getTime()),
                isLanding, desc);
    }




    /**
     * @return local image link;
     */
    @Override
    public String uploadImage(MultipartFile image) {
        StringBuilder name;
        if (!image.isEmpty() && image.getOriginalFilename() != null) {
            try {
                byte[] bytes = image.getBytes();

                name = new StringBuilder(image.getOriginalFilename());
                int dot = name.lastIndexOf(".");
                String imgFormat = name.substring(dot - 1);
                name = new StringBuilder(name.subSequence(0, dot));

                String rootPath = "src/main/resources/img";
                Path path = Paths.get(rootPath + File.separator + name + imgFormat);
                int i = 1;
                while (Files.exists(path)) {
                    name.append(i);
                    path = Paths.get(rootPath + File.separator + name + imgFormat);
                    i++;
                }
                File uploadedFile = Files.createFile(path).toFile();
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile))) {
                    stream.write(bytes);
                    stream.flush();
                }
                return name.toString();
            } catch (Exception e) {
                log.trace(e);
            }
        }
        return "";
    }

    @Override
    public List<DtoCourse> getAllByTrainerAndEmployee(Integer trainerId, Integer employeeId) {
        List<Course> courses = courseDao.getAllCourseByTrainerAndByEmployee(trainerId, employeeId);
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
