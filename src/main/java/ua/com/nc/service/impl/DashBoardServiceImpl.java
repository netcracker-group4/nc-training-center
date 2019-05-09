package ua.com.nc.service.impl;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.dao.interfaces.IGroupDao;
import ua.com.nc.dao.interfaces.ILevelDao;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Level;
import ua.com.nc.domain.User;
import ua.com.nc.dto.CourseAndGroups;
import ua.com.nc.dto.DTOLevel;
import ua.com.nc.dto.DTOTrainer;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.service.DashBoardService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {
    private static final Logger log = Logger.getLogger(DashBoardServiceImpl.class);
    @Autowired
    private
    ILevelDao iLevelDao;
    @Autowired
    private
    ICourseDao iCourseDao;
    @Autowired
    private
    IUserDao iUserDao;
    @Autowired
    private
    IGroupDao iGroupDao;

    @Override
    public List<DTOLevel> getLevelAndQuantity() {
        List<DTOLevel> dtoLevels = new ArrayList<>();
        List<Level> levelList = iLevelDao.getAll();
        for (Level level : levelList) {
            List<DTOLevel.DTOCourseGroup> dtoCourseGroups = new ArrayList<>();
            List<Course> courses = iCourseDao.getAllByLevel(level.getId());

            for (Course course : courses) {
                List<Group> groups = iGroupDao.getAllGroupsOfCourse(course.getId());
                for (Group group : groups) {
                    dtoCourseGroups.add(new DTOLevel.DTOCourseGroup(course, group));
                }
            }
            dtoLevels.add(new DTOLevel(level, dtoCourseGroups.size(), dtoCourseGroups));
        }
        log.info("response to get level and quantity " + new Gson().toJson(dtoLevels));
        return dtoLevels;
    }

    @Override
    public List<DTOTrainer> getLevelAndTrainers() {
        List<DTOTrainer> dtoTrainers = new ArrayList<>();
        List<User> trainers = iUserDao.getAllTrainers();
        for (User trainer : trainers) {
            List<DTOTrainer.CourseAndLevel> courseAndLevels = new ArrayList<>();
            List<Level> levels = iLevelDao.getAllByTrainer(trainer.getId());
            List<Course> courses = iCourseDao.getAllByTrainer(trainer.getId());
            for (Course course : courses) {
                Level level = findById(levels, course.getLevel());
                courseAndLevels.add(new DTOTrainer.CourseAndLevel(course, level));
            }
            dtoTrainers.add(new DTOTrainer(trainer, courseAndLevels.size(), courseAndLevels));
        }
        log.info("response to get level and trainers " + new Gson().toJson(dtoTrainers));
        return dtoTrainers;
    }

    @Override
    public List<CourseAndGroups> getTrainingAndQuantity() {
        List<Course> courses = iCourseDao.getAll();
        List<CourseAndGroups> courseAndGroups = new ArrayList<>();
        for (Course course : courses) {
            int numberOfEmployeesInCourse = 0;
            List<Group> groups = iGroupDao.getAllGroupsOfCourse(course.getId());
            List<DtoGroup> groupAndQuantities = new ArrayList<>();
            for (Group group : groups) {
                int numberOfEmployeesInGroup = iGroupDao.getNumberOfEmployeesInGroup(group.getId());
                numberOfEmployeesInCourse += numberOfEmployeesInGroup;
                groupAndQuantities.add(new DtoGroup(group.getId(), group.getTitle(),
                        numberOfEmployeesInGroup));
            }
            courseAndGroups.add(new CourseAndGroups(course, numberOfEmployeesInCourse, groupAndQuantities));
        }
        log.info("response to get level and trainers " + new Gson().toJson(courseAndGroups));
        return courseAndGroups;
    }


    private Level findById(List<Level> levels, int id) {
        for (Level level : levels) {
            if (level.getId() == id) {
                return level;
            }
        }
        return null;

    }

}
