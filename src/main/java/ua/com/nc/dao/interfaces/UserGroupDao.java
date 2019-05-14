package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.UserGroup;

public interface UserGroupDao extends GenericDao<UserGroup> {

    void deleteAllForGroup(Integer groupId);

    void deleteAllForUser(Integer userId);

    UserGroup getByUserAndCourse(Integer userId, Integer courseId);

    UserGroup getByUserAndGroup(Integer userId, Integer groupId);
}
