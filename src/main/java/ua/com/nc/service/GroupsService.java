package ua.com.nc.service;


import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoGroup;

import java.util.List;
import java.util.Map;

public interface GroupsService {

    DtoGroup getGroupById(int groupId);

    List<DtoGroup> getAll();

    List<DtoGroup> getAllByEmployeeId(Integer employeeId);

    List<DtoGroup> getGroupsAndQuantity();

    User getTrainer(int id);

    List<DtoGroup> getAllByTrainerId(Integer employeeId);

    Map<String, Double> getAttendanceGraph(Integer groupId);
}
