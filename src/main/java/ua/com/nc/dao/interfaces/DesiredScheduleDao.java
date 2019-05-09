package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.DesiredSchedule;

import java.util.List;

public interface DesiredScheduleDao extends GenericDao<DesiredSchedule, Integer> {
    List<DesiredSchedule> getAllForCourse(int courseId);

    List<DesiredSchedule> getAllForGroup(int groupId);
}
