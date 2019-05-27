package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.dao.interfaces.SuitabilityDao;
import ua.com.nc.domain.User;
import ua.com.nc.dto.schedule.DesiredToSave;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.dto.schedule.ScheduleForUser;
import ua.com.nc.service.DesiredScheduleService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/desired-schedule")
public class DesiredScheduleController {

    @Autowired
    private DesiredScheduleService desiredScheduleService;
    @Autowired
    private SuitabilityDao suitabilityDao;


    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(value = {"/{id}/ungrouped"}, method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleForUser>> getDesiredScheduleForUngroupedStudentsForCourse(
            @PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(
                desiredScheduleService.getDesiredScheduleForUngroupedStudentsOfCourse(id));
    }


    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(value = {"/{id}/grouped"}, method = RequestMethod.GET)
    public ResponseEntity<List<GroupSchedule>> getDesiredScheduleForFormedGroupsForCourse(
            @PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(
                desiredScheduleService.getDesiredScheduleForFormedGroupsForCourse(id));
    }


    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addGroup(@RequestBody GroupSchedule groupSchedule) {
        int newId;
        if (groupSchedule.getId() == 0) {
            newId = desiredScheduleService.add(groupSchedule);
        } else {
            newId = desiredScheduleService.update(groupSchedule);
        }
        return ResponseEntity.ok(newId);
    }


    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(path = "/invert-attending/{userGroupId}", method = RequestMethod.GET)
    public ResponseEntity<String> invertUser(@PathVariable Integer userGroupId) {
        desiredScheduleService.invertAttending(userGroupId);
        return ResponseEntity.ok("Inverted");
    }


    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Integer id) {
        desiredScheduleService.delete(id);
        return ResponseEntity.ok("Deleted");
    }


    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name()) " +
            "|| hasAuthority(T(ua.com.nc.domain.Role).TRAINER.name())")
    @RequestMapping(value = {"/{groupId}"}, method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleForUser>> getDesiredScheduleForGroup(@PathVariable("groupId") Integer groupId) throws Exception {
        return ResponseEntity.ok(desiredScheduleService.getDesiredScheduleForGroup(groupId));
    }


    @RequestMapping(value = {"/day-intervals"}, method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDayIntervals() {
        return ResponseEntity.ok(desiredScheduleService.getDayIntervals());
    }


    @PreAuthorize("@customSecuritySecurity.canJoinCourse(authentication, #desiredToSave)")
    @RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addGroup(@AuthenticationPrincipal User user, @RequestBody DesiredToSave desiredToSave) {
        log.info(desiredToSave);
        desiredScheduleService.saveDesired(user.getId(), desiredToSave);
        return ResponseEntity.ok("Saved");
    }


    @RequestMapping(value = "/suitabilities", method = RequestMethod.GET)
    public ResponseEntity<?> getSuitabilities() {
        return ResponseEntity.ok(suitabilityDao.getAll());
    }


}
