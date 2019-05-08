package ua.com.nc.dto;

import java.sql.Date;

public class DtoFeedback {
    private Integer id;
    private DtoTeacherAndManager teacher;
    private String text;
    private Date timeDate;

    public DtoFeedback() {
    }

    public DtoFeedback(Integer id, DtoTeacherAndManager teacher, String text, Date timeDate) {
        this.id = id;
        this.teacher = teacher;
        this.text = text;
        this.timeDate = timeDate;
    }

    public Integer getId() {
        return id;
    }

    public DtoTeacherAndManager getTeacher() {
        return teacher;
    }

    public String getText() {
        return text;
    }

    public Date getTimeDate() {
        return timeDate;
    }
}
