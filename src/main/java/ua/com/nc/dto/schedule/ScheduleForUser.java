package ua.com.nc.dto.schedule;

import lombok.Data;
import ua.com.nc.domain.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleForUser {
    private int userId;
    private String userName;
    private List<ScheduleForInterval> scheduleForIntervals = new ArrayList<>();

    public ScheduleForUser() {
    }

    public ScheduleForUser(User user, List<ParsedSchedule> desiredSchedules, int start, double end) {
        List<ParsedSchedule> parsedSchedules = getForUser(user.getId(), desiredSchedules);
        this.userId = user.getId();
        this.userName = user.getFirstName() + "  " + user.getLastName();
        for (int i = start; i < end; i++) {
            scheduleForIntervals.add(new ScheduleForInterval(i, i + 1, parsedSchedules));
        }
    }

    private List<ParsedSchedule> getForUser(int userId, List<ParsedSchedule> desiredSchedules) {
        List<ParsedSchedule> result = new ArrayList<>();
        for (ParsedSchedule parsedSchedule : desiredSchedules) {
            if (parsedSchedule.getUserId() == userId) {
                result.add(parsedSchedule);
            }
        }
        return result;
    }


}
