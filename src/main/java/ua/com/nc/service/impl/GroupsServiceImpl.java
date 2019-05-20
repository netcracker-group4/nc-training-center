package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.dto.DtoGroup;
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
        List<Group> groups = new ArrayList<>();
        groups.add(groupDao.getGroupById(groupId));
        return getDtoGroups(levels, groups).get(0);
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

    @Override
    public List<DtoGroup> getAllByEmployeeId(Integer employeeId) {
        List<DtoGroup> dtoGroups = new ArrayList<>();
        List<Level> levels = levelDao.getAll();
        List<UserGroup> userGroups = userGroupDao.getByUser(employeeId);
        for (UserGroup userGroup : userGroups) {
            int courseId = userGroup.getCourseId();
            Course course = courseDao.getEntityById(courseId);
            String courseName = course.getName();
            if (userGroup.getGroupId() == null || userGroup.getGroupId() == 0) {
                dtoGroups.add(new DtoGroup(null, "", courseId,
                        courseName, course.getUserId(),
                        getLevelName(levels, course.getLevel())));
            } else {
                Group group = groupDao.getEntityById(userGroup.getGroupId());
                dtoGroups.add(new DtoGroup(userGroup.getGroupId(), group.getTitle(), courseId,
                        courseName, course.getUserId(),
                        getLevelName(levels, course.getLevel())));
            }

        }
        return dtoGroups;
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


    @Override
    public List<DtoGroup> getAllByTrainerId(Integer trainerId) {
        List<Level> levels = levelDao.getAll();
        List<Group> groups = groupDao.getGroupByTrainerId(trainerId);
        return getDtoGroups(levels, groups);
    }

    private List<DtoGroup> getDtoGroups(List<Level> levels, List<Group> groups) {
        List<DtoGroup> dtoGroups = new ArrayList<>();
        if (groups != null && groups.size() != 0) {
            for (Group group : groups) {
                int courseId = group.getCourseId();
                Course course = courseDao.getEntityById(courseId);
                String courseName = course.getName();
                dtoGroups.add(new DtoGroup(group.getId(), group.getTitle(), courseId,
                        courseName, course.getUserId(),
                        getLevelName(levels, course.getLevel())));
            }
        }
        return dtoGroups;
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
}
