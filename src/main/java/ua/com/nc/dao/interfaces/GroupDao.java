package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Group;
import ua.com.nc.domain.User;

import java.util.List;

public interface GroupDao extends GenericDao<Group> {

    List<Group> getAllGroupsOfCourse(int courseId);

    int getNumberOfEmployeesInGroup(int groupId);

    List<Group> getAllGroupsByStudent(int studentId);

    Group getGroupById(Integer id);

    List<Group> getGroupByTrainerId(Integer id);

    void deleteUserFromGroup(String id, String userId);
}
