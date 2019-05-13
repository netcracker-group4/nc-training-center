package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.dao.interfaces.UserGroupDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.service.GroupsService;

import java.util.List;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private UserGroupDao userGroupDao;
    private final Gson gson = new Gson();


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addGroup(@RequestBody GroupSchedule groupSchedule) {
        int newId;
        if (groupSchedule.getId() == 0) {
            newId = groupsService.add(groupSchedule);
        } else {
            newId = groupsService.update(groupSchedule);
        }
        return Integer.toString(newId);
    }

    @RequestMapping(path = "/invert-attending/{userGroupId}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public String invertUser(@PathVariable Integer userGroupId) {
        groupsService.invertAttending(userGroupId);
        return "User updated";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteGroup(@PathVariable Integer id) {
        groupsService.delete(id);
        return "Group deleted";
    }

    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(groupsService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-groups/{id}")
    @ResponseBody
    public String getGroupsInCourse(@PathVariable Integer id) {
        return gson.toJson(groupDao.getAllGroupsOfCourse(id));
    }

    @RequestMapping(value = "/{id}")
    @ResponseBody
    public String getGroup(@PathVariable Integer id) {
        return gson.toJson(groupDao.getEntityById(id));
    }

    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getStudents(@PathVariable Integer id) {
        return userDao.getByGroupId(id);
    }

    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteStudent(@PathVariable Integer id, @PathVariable Integer userId) {
        groupDao.deleteUserFromGroup(id, userId);
    }

    @RequestMapping(value = "/{id}/course", method = RequestMethod.GET)
    @ResponseBody
    public Course getCourseByGroup(@PathVariable Integer id) {
        return courseDao.getCourseByGroup(id);
    }

    @RequestMapping(value = {"/groups-and-quantity"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLevelAndQuantity() {
        return gson.toJson(groupsService.getGroupsAndQuantity());
    }

    @RequestMapping(value = {"/{id}/trainer"}, method = RequestMethod.GET)
    @ResponseBody
    public User getTeacher(@PathVariable Integer id) {
        return groupsService.getTrainer(id);

    }

    @RequestMapping(value = {"/employee/{employeeId}"}, method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public String getGroupsByUser(@PathVariable Integer employeeId) {
        return gson.toJson(groupsService.getAllByEmployeeId(employeeId));
    }

}
