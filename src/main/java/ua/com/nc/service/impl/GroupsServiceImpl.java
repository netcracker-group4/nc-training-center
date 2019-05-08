package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.dao.interfaces.IGroupDao;
import ua.com.nc.dao.interfaces.IUserGroupDao;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.UserGroup;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.dto.schedule.ScheduleForUser;
import ua.com.nc.service.GroupsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupsServiceImpl implements GroupsService {
    @Autowired
    IGroupDao iGroupDao;
    @Autowired
    IUserGroupDao iUserGroupDao;
    @Autowired
    ICourseDao iCourseDao;

    @Override
    synchronized public int update(GroupSchedule groupSchedule) {
        // TODO what to do with transactions for few daos??
        Group groupToUpdate = iGroupDao.getEntityById(groupSchedule.getId());
        groupToUpdate.setTitle(groupSchedule.getName());
        iGroupDao.update(groupToUpdate);
        iGroupDao.commit();
        iUserGroupDao.deleteAllForGroup(groupToUpdate.getId());
        updateStudentsForGroup(groupSchedule, groupToUpdate);
        iUserGroupDao.commit();
        return groupToUpdate.getId();
    }

    @Override
    synchronized public boolean delete(int groupId) {
        // TODO what to do with transactions for few daos??
        iUserGroupDao.deleteAllForGroup(groupId);
        iUserGroupDao.commit();

        iGroupDao.delete(groupId);
        iGroupDao.commit();
        return true;
    }

    @Override
    synchronized public int add(GroupSchedule groupSchedule) {
        // TODO what to do with transactions for few daos??
        Group groupToInsert = new Group(groupSchedule.getCourseId(), groupSchedule.getName());
        iGroupDao.insert(groupToInsert);
        iGroupDao.commit();
        updateStudentsForGroup(groupSchedule, groupToInsert);
        iUserGroupDao.commit();
        return groupToInsert.getId();
    }

    @Override
    public List<DtoGroup> getAll() {
        List<Group> groups = iGroupDao.getAll();
        List<DtoGroup> dtoGroups = new ArrayList<>();
        for (Group group : groups) {
            dtoGroups.add(new DtoGroup(group.getId(), group.getTitle()));
        }
        return dtoGroups;
    }

    synchronized private void updateStudentsForGroup(GroupSchedule groupSchedule, Group group) {
        List<ScheduleForUser> newUsers = groupSchedule.getGroupScheduleList();
        for (ScheduleForUser newUser : newUsers) {
            UserGroup oldUserGroupForThisCourse = iUserGroupDao.getByUserAndCourse(newUser.getUserId(), group.getCourseId());
            if (oldUserGroupForThisCourse == null) {
                //if this user has not been grouped yet for this course
                iUserGroupDao.insert(new UserGroup(newUser.getUserId(), group.getId(), true));
            } else {
                //if this user already has been saved as a part of another group for this course
                oldUserGroupForThisCourse.setGroupId(group.getId());
                iUserGroupDao.update(oldUserGroupForThisCourse);
            }
        }
    }
}
