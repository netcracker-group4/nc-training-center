package ua.com.nc.dto.schedule;

import lombok.Data;

@Data
public class ScheduleForDay {
    private Integer day;
    private Integer[] array;

    public ScheduleForDay() {
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setArray(Integer[] array) {
        this.array = array;
    }
}
