package ua.com.nc.service;

import ua.com.nc.dto.schedule.DesiredToSave;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.dto.schedule.ScheduleForUser;

import java.util.List;

public interface DesiredScheduleService {

    int update(GroupSchedule groupSchedule);

    boolean delete(int groupId);

    int add(GroupSchedule groupSchedule);

    void invertAttending(Integer userGroupId);

    List<ScheduleForUser> getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception;

    List<GroupSchedule> getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception;

    List<String> getDayIntervals();

    List<ScheduleForUser> getDesiredScheduleForGroup(int groupId) throws Exception;

    String saveDesired(Integer id, DesiredToSave desiredToSave);
}
