package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;

    @Override
    public List<DTOLevel> getLevelAndQuantity() {
        List<DTOLevel> dtoLevels = new ArrayList<>();
        List<Level> levelList = levelDao.getAll();
        for (Level level : levelList) {
            dtoLevels.add(getDtoLevel(level));
        }
        return dtoLevels;
    }

    private DTOLevel getDtoLevel(Level level) {
        List<DTOLevel.DTOCourseGroup> dtoCourseGroups = new ArrayList<>();
        List<Course> courses = courseDao.getAllByLevel(level.getId());
        for (Course course : courses) {
            dtoCourseGroups.addAll(getAllGroupsOfCourse(course));
        }
        return new DTOLevel(level, dtoCourseGroups.size(), dtoCourseGroups);
    }

    private List<DTOLevel.DTOCourseGroup> getAllGroupsOfCourse(Course course) {
        List<DTOLevel.DTOCourseGroup> dtoGroupsForCourse = new ArrayList<>();
        List<Group> groups = groupDao.getAllGroupsOfCourse(course.getId());
        groups.forEach(g -> dtoGroupsForCourse.add(new DTOLevel.DTOCourseGroup(course, g)));
        return dtoGroupsForCourse;
    }


    @Override
    public List<DTOTrainer> getLevelAndTrainers() {
        List<DTOTrainer> dtoTrainers = new ArrayList<>();
        List<User> trainers = userDao.getAllTrainers();
        for (User trainer : trainers) {
            DTOTrainer dtoTrainer = getDtoTrainer(trainer);
            dtoTrainers.add(dtoTrainer);
        }
        return dtoTrainers;
    }

    private DTOTrainer getDtoTrainer(User trainer) {
        List<Course> courses = courseDao.getAllByTrainer(trainer.getId());
        List<DTOTrainer.CourseAndLevel> courseAndLevels = new ArrayList<>();
        List<Level> levels = levelDao.getAllByTrainer(trainer.getId());
        for (Course course : courses) {
            Level level = findById(levels, course.getLevel());
            DTOTrainer.CourseAndLevel courseAndLevel = new DTOTrainer.CourseAndLevel(course, level);
            courseAndLevels.add(courseAndLevel);
        }
        return new DTOTrainer(trainer, courseAndLevels.size(), courseAndLevels);
    }


    @Override
    public List<CourseAndGroups> getTrainingAndQuantity() {
        List<Course> courses = courseDao.getAll();
        List<CourseAndGroups> courseAndGroups = new ArrayList<>();
        for (Course course : courses) {
            CourseAndGroups courseAndGroups1 = getCourseAndGroups(course);
            courseAndGroups.add(courseAndGroups1);
        }
        return courseAndGroups;
    }

    private CourseAndGroups getCourseAndGroups(Course course) {
        int numberOfEmployeesInCourse = userDao.getUngroupedByCourse(course.getId()).size();
        List<Group> groups = groupDao.getAllGroupsOfCourse(course.getId());
        List<DtoGroup> groupAndQuantities = new ArrayList<>();
        for (Group group : groups) {
            int numberOfEmployeesInGroup = groupDao.getNumberOfEmployeesInGroup(group.getId());
            numberOfEmployeesInCourse += numberOfEmployeesInGroup;
            groupAndQuantities.add(
                    new DtoGroup(group.getId(), group.getTitle(), numberOfEmployeesInGroup));
        }
        return new CourseAndGroups(course, numberOfEmployeesInCourse, groupAndQuantities);
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
