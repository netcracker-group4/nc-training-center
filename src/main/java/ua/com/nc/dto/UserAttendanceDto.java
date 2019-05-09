package ua.com.nc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.com.nc.domain.Attendance;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAttendanceDto extends Attendance {
    String studentFirstName;
    String studentLastName;
    String courseName;
    Integer courseId;
    Date timeDate;
    String topic;

    public UserAttendanceDto(Integer id, Integer lessonId, Integer userId, String reason, String status,
                             String studentFirstName, String studentLastName, Integer courseId, String courseName, Date timeDate,
                             String topic) {
        super(id, lessonId, userId, reason, status);
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.timeDate = timeDate;
        this.topic = topic;
    }
}
