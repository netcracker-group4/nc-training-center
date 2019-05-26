package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString
public class Feedback extends Entity {
    private Integer studentId;
    private Integer trainerId;
    private Integer courseId;
    private String text;
    private Timestamp time;

    public Feedback() {
    }

    public Feedback(Integer id, Integer studentId, Integer trainerId, Integer courseId, String text, Timestamp time) {
        super(id);
        this.studentId = studentId;
        this.trainerId = trainerId;
        this.courseId = courseId;
        this.text = text;
        this.time = time;
    }
}
