package ua.com.nc.service;


import ua.com.nc.domain.schedule.GroupSchedule;
import ua.com.nc.dto.DtoGroup;

import java.util.List;

public interface GroupsService {
    int update(GroupSchedule groupSchedule);
    boolean delete(int groupId);
    int add(GroupSchedule groupSchedule);
    List<DtoGroup> getAll();
}
