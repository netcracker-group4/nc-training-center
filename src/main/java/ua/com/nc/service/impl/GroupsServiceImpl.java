package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.dao.interfaces.UserGroupDao;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.User;
import ua.com.nc.domain.UserGroup;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.dto.schedule.ScheduleForUser;
import ua.com.nc.service.GroupsService;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class GroupsServiceImpl implements GroupsService {
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserGroupDao userGroupDao;
    @Autowired
    CourseDao courseDao;
    @Autowired
    UserDao userDao;

    @Override
    public int update(GroupSchedule groupSchedule) {
        // TODO what to do with transactions for few daos??
        Group groupToUpdate = groupDao.getEntityById(groupSchedule.getId());
        groupToUpdate.setTitle(groupSchedule.getName());
        groupDao.update(groupToUpdate);
        userGroupDao.deleteAllForGroup(groupToUpdate.getId());
        updateStudentsForGroup(groupSchedule, groupToUpdate);
        return groupToUpdate.getId();
    }

    @Override
    public boolean delete(int groupId) {
        // TODO what to do with transactions for few daos??
        userGroupDao.deleteAllForGroup(groupId);

        groupDao.delete(groupId);
        return true;
    }

    @Override
    public int add(GroupSchedule groupSchedule) {
        // TODO what to do with transactions for few daos??
        Group groupToInsert = new Group(groupSchedule.getCourseId(), groupSchedule.getName());
        groupDao.insert(groupToInsert);
        updateStudentsForGroup(groupSchedule, groupToInsert);
        return groupToInsert.getId();
    }

    //method created to avoid usages of dao in controller
    @Override
    public Group getGroupById(int groupId) {
        return groupDao.getGroupById(groupId);
    }

    @Override
    public List<DtoGroup> getAll() {
        List<Group> groups = groupDao.getAll();
        List<DtoGroup> dtoGroups = new ArrayList<>();
        for (Group group : groups) {
            dtoGroups.add(new DtoGroup(group.getId(), group.getTitle()));
        }
        return dtoGroups;
    }

    private void updateStudentsForGroup(GroupSchedule groupSchedule, Group group) {
        List<ScheduleForUser> newUsers = groupSchedule.getGroupScheduleList();
        for (ScheduleForUser newUser : newUsers) {
            UserGroup oldUserGroupForThisCourse = userGroupDao.getByUserAndCourse(newUser.getUserId(), group.getCourseId());
            if (oldUserGroupForThisCourse == null) {
                //if this user has not been grouped yet for this course
                userGroupDao.insert(new UserGroup(newUser.getUserId(), group.getId(), true));
            } else {
                //if this user already has been saved as a part of another group for this course
                oldUserGroupForThisCourse.setGroupId(group.getId());
                userGroupDao.update(oldUserGroupForThisCourse);
            }
        }
    }


    @Override
    public List<DtoGroup> getGroupsAndQuantity() {
        List<Group> groups = groupDao.getAll();
        List<DtoGroup> dtos = new ArrayList<>();
        groups.forEach(g -> {
            int n = groupDao.getNumberOfEmployeesInGroup(g.getId());
            dtos.add(new DtoGroup(g.getId(), g.getTitle(), n));
        });

        return dtos;
    }

    @Override
    public User getTrainer(int id) {
        return userDao.getTrainerByGroupId(id);
    }
}
