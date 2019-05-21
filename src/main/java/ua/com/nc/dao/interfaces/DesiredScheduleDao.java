package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.DesiredSchedule;

import java.util.List;

public interface DesiredScheduleDao extends GenericDao<DesiredSchedule> {

    List<DesiredSchedule> getByUsrGroupId(Integer id);
}
