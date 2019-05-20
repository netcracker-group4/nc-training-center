package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoGroup;
import ua.com.nc.service.GroupsService;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private GroupsService groupsService;


    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(groupsService.getAllGroups(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-groups/{courseId}")
    @ResponseBody
    public List<Group> getGroupsInCourse(@PathVariable Integer courseId) {
        return groupsService.getAllGroupsOfCourse(courseId);
    }

    @RequestMapping(value = "/{groupId}")
    @ResponseBody
    public DtoGroup getGroup(@PathVariable Integer groupId) {
        log.info("retrieving group with  groupId = " + groupId);
        return groupsService.getGroupById(groupId);
    }

    @RequestMapping(value = "/{groupId}/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getStudents(@PathVariable Integer groupId) {
        return userDao.getByGroupId(groupId);
    }

    @RequestMapping(value = "/{groupId}/user/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteStudent(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupsService.removeStudentFromGroup(userId, groupId);
    }

    @RequestMapping(value = "/{id}/course", method = RequestMethod.GET)
    @ResponseBody
    public Course getCourseByGroup(@PathVariable Integer id) {
        return courseDao.getCourseByGroup(id);
    }

    @RequestMapping(value = {"/groups-and-quantity"}, method = RequestMethod.GET)
    @ResponseBody
    public List<DtoGroup> getLevelAndQuantity() {
        return groupsService.getGroupsAndQuantity();
    }

    @RequestMapping(value = {"/{id}/trainer"}, method = RequestMethod.GET)
    @ResponseBody
    public User getTeacher(@PathVariable Integer id) {
        return groupsService.getTrainer(id);

    }

    @RequestMapping(value = {"/employee/{employeeId}"}, method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public List<DtoGroup> getGroupsByUser(@PathVariable Integer employeeId) {
        return groupsService.getGroupsOfEmployee(employeeId);
    }

    @RequestMapping(value = {"/trainer/{employeeId}"}, method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public List<DtoGroup> getGroupsByTrainer(@PathVariable Integer employeeId) {
        return groupsService.getAllByTrainerId(employeeId);
    }

    @RequestMapping(value = "{id}/getAttendanceGraph")
    @ResponseBody
    public Map<String, Double> getAttendanceGraph(@PathVariable Integer id){
        return groupsService.getAttendanceGraph(id);
    }
}
