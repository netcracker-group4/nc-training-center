package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dto.DateDeserializer;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.service.LessonsService;

import java.sql.Timestamp;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping(value = "/schedule")
public class LessonsController {

    @Autowired
    LessonsService lessonsService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{groupId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveLessons(authentication, #groupId)")
    public String getAllForGroup(@PathVariable String groupId) {
        return lessonsService.getAllForGroup(Integer.parseInt(groupId));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/employee/{userId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToSeeScheduleOf(authentication, #userId)")
    public String getAllForUser(@PathVariable Integer userId) {
        return lessonsService.getAllForUser(userId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToAddLesson(authentication, #toAdd)")
    public String addLesson(@RequestBody String toAdd) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,
                new DateDeserializer()).create();
        DtoLesson toAdd1 = gson.fromJson(toAdd, DtoLesson.class);
        if (toAdd1.getId() == null || toAdd1.getId() == 0) {
            return lessonsService.addLesson(toAdd1);
        }
        return lessonsService.updateLesson(toAdd1);
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{lessonId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToDeleteLesson(authentication, #lessonId)")
    public String deleteLesson(@PathVariable Integer lessonId) {
        return lessonsService.deleteLesson(lessonId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{lessonId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToCancelLesson(authentication, #lessonId)")
    public String cancelLesson(@PathVariable Integer lessonId) {
        return lessonsService.cancelLesson(lessonId);
    }

}
