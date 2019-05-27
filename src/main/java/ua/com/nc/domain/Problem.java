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

    public Problem (Integer id, Integer studentId, String description, String message, Integer status, Integer chatId) {
        super(id);
        this.studentId = studentId;
        this.description = description;
        this.message = message;
        this.status = status;
        this.chatId = chatId;
    }

    public Problem (Integer studentId, String description, String message, Integer status, Integer chatId) {
        this.studentId = studentId ;
        this.description = description;
        this.message = message;
        this.status = status;
        this.chatId = chatId;
    }

}
