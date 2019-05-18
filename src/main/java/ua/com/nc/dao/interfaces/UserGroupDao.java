package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.UserGroup;

import java.util.List;

public interface UserGroupDao extends GenericDao<UserGroup> {

    void deleteAllForGroup(Integer groupId);

    void deleteAllForUser(Integer userId);

    UserGroup getByUserAndCourse(Integer userId, Integer courseId);

    List<UserGroup> getByUser(Integer userId);

    UserGroup getByUserAndGroup(Integer userId, Integer groupId);
}
