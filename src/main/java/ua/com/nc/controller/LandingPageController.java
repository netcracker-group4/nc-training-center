package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;
import ua.com.nc.service.LandingPageService;
import ua.com.nc.service.SerializationService;

import java.util.List;


@Log4j2
@Controller
@RequestMapping("/api/main-page")
public class LandingPageController {

    @Autowired
    private LandingPageService landingPageService;
    @Autowired
    private SerializationService serializationService;

//    private Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();

    @RequestMapping(value = {"/courses-on-landing-page"}, method = RequestMethod.GET)
    public ResponseEntity<String> getLandingPageCourses() {
        log.info("getting courses on landing page");
        List<Course> landingPageCourses = landingPageService.getLandingPageCourses();
        String body = serializationService.serializationWithDateTimeFormat(landingPageCourses);
        return ResponseEntity.ok(body);
    }

    @RequestMapping(value = {"/trainers-on-landing-page"}, method = RequestMethod.GET)
    public ResponseEntity<String> getLandingPageTrainers() {
        List<User> landingPageTrainers = landingPageService.getLandingPageTrainers();
        String body = serializationService.serializationWithDateTimeFormat(landingPageTrainers);
        return ResponseEntity.ok(body);
    }

    @RequestMapping(value = {"/update-course-landing-page"}, method = RequestMethod.POST)
    public ResponseEntity<String> updateCourseLandingPage(@RequestParam(name = "isOnLandingPage") boolean isOnLandingPage,
                                        @RequestParam(name = "id") Integer id) {
        landingPageService.updateCourseLandingPage(id, isOnLandingPage);
        return ResponseEntity.ok("Updated");
    }

    @RequestMapping(value = {"/update-trainer-landing-page"}, method = RequestMethod.POST)
    public ResponseEntity<String> updateTrainerLandingPage(@RequestParam(name = "isOnLandingPage") boolean isOnLandingPage,
                                                           @RequestParam(name = "id") Integer id) {
        landingPageService.updateTrainerLandingPage(id, isOnLandingPage);
        return ResponseEntity.ok("Updated");
    }

}
