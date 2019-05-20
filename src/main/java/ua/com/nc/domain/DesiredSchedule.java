package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DesiredSchedule extends Entity {
    private int userId;
    private int courseId;
    // m1 h1 m2 h2 day in [0,1,2,3,4,5,6]
    private String cronInterval;
    private int suitability;

    public DesiredSchedule(int userId, int courseId, String cronInterval, int suitability) {
        this.userId = userId;
        this.courseId = courseId;
        this.cronInterval = cronInterval;
        this.suitability = suitability;
    }

    public DesiredSchedule(Integer id, int userId, int courseId, String cronInterval, int suitability) {
        super(id);
        this.userId = userId;
        this.courseId = courseId;
        this.cronInterval = cronInterval;
        this.suitability = suitability;
    }

}
