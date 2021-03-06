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
        for (UserGroup userGroup : userGroups) {
            DtoGroup extendedGtoGroup = getExtendedGtoGroup(levels, userGroup);
            if (extendedGtoGroup != null)
                dtoGroups.add(extendedGtoGroup);
        }
        return dtoGroups;
    }

    private DtoGroup getExtendedGtoGroup(List<Level> levels, UserGroup userGroup) {
        int courseId = userGroup.getCourseId();
        Course course = courseDao.getEntityById(courseId);
        if (course != null) {
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
        return null;
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
        int coefficient = 0;
        int latenessCoefficient = 0;
        Map<String, Double> attendance = new HashMap<>();
        statusDao.getAll().forEach(reason -> attendance.put(reason.getTitle().toLowerCase(), 0.0));
        List<Attendance> attendances = attendanceDao.getAttendanceByGroupId(groupId);
        if (attendances != null && !attendances.isEmpty()) {
            for (Attendance instance : attendances) {
                String status = instance.getStatus().toLowerCase();
                if (status.equals("late")) {
                    attendance.put("present", attendance.get("present") + 1);
                    coefficient++;
                    latenessCoefficient++;
                } else {
                    attendance.put(status, attendance.get(status) + 1);
                    coefficient++;
                }
            }
            return countPercentages(coefficient, latenessCoefficient, attendance);
        } else {
            return attendance;
        }
    }

    private Map<String, Double> countPercentages(int coefficient, int latenessCoefficient, Map<String, Double> attendance) {
        Map<String, Double> result = new HashMap<>();
        final double percentage = 100.0 / coefficient;
        attendance.forEach((key, value) -> result.put(key, (value * percentage)));
        if (!attendance.get("present").equals(0.0)){
            final double latenessPercentage = 100.0 / (attendance.get("present") - latenessCoefficient);
            result.replace("late", latenessCoefficient * latenessPercentage);
        } else {
            result.replace("late", 0.0);
        }
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
