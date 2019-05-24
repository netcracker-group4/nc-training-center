package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.SuitabilityDao;
import ua.com.nc.domain.User;
import ua.com.nc.dto.schedule.DesiredToSave;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.service.DesiredScheduleService;

@Log4j2
@Controller
@RequestMapping("/api/desired-schedule")
public class DesiredScheduleController {

    @Autowired
    private DesiredScheduleService desiredScheduleService;

    private final Gson gson = new Gson();

    @Autowired
    private SuitabilityDao suitabilityDao;

    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(value = {"/{id}/ungrouped"}, method = RequestMethod.GET)
    public String getDesiredScheduleForUngroupedStudentsForCourse(@PathVariable("id") Integer id) throws Exception {
        return gson.toJson(desiredScheduleService.getDesiredScheduleForUngroupedStudentsOfCourse(id));
    }

    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(value = {"/{id}/grouped"}, method = RequestMethod.GET)
    public String getDesiredScheduleForFormedGroupsForCourse(@PathVariable("id") Integer id) throws Exception {
        return gson.toJson(desiredScheduleService.getDesiredScheduleForFormedGroupsForCourse(id));
    }


    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addGroup(@RequestBody GroupSchedule groupSchedule) {
        int newId;
        if (groupSchedule.getId() == 0) {
            newId = desiredScheduleService.add(groupSchedule);
        } else {
            newId = desiredScheduleService.update(groupSchedule);
        }
        return Integer.toString(newId);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(path = "/invert-attending/{userGroupId}", method = RequestMethod.GET)
    public void invertUser(@PathVariable Integer userGroupId) {
        desiredScheduleService.invertAttending(userGroupId);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteGroup(@PathVariable Integer id) {
        desiredScheduleService.delete(id);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name()) " +
            "|| hasAuthority(T(ua.com.nc.domain.Role).TRAINER.name())")
    @RequestMapping(value = {"/{groupId}"}, method = RequestMethod.GET)
    public String getDesiredScheduleForGroup(@PathVariable("groupId") Integer groupId) throws Exception {
        return gson.toJson(desiredScheduleService.getDesiredScheduleForGroup(groupId));
    }

    @ResponseBody
    @RequestMapping(value = {"/day-intervals"}, method = RequestMethod.GET)
    public String getDayIntervals() {
        return gson.toJson(desiredScheduleService.getDayIntervals());
    }


    @ResponseBody
    @PreAuthorize("@customSecuritySecurity.canJoinCourse(authentication, #desiredToSave)")
    @RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addGroup(@AuthenticationPrincipal User user, @RequestBody DesiredToSave desiredToSave) {
        log.info(desiredToSave);
        desiredScheduleService.saveDesired(user.getId(), desiredToSave);
    }


    @RequestMapping(value = "/suitabilities", method = RequestMethod.GET)
    public ResponseEntity<?> getSuitabilities() {
        return new ResponseEntity<>(suitabilityDao.getAll(), HttpStatus.OK);
    }


}
