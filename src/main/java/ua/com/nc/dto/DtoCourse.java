package ua.com.nc.dto;

import java.sql.Date;

public class DtoCourse {
    private Integer id;
    private String name;
    private int level;
    private int courseStatusId;
    private int userId;
    private String imageUrl;
    private Date startDate;
    private Date endDate;
    private Boolean isOnLandingPage;
    private String description;

    public DtoCourse() {
    }

    public DtoCourse(Integer id, String name, int level, int courseStatusId, int userId, String imageUrl, Date startDate, Date endDate, Boolean isOnLandingPage, String description) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCourseStatusId() {
        return courseStatusId;
    }

    public void setCourseStatusId(int courseStatusId) {
        this.courseStatusId = courseStatusId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getOnLandingPage() {
        return isOnLandingPage;
    }

    public void setOnLandingPage(Boolean onLandingPage) {
        isOnLandingPage = onLandingPage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
