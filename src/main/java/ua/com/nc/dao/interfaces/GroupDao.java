package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Group;

import java.util.List;

public interface GroupDao extends GenericDao<Group> {

    List<Group> getAllGroupsOfCourse(Integer courseId);

    int getNumberOfEmployeesInGroup(Integer groupId);

    List<Group> getAllGroupsByStudent(Integer studentId);

    List<Group> getGroupByTrainerId(Integer id);

    Group getByUserIdAndGroupId(Integer userId, Integer groupId);
}
