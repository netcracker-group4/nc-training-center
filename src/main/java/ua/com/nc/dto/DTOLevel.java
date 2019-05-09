package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Level;

import java.util.List;

@Data
public class DTOLevel {
    Level level;
    int numberOfGroups;
    List<DTOCourseGroup> groups;

    public DTOLevel(Level level, int numberOfGroups, List<DTOCourseGroup> groups) {
        this.level = level;
        this.numberOfGroups = numberOfGroups;
        this.groups = groups;
    }

    @Data
    public static class DTOCourseGroup {
        Course course;
        Group group;

        public DTOCourseGroup(Course course, Group group) {
            this.course = course;
            this.group = group;
        }
    }
}

