package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class Problem extends Entity {

    private Integer studentId;
    private String description;
    private String message;
    private Integer status;
    private Integer chatId;
    private Timestamp dateTime;

    public Integer getStudentId() { return studentId; }

    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public Problem () {}

    public Problem (Integer studentId, String description, String message, Integer status, Integer chatId) {
        this.studentId = studentId ;
        this.description = description;
        this.message = message;
        this.status = status;
        this.chatId = chatId;
    }

    public Problem (Integer studentId, String description, String message, Integer status) {
        this.studentId = studentId ;
        this.description = description;
        this.message = message;
        this.status = status;
    }

}
