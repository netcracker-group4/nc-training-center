package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.dto.DateDeserializer;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.LessonsService;

import java.sql.Timestamp;

@Log4j2
@Controller
@RequestMapping(value = "/api/schedule")
public class LessonsController {

    @Autowired
    private LessonsService lessonsService;

    // default converter doesn't have custom adapter specified
    private Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, (JsonSerializer<Timestamp>)
            (timestamp, type, jsonSerializationContext) -> new JsonPrimitive(timestamp.toString())).create();


    @RequestMapping(method = RequestMethod.GET, value = "/{groupId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveLessons(authentication, #groupId)")
    public ResponseEntity<String> getAllForGroup(@PathVariable Integer groupId) {
        return ResponseEntity.ok(gson.toJson(lessonsService.getAllForGroup(groupId)));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/employee/{userId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToSeeScheduleOf(authentication, #userId)")
    public ResponseEntity<?> getAllForEmployee(@PathVariable Integer userId) {
        return ResponseEntity.ok(gson.toJson(lessonsService.getAllForEmployee(userId)));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/trainer/{userId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToSeeScheduleOf(authentication, #userId)")
    public ResponseEntity<?> getAllForTrainer(@PathVariable Integer userId) {
        return ResponseEntity.ok(gson.toJson(lessonsService.getAllForTrainer(userId)));
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/new")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToAddLesson(authentication, #toAdd)")
    public ResponseEntity<Integer> addLesson(@RequestBody String toAdd) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,
                new DateDeserializer()).create();
        DtoLesson toAdd1 = gson.fromJson(toAdd, DtoLesson.class);
        if (toAdd1.getId() == null || toAdd1.getId() == 0) {
            return ResponseEntity.ok(lessonsService.addLesson(toAdd1));
        } else throw new LogicException("Lesson is already created");
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToUpdateLesson(authentication, #toAdd)")
    public ResponseEntity<Integer> editLesson(@RequestBody String toAdd) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,
                new DateDeserializer()).create();
        DtoLesson toAdd1 = gson.fromJson(toAdd, DtoLesson.class);
        return ResponseEntity.ok(lessonsService.updateLesson(toAdd1));
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{lessonId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToDeleteLesson(authentication, #lessonId)")
    public ResponseEntity<String> deleteLesson(@PathVariable Integer lessonId) {
        lessonsService.deleteLesson(lessonId);
        return ResponseEntity.ok("Deleted");
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{lessonId}")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToCancelLesson(authentication, #lessonId)")
    public ResponseEntity<Boolean> cancelLesson(@PathVariable Integer lessonId) {
        return ResponseEntity.ok(lessonsService.invertIsCanceledForLesson(lessonId));
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{lessonId}/performed")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToCancelLesson(authentication, #lessonId)")
    public ResponseEntity<Boolean> performLesson(@PathVariable Integer lessonId) {
        return ResponseEntity.ok(lessonsService.invertIsPerformedForLesson(lessonId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/lesson/{lessonId}")
    public ResponseEntity<String> getLesson(@PathVariable Integer lessonId) {
        DtoLesson lessonById = lessonsService.getLessonById(lessonId);
        return ResponseEntity.ok(gson.toJson(lessonById));
    }
}
