package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString
public class Feedback extends Entity<Integer> {
    private Integer studentId;
    private Integer trainerId;
    private Integer courseId;
    private String text;
    private Date timeDate;

    public Feedback(Integer id, Integer studentId, Integer trainerId, Integer courseId, String text, Date timeDate) {
        super(id);
        this.studentId = studentId;
        this.trainerId = trainerId;
        this.courseId = courseId;
        this.text = text;
        this.timeDate = timeDate;
    }
}
