package ua.com.nc.service;


import ua.com.nc.domain.schedule.GroupSchedule;

public interface GroupsService {
    int update(GroupSchedule groupSchedule);
    boolean delete(int groupId);
    int add(GroupSchedule groupSchedule);
}
