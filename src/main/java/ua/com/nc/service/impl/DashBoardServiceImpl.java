package ua.com.nc.service.impl;

import com.google.gson.Gson;
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
import ua.com.nc.service.DashBoardService;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {
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
    public String getLevelAndQuantity() {
        List<DTOLevel> dtoLevels = new ArrayList<>();
        List<Level> levelList = iLevelDao.getAll();
        for (Level level : levelList) {
            List<DTOCourseGroup> dtoCourseGroups = new ArrayList<>();
            List<Course> courses = iCourseDao.getAllByLevel(level.getId());

            for (Course course : courses) {
                List<Group> groups = iGroupDao.getAllGroupsOfCourse(course.getId());
                for (Group group : groups) {
                    dtoCourseGroups.add(new DTOCourseGroup(course, group));
                }
            }
                dtoLevels.add(new DTOLevel(level, dtoCourseGroups.size(), dtoCourseGroups));
        }
        System.out.println("log response to get level and quantity");
        System.out.println(new Gson().toJson(dtoLevels));
        return new Gson().toJson(dtoLevels);
    }

    @Override
    public String getLevelAndTrainers() {
        List<DTOTrainer> dtoTrainers = new ArrayList<>();
        List<User> trainers = iUserDao.getAllTrainers();
        for (User trainer : trainers) {
            List<CourseAndLevel> courseAndLevels = new ArrayList<>();
            List<Level> levels = iLevelDao.getAllByTrainer(trainer.getId());
            List<Course> courses = iCourseDao.getAllByTrainer(trainer.getId());
            for (Course course : courses) {
                Level level = findById(levels, course.getLevel());
                courseAndLevels.add(new CourseAndLevel(course, level));
            }
            dtoTrainers.add(new DTOTrainer(trainer, courseAndLevels.size(), courseAndLevels));
        }
        System.out.println("log response to get level and trainers");
        System.out.println(new Gson().toJson(dtoTrainers));
        return new Gson().toJson(dtoTrainers);
    }

    @Override
    public String getTrainingAndQuantity() {
        List<Course> courses = iCourseDao.getAll();
        List<CourseAndGroups> courseAndGroups = new ArrayList<>();
        for (Course course : courses) {
            int numberOfEmployeesInCourse = 0;
            List<Group> groups = iGroupDao.getAllGroupsOfCourse(course.getId());
            List<GroupAndQuantity> groupAndQuantities = new ArrayList<>();
            for (Group group : groups) {
                int numberOfEmployeesInGroup = iGroupDao.getNumberOfEmployeesInGroup(group.getId());
                numberOfEmployeesInCourse += numberOfEmployeesInGroup;
                groupAndQuantities.add(new GroupAndQuantity(group, numberOfEmployeesInGroup));
            }
            courseAndGroups.add(new CourseAndGroups(course, numberOfEmployeesInCourse, groupAndQuantities));
        }
        System.out.println("log response to get level and trainers");
        System.out.println(new Gson().toJson(courseAndGroups));
        return new Gson().toJson(courseAndGroups);
    }


    private Level findById(List<Level> levels, int id){
        for (Level level : levels) {
            if (level.getId() == id){
                return level;
            }
        }
        return null;

    }

    private class DTOLevel{
        Level level;
        int numberOfGroups;
        List<DTOCourseGroup> groups;

        DTOLevel(Level level, int numberOfGroups, List<DTOCourseGroup> groups) {
            this.level = level;
            this.numberOfGroups = numberOfGroups;
            this.groups = groups;
        }
    }

    private class DTOCourseGroup{
        Course course;
        Group group;

        DTOCourseGroup(Course course, Group group) {
            this.course = course;
            this.group = group;
        }
    }

    private class DTOTrainer{
        private User trainer;
        private int numberOfCourses;
        private List<CourseAndLevel> courseAndLevels;

        DTOTrainer(User trainer, int numberOfCourses, List<CourseAndLevel> courseAndLevels) {
            this.trainer = trainer;
            this.numberOfCourses = numberOfCourses;
            this.courseAndLevels = courseAndLevels;
        }

        public User getTrainer() {
            return trainer;
        }

        public int getNumberOfCourses() {
            return numberOfCourses;
        }

        public List<CourseAndLevel> getCourseAndLevels() {
            return courseAndLevels;
        }
    }

    private class CourseAndLevel{
        private Course course;
        private Level level;

        CourseAndLevel(Course course, Level level) {
            this.course = course;
            this.level = level;
        }

        public Course getCourse() {
            return course;
        }

        public Level getLevel() {
            return level;
        }
    }

    private class GroupAndQuantity{
        private Group group;
        private int quantityOfEmployees;

        GroupAndQuantity(Group group, int quantityOfEmployees) {
            this.group = group;
            this.quantityOfEmployees = quantityOfEmployees;
        }

        public Group getGroup() {
            return group;
        }

        public int getQuantityOfEmployees() {
            return quantityOfEmployees;
        }
    }

    private class CourseAndGroups{
        Course course;
        int numberOfEmployees;
        List<GroupAndQuantity> groups;

        CourseAndGroups(Course course, int numberOfEmployees, List<GroupAndQuantity> groups) {
            this.course = course;
            this.numberOfEmployees = numberOfEmployees;
            this.groups = groups;
        }

        public Course getCourse() {
            return course;
        }

        public int getNumberOfEmployees() {
            return numberOfEmployees;
        }

        public List<GroupAndQuantity> getGroups() {
            return groups;
        }
    }
}
