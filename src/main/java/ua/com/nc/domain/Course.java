package ua.com.nc.domain;

import lombok.Data;

import java.sql.Date;
import java.util.Objects;

@Data
public class Course extends Entity<Integer>  {
    private String name;
    private int level;
    private int courseStatusId;
    private int userId;
    private String imageUrl;
    private Date startDate;
    private Date endDate;
    private Boolean isOnLandingPage;
    private String description;

    public Course(String name, int level, int courseStatusId, int userId, String imageUrl, Date startDate, Date endDate, Boolean isOnLandingPage, String description) {
        this.name = name;
        this.level = level;
        this.courseStatusId = courseStatusId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOnLandingPage = isOnLandingPage;
        this.description = description;
    }


    public Course(Integer id, String name, int level, int courseStatusId, int userId, String imageUrl, Date startDate, Date endDate, Boolean isOnLandingPage, String description) {
        super(id);
        this.name = name;
        this.level = level;
        this.courseStatusId = courseStatusId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOnLandingPage = isOnLandingPage;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        if (!super.equals(o)) return false;
        Course course = (Course) o;
        return level == course.level &&
                courseStatusId == course.courseStatusId &&
                userId == course.userId &&
                Objects.equals(name, course.name) &&
                Objects.equals(imageUrl, course.imageUrl) &&
                Objects.equals(startDate.toString(), course.startDate.toString()) &&
                Objects.equals(endDate.toString(), course.endDate.toString()) &&
                Objects.equals(isOnLandingPage, course.isOnLandingPage) &&
                Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, level, courseStatusId, userId, imageUrl, startDate, endDate, isOnLandingPage, description);
    }
}
