package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Level;
import ua.com.nc.domain.User;

import java.util.List;

@Data
public class DTOTrainer {
    private User trainer;
    private int numberOfCourses;
    private List<CourseAndLevel> courseAndLevels;

    public DTOTrainer(User trainer, int numberOfCourses, List<CourseAndLevel> courseAndLevels) {
        this.trainer = trainer;
        this.numberOfCourses = numberOfCourses;
        this.courseAndLevels = courseAndLevels;
    }


    @Data
    public static class CourseAndLevel {
        private Course course;
        private Level level;

        public CourseAndLevel(Course course, Level level) {
            this.course = course;
            this.level = level;
        }
    }
}
