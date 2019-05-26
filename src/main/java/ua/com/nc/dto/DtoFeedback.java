package ua.com.nc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class DtoFeedback {
    private Integer id;
    private Integer studentId;
    private DtoTeacherAndManager teacher;
    private DtoCourse course;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private Timestamp time;

    public DtoFeedback() {
    }

    public DtoFeedback(Integer id, Integer studentId, DtoTeacherAndManager teacher, DtoCourse course, String text, Timestamp time) {
        this.id = id;
        this.studentId = studentId;
        this.teacher = teacher;
        this.course = course;
        this.text = text;
        this.time = time;
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

    public DtoCourse getCourse() {
        return course;
    }

    public void setCourse(DtoCourse course) {
        this.course = course;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
