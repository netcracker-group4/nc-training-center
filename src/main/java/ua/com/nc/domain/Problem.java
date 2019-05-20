package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Problem extends Entity {

    private Integer studentId;
    private String description;
    private String message;
    private String status;
    public Problem () {}

    public Problem (Integer id, Integer studentId, String description, String message, String status) {
        super(id);
        this.studentId = studentId ;
        this.description = description;
        this.message = message;
        this.status = status;
    }

}
