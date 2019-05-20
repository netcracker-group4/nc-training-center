package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Course extends Entity {
    private String name;
    private int level;
    private int courseStatusId;
    private int userId;
    private String imageUrl;
    private Date startDate;
    private Date endDate;
    private Boolean isOnLandingPage;
    private String description;

    public Course(String name, int level, int courseStatusId, int userId,
                  String imageUrl, Date startDate, Date endDate, Boolean isOnLandingPage, String description) {
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


    public Course(Integer id, String name, int level, int courseStatusId,
                  int userId, String imageUrl, Date startDate, Date endDate,
                  Boolean isOnLandingPage, String description) {
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

}
