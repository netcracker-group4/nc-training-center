package ua.com.nc.dto;


import lombok.Data;
import ua.com.nc.domain.Course;

import java.util.List;

@Data
public class CourseAndGroups {
    Course course;
    int numberOfEmployees;
    List<DtoGroup> groups;

    public CourseAndGroups(Course course, int numberOfEmployees, List<DtoGroup> groups) {
        this.course = course;
        this.numberOfEmployees = numberOfEmployees;
        this.groups = groups;
    }
}