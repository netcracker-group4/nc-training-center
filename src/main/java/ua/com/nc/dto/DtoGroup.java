package ua.com.nc.dto;

import lombok.Getter;
import ua.com.nc.domain.Group;

@Getter
public class DtoGroup {
    private Integer id;
    private Integer courseId;
    private String title;
    private String courseName;
    private int trainerId;
    private String level;
    private int numberOfEmployees;


    public DtoGroup() {
    }

    public DtoGroup(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public DtoGroup(Integer id, String title, int courseId, String courseName, int trainerId, String level) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.courseName = courseName;
        this.trainerId = trainerId;
        this.level = level;
    }

    public DtoGroup(Integer id, String title, int numberOfEmployees) {
        this.id = id;
        this.title = title;
        this.numberOfEmployees = numberOfEmployees;
    }

}
