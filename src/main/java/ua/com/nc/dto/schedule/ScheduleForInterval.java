package ua.com.nc.dto.schedule;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleForInterval {
    private int start;
    private int end;
    String[] colorsForDays = new String[7];

    public ScheduleForInterval() {
    }

    public ScheduleForInterval(int start, int end, List<ParsedSchedule> parsedScheduleList) {
        List<ParsedSchedule> forThisInterval = getForInterval(start, parsedScheduleList);
        this.start = start;
        this.end = end;
        int daysIterator = 0;
        for (int i = 0; i < 7; i++) {
            if (daysIterator < forThisInterval.size() && forThisInterval.get(daysIterator).getDayOfWeek().getValue() == i + 1) {
                colorsForDays[i] = forThisInterval.get(daysIterator).getColor();
                daysIterator++;
            } else colorsForDays[i] = "grey";
        }
    }

    private List<ParsedSchedule> getForInterval(int start, List<ParsedSchedule> parsedScheduleList) {
        List<ParsedSchedule> result = new ArrayList<>();
        for (ParsedSchedule parsedSchedule : parsedScheduleList) {
            if (parsedSchedule.getStart() == start) {
                result.add(parsedSchedule);
            }
        }
        result.sort((a, b) -> b.getDayOfWeek().getValue() - a.getDayOfWeek().getValue());
        return result;
    }
}
