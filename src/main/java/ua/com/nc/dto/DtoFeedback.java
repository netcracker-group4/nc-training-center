package ua.com.nc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public class DtoFeedback {
    private Integer id;
    private Integer studentId;
    private DtoTeacherAndManager teacher;
    private DtoCourse dtoCourse;
    private String text;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="GMT")
    private OffsetDateTime timeDate;

    public DtoFeedback() {
    }

    public DtoFeedback(Integer id, Integer studentId, DtoTeacherAndManager teacher, DtoCourse dtoCourse, String text, OffsetDateTime timeDate) {
        this.id = id;
        this.studentId = studentId;
        this.teacher = teacher;
        this.dtoCourse = dtoCourse;
        this.text = text;
        this.timeDate = timeDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public DtoTeacherAndManager getTeacher() {
        return teacher;
    }

    public void setTeacher(DtoTeacherAndManager teacher) {
        this.teacher = teacher;
    }

    public DtoCourse getDtoCourse() {
        return dtoCourse;
    }

    public void setDtoCourse(DtoCourse dtoCourse) {
        this.dtoCourse = dtoCourse;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OffsetDateTime getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(OffsetDateTime timeDate) {
        this.timeDate = timeDate;
    }
}
