package ua.com.nc.dto.schedule;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Data
public class ScheduleForInterval {
    private int start;
    private int end;
    String[] colorsForDays = new String[7];

    public ScheduleForInterval() {
    }

    ScheduleForInterval(int start, int end, List<ParsedSchedule> parsedScheduleList) {
        List<ParsedSchedule> forThisInterval = getForInterval(start, parsedScheduleList);
        System.out.println("forThisInterval  "+forThisInterval)
        ;this.start = start;
        this.end = end;
        for (int i = 0; i < 7; i++) {
            ParsedSchedule parsedSchedule = getForDay(forThisInterval, i);
            if (parsedSchedule != null) {
                colorsForDays[i] = parsedSchedule.getColor();
            } else colorsForDays[i] = "grey";
        }
        System.out.println(Arrays.toString(colorsForDays));
    }

    private List<ParsedSchedule> getForInterval(int start, List<ParsedSchedule> parsedScheduleList) {
        List<ParsedSchedule> result = new ArrayList<>();
        for (ParsedSchedule parsedSchedule : parsedScheduleList) {
            if (parsedSchedule.getStart() == start) {
                result.add(parsedSchedule);
            }
        }
        result.sort(Comparator.comparingInt(a -> a.getDayOfWeek().getValue()));
        System.out.println("for interval " + start + " " + result);
        return result;
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
