package ua.com.nc.dto.schedule;

import lombok.Data;
import ua.com.nc.domain.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleForUser {
    private int id;
    private int userId;
    private String userName;
    private boolean isAttending;
    private List<ScheduleForInterval> scheduleForIntervals = new ArrayList<>();

    public ScheduleForUser() {
    }

    public ScheduleForUser(int id, User user, boolean isAttending , List<ParsedSchedule> desiredSchedules, int start, double end) {
        this.id = id;
        this.isAttending = isAttending;
        this.userId = user.getId();
        this.userName = user.getFirstName() + "  " + user.getLastName();
        for (int i = start; i < end; i++) {
            List<ParsedSchedule> forInterval = getForInterval(i, desiredSchedules);
            scheduleForIntervals.add(new ScheduleForInterval(i, i + 1, forInterval));
        }
    }

    private List<ParsedSchedule> getForInterval(int start, List<ParsedSchedule> parsedScheduleList) {
        List<ParsedSchedule> result = new ArrayList<>();
        for (ParsedSchedule parsedSchedule : parsedScheduleList) {
            if (parsedSchedule.getStart() == start) {
                result.add(parsedSchedule);
            }
        }
        return result;
    }


}
