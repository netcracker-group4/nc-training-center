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
    public ResponseEntity<List<Group>> getGroupsInCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(groupsService.getAllGroupsOfCourse(courseId));
    }

    @RequestMapping(value = "/{groupId}")
    public ResponseEntity<DtoGroup> getGroup(@PathVariable Integer groupId) {
        log.info("retrieving group with  groupId = " + groupId);
        return ResponseEntity.ok(groupsService.getGroupById(groupId));
    }

    @RequestMapping(value = "/{groupId}/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getStudents(@PathVariable Integer groupId) {
        return ResponseEntity.ok(userDao.getByGroupId(groupId));
    }

    @RequestMapping(value = "/{groupId}/user/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupsService.removeStudentFromGroup(userId, groupId);
        return ResponseEntity.ok("Removed");
    }

    @RequestMapping(value = "/{id}/course", method = RequestMethod.GET)
    public ResponseEntity<Course> getCourseByGroup(@PathVariable Integer id) {
        return ResponseEntity.ok(courseDao.getCourseByGroup(id));
    }

    @RequestMapping(value = {"/groups-and-quantity"}, method = RequestMethod.GET)
    public ResponseEntity<List<DtoGroup>> getLevelAndQuantity() {
        return ResponseEntity.ok(groupsService.getGroupsAndQuantity());
    }

    @RequestMapping(value = {"/{id}/trainer"}, method = RequestMethod.GET)
    public ResponseEntity<User> getTeacher(@PathVariable Integer id) {
        return ResponseEntity.ok(groupsService.getTrainer(id));

    }

    @RequestMapping(value = {"/employee/{employeeId}"}, method = RequestMethod.GET)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<List<DtoGroup>> getGroupsByUser(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(groupsService.getGroupsOfEmployee(employeeId));
    }

    @RequestMapping(value = {"/trainer/{employeeId}"}, method = RequestMethod.GET)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<List<DtoGroup>> getGroupsByTrainer(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(groupsService.getAllByTrainerId(employeeId));
    }

    @RequestMapping(value = "{id}/attendance-graph")
    public  ResponseEntity<Map<String, Double>> getAttendanceGraph(@PathVariable Integer id) {
        return ResponseEntity.ok(groupsService.getAttendanceGraph(id));
    }
}
