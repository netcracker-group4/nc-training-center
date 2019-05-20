package ua.com.nc.service;


import ua.com.nc.domain.Group;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoGroup;

import java.util.List;
import java.util.Map;

public interface GroupsService {

    DtoGroup getGroupById(int groupId);

    List<DtoGroup> getAllGroups();

    List<DtoGroup> getGroupsOfEmployee(Integer employeeId);

    List<DtoGroup> getGroupsAndQuantity();

    User getTrainer(int id);

    List<DtoGroup> getAllByTrainerId(Integer employeeId);

    Map<String, Double> getAttendanceGraph(Integer groupId);

    List<Group> getAllGroupsOfCourse(Integer courseId);

    void removeStudentFromGroup(Integer userId, Integer groupId);
}
