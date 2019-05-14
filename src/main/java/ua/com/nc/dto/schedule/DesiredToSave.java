package ua.com.nc.dto.schedule;

import lombok.Data;

@Data
public class DesiredToSave {
    private Integer courseId;
    private ScheduleForDay[] forDays;

    public DesiredToSave() {
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setForDays(ScheduleForDay[] forDays) {
        this.forDays = forDays;
    }
}
