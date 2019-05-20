package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Group extends Entity {
    private int courseId;
    private String title;

    public Group() {
    }

    public Group(int id, int courseId, String title) {
        super(id);
        this.courseId = courseId;
        this.title = title;
    }

    public Group(int courseId, String title) {
        this.courseId = courseId;
        this.title = title;
    }

}
