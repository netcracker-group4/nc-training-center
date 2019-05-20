package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.dao.interfaces.CourseStatusDao;
import ua.com.nc.domain.*;
import ua.com.nc.dto.DtoCourse;
import ua.com.nc.dto.schedule.*;
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

@Log4j
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

    private int startOfDay = 8;
    private int endOfDay = 21;


    private CourseStatusDao statusDao;

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
        courseDao.edit(id,name,lvl, statusId, isLanding,new java.sql.Date(starts.getTime()),new java.sql.Date(ends.getTime()),desc);
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


    @Override
    public List<ScheduleForUser> getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception {
//        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getUngroupedForCourse(courseId);
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        for (User user : userDao.getUngroupedByCourse(courseId)) {
            UserGroup byUserAndCourse = userGroupDao.getByUserAndCourse(user.getId(), courseId);
            List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getByUsrGroupId(byUserAndCourse.getId());
            scheduleForUsers.add(new ScheduleForUser(byUserAndCourse.getId(), user,
                    byUserAndCourse.isAttending(),
                    parseSchedules(desiredScheduleList, suitabilityDao.getAll()),
                    startOfDay, endOfDay));
        }
        return scheduleForUsers;
    }

    @Override
    public List<GroupSchedule> getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception {
        List<GroupSchedule> scheduleForGroupsForCourse = new ArrayList<>();
        List<Suitability> all = suitabilityDao.getAll();
        List<Group> allGroupsForCourse = groupDao.getAllGroupsOfCourse(courseId);
        for (Group group : allGroupsForCourse) {
            List<ScheduleForUser> scheduleForUsersInGroup = new ArrayList<>();
            for (User user : userDao.getByGroupId(group.getId())) {
                UserGroup byUserAndCourse = userGroupDao.getByUserAndCourse(user.getId(), courseId);
                List<ParsedSchedule> desiredScheduleList = parseSchedules(
                        desiredScheduleDao.getByUsrGroupId(byUserAndCourse.getId()), all);
                scheduleForUsersInGroup.add(new ScheduleForUser(byUserAndCourse.getId(), user,
                        byUserAndCourse.isAttending(),
                        desiredScheduleList, startOfDay, endOfDay));
            }
            scheduleForGroupsForCourse.add(new GroupSchedule(group.getId(), group.getTitle(),
                    scheduleForUsersInGroup, courseId));
        }
        return scheduleForGroupsForCourse;
    }


    private List<ParsedSchedule> parseSchedules(List<DesiredSchedule> desiredSchedules, List<Suitability> suitabilities)
            throws Exception {
        List<ParsedSchedule> result = new ArrayList<>();
        for (DesiredSchedule desiredSchedule : desiredSchedules) {
            result.add(new ParsedSchedule(desiredSchedule, suitabilities));
        }
        return result;
    }

    @Override
    public List<String> getDayIntervals() {
        List<String> dayIntervals = new ArrayList<>();
        for (int i = startOfDay; i < endOfDay; i++) {
            int halfDay = 12;
            int hours = (i % halfDay == 0) ? 12 : i % halfDay;
            String appendix = (i < 12) ? "am" : "pm";
            dayIntervals.add(hours + appendix);
        }
        return dayIntervals;
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
    public List<ScheduleForUser> getDesiredScheduleForGroup(int groupId) throws Exception {
        List<DesiredSchedule> desiredScheduleList = desiredScheduleDao.getAllForGroup(groupId);
        List<ScheduleForUser> scheduleForUsers = new ArrayList<>();
        List<Suitability> all = suitabilityDao.getAll();
        for (User user : userDao.getByGroupId(groupId)) {
            UserGroup byUserAndGroup = userGroupDao.getByUserAndGroup(user.getId(), groupId);
            scheduleForUsers.add(new ScheduleForUser(byUserAndGroup.getId(), user,
                    byUserAndGroup.isAttending(),
                    parseSchedules(desiredScheduleList, all),
                    startOfDay, endOfDay));
        }
        return scheduleForUsers;
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

    @Override
    public String saveDesired(Integer id, DesiredToSave desiredToSave) {
        Integer courseId = desiredToSave.getCourseId();
        UserGroup userGroup = userGroupDao.getByUserAndCourse(id, courseId);
        if (userGroup == null) {
            userGroup = new UserGroup(id, null, courseId, true);
            userGroupDao.insert(userGroup);
        }
        for (ScheduleForDay scheduleForDay : desiredToSave.getForDays()) {
            saveForDay(id, courseId, scheduleForDay);
        }
        return "saved";
    }

    private void saveForDay(Integer id, Integer courseId, ScheduleForDay scheduleForDay) {
        for (int i = startOfDay; i < endOfDay ; i++) {
            if (scheduleForDay.getArray()[i - startOfDay] != -1) {
                String cronInterval = "0 " + i  + " 0 " + (i + 1) + " " + scheduleForDay.getDay();
                DesiredSchedule desiredSchedule = new DesiredSchedule(id, courseId,
                        cronInterval, scheduleForDay.getArray()[i - startOfDay]);
                log.info("before saving");
                log.info(desiredSchedule);
                desiredScheduleDao.insert(desiredSchedule);
            }
        }
    }
}
