package ua.com.nc.service;


import ua.com.nc.domain.Group;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.dto.schedule.GroupSchedule;

import java.util.List;

public interface GroupsService {
    int update(GroupSchedule groupSchedule);

    boolean delete(int groupId);

    int add(GroupSchedule groupSchedule);

    Group getGroupById(int groupId);

    List<DtoGroup> getAll();

    List<DtoGroup> getAllByEmployeeId(Integer employeeId);

    List<DtoGroup> getGroupsAndQuantity();

    User getTrainer(int id);

    void invertAttending(Integer userGroupId);
}
