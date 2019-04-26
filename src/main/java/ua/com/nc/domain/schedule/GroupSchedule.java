package ua.com.nc.domain.schedule;

import lombok.Data;
import java.util.List;

@Data
public class GroupSchedule {
    private int id;
    private String name;
    private int courseId;
    private List<ScheduleForUser> groupScheduleList;

    public GroupSchedule() {
    }

    public GroupSchedule(int id, String name, List<ScheduleForUser> forUsersInGroup, int courseId) {
        this.id = id;
        this.name = name;
        this.groupScheduleList = forUsersInGroup;
        this.courseId = courseId;
    }
}
