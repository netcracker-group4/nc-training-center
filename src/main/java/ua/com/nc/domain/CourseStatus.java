package ua.com.nc.domain;

import lombok.Data;

@Data
public class CourseStatus extends Entity<Integer>{
    private String title, description;

    public CourseStatus(Integer id, String title, String description) {
        super(id);
        this.title = title;
        this.description = description;
    }

    public CourseStatus(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
