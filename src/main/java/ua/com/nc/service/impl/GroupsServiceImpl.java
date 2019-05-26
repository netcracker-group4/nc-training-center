package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.GroupsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class GroupsServiceImpl implements GroupsService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserGroupDao userGroupDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private AttendanceStatusDao statusDao;
    @Autowired
    private AttendanceDao attendanceDao;


    @Override
    public DtoGroup getGroupById(int groupId) {
        List<Level> levels = levelDao.getAll();
        Group group = groupDao.getEntityById(groupId);
        if (group == null) {
            throw new LogicException("There is no such group");
        }
        return getDtoGroup(levels, group);
    }

    @Override
    public List<DtoGroup> getAllGroups() {
        List<Group> groups = groupDao.getAll();
        List<DtoGroup> dtoGroups = new ArrayList<>();
        groups.forEach(g -> dtoGroups.add(new DtoGroup(g.getId(), g.getTitle())));
        return dtoGroups;
    }

    @Override
    public List<DtoGroup> getGroupsOfEmployee(Integer employeeId) {
        List<DtoGroup> dtoGroups = new ArrayList<>();
        List<Level> levels = levelDao.getAll();
        List<UserGroup> userGroups = userGroupDao.getByUser(employeeId);
        userGroups.forEach(g -> dtoGroups.add(getExtendedGtoGroup(levels, g)));
        return dtoGroups;
    }

    private DtoGroup getExtendedGtoGroup(List<Level> levels, UserGroup userGroup) {
        int courseId = userGroup.getCourseId();
        Course course = courseDao.getEntityById(courseId);
        String levelName = getLevelName(levels, course.getLevel());
        Integer groupId;
        String title;
        if (userGroup.getGroupId() == null || userGroup.getGroupId() == 0) {
            title = "";
            groupId = null;
        } else {
            Group group = groupDao.getEntityById(userGroup.getGroupId());
            title = group.getTitle();
            groupId = userGroup.getGroupId();
        }
        return new DtoGroup(groupId, title, courseId,
                course.getName(), course.getUserId(), levelName);
    }

    private String getLevelName(List<Level> levels, int levelId) {
        for (Level level : levels) {
            if (level.getId() == levelId) {
                return level.getTitle();
            }
        }
        return "Unknown";
    }


    @Override
    public List<DtoGroup> getGroupsAndQuantity() {
        List<Group> groups = groupDao.getAll();
        List<DtoGroup> dtoGroups = new ArrayList<>();
        groups.forEach(g -> {
            int n = groupDao.getNumberOfEmployeesInGroup(g.getId());
            dtoGroups.add(new DtoGroup(g.getId(), g.getTitle(), n));
        });
        return dtoGroups;
    }

    @Override
    public User getTrainer(int id) {
        return userDao.getTrainerByCourseId(id);
    }


    @Override
    public List<DtoGroup> getAllByTrainerId(Integer trainerId) {
        List<Group> groups = groupDao.getGroupByTrainerId(trainerId);
        List<Level> levels = levelDao.getAll();
        List<DtoGroup> dtoGroups = new ArrayList<>();
        for (Group group : groups) {
            DtoGroup dtoGroup = getDtoGroup(levels, group);
            dtoGroups.add(dtoGroup);
        }
        return dtoGroups;
    }

    private DtoGroup getDtoGroup(List<Level> levels, Group group) {
        int courseId = group.getCourseId();
        Course course = courseDao.getEntityById(courseId);
        String courseName = course.getName();
        return new DtoGroup(group.getId(), group.getTitle(), courseId,
                courseName, course.getUserId(),
                getLevelName(levels, course.getLevel()));
    }

    @Override
    public Map<String, Double> getAttendanceGraph(Integer groupId) {
        Map<String, Integer> attendance = new HashMap<>();
        statusDao.getAll().forEach(reason -> attendance.put(reason.getTitle().toLowerCase(), 0));
        List<Attendance> attendances = attendanceDao.getAttendanceByGroupId(groupId);
        int coefficient = 0;
        if (attendances != null && !attendances.isEmpty()) {
            for (Attendance instance : attendanceDao.getAttendanceByGroupId(groupId)) {
                String status = instance.getStatus().toLowerCase();
                attendance.put(status, attendance.get(status) + 1);
                coefficient++;
            }
        }
        final double percentage = 100.0 / coefficient;
        Map<String, Double> result = new HashMap<>();
        attendance.forEach((key, value) -> result.put(key, (value * percentage)));
        return result;
    }


    @Override
    public List<Group> getAllGroupsOfCourse(Integer courseId) {
        return groupDao.getAllGroupsOfCourse(courseId);
    }


    @Override
    public void removeStudentFromGroup(Integer userId, Integer groupId) {
        UserGroup userGroup = userGroupDao.getByUserAndGroup(userId, groupId);
        userGroup.setGroupId(null);
        userGroupDao.update(userGroup);
    }
}
