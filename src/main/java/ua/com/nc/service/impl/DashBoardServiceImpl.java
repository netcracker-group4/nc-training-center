package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.dao.interfaces.LevelDao;
import ua.com.nc.dao.interfaces.UserDao;
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

@Log4j
@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    private
    LevelDao levelDao;
    @Autowired
    private
    CourseDao courseDao;
    @Autowired
    private
    UserDao userDao;
    @Autowired
    private
    GroupDao groupDao;

    @Override
    public List<DTOLevel> getLevelAndQuantity() {
        List<DTOLevel> dtoLevels = new ArrayList<>();
        List<Level> levelList = levelDao.getAll();
        for (Level level : levelList) {
            List<DTOLevel.DTOCourseGroup> dtoCourseGroups = new ArrayList<>();
            List<Course> courses = courseDao.getAllByLevel(level.getId());

            for (Course course : courses) {
                List<Group> groups = groupDao.getAllGroupsOfCourse(course.getId());
                for (Group group : groups) {
                    dtoCourseGroups.add(new DTOLevel.DTOCourseGroup(course, group));
                }
            }
            dtoLevels.add(new DTOLevel(level, dtoCourseGroups.size(), dtoCourseGroups));
        }
        return dtoLevels;
    }

    @Override
    public List<DTOTrainer> getLevelAndTrainers() {
        List<DTOTrainer> dtoTrainers = new ArrayList<>();
        List<User> trainers = userDao.getAllTrainers();
        for (User trainer : trainers) {
            List<DTOTrainer.CourseAndLevel> courseAndLevels = new ArrayList<>();
            List<Level> levels = levelDao.getAllByTrainer(trainer.getId());
            List<Course> courses = courseDao.getAllByTrainer(trainer.getId());
            for (Course course : courses) {
                Level level = findById(levels, course.getLevel());
                courseAndLevels.add(new DTOTrainer.CourseAndLevel(course, level));
            }
            dtoTrainers.add(new DTOTrainer(trainer, courseAndLevels.size(), courseAndLevels));
        }
        return dtoTrainers;
    }

    @Override
    public List<CourseAndGroups> getTrainingAndQuantity() {
        List<Course> courses = courseDao.getAll();
        List<CourseAndGroups> courseAndGroups = new ArrayList<>();
        for (Course course : courses) {
            int numberOfEmployeesInCourse = 0;
            List<Group> groups = groupDao.getAllGroupsOfCourse(course.getId());
            List<DtoGroup> groupAndQuantities = new ArrayList<>();
            for (Group group : groups) {
                int numberOfEmployeesInGroup = groupDao.getNumberOfEmployeesInGroup(group.getId());
                numberOfEmployeesInCourse += numberOfEmployeesInGroup;
                groupAndQuantities.add(new DtoGroup(group.getId(), group.getTitle(),
                        numberOfEmployeesInGroup));
            }
            courseAndGroups.add(new CourseAndGroups(course, numberOfEmployeesInCourse, groupAndQuantities));
        }
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
