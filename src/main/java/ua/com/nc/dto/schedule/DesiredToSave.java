package ua.com.nc.dto.schedule;

import lombok.Data;

@Data
public class DesiredToSave {
    private Integer courseId;
    private ScheduleForDay[] forDays;
}
