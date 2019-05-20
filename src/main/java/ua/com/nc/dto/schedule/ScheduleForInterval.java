package ua.com.nc.dto.schedule;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleForInterval {
    private int start;
    private int end;
    String[] colorsForWeekDays = new String[7];

    public ScheduleForInterval() {
    }

    ScheduleForInterval(int start, int end, List<ParsedSchedule> parsedScheduleList) {
        this.start = start;
        this.end = end;
        for (int i = 0; i < 7; i++) {
            ParsedSchedule parsedSchedule = getForDay(parsedScheduleList, i);
            if (parsedSchedule != null) {
                colorsForWeekDays[i] = parsedSchedule.getColor();
            } else colorsForWeekDays[i] = "grey";
        }
    }



    private ParsedSchedule getForDay(List<ParsedSchedule> forThisInterval, int day){
        for (ParsedSchedule parsedSchedule : forThisInterval) {
            if(parsedSchedule.getDayOfWeek().getValue()-1 == day){
                return parsedSchedule;
            }
        }
        return null;
    }
}
