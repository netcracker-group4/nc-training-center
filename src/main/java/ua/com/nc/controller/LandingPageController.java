package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.service.LandingPageService;


@Log4j2
@Controller
@RequestMapping("/api/main-page")
public class LandingPageController {

    @Autowired
    private LandingPageService landingPageService;
    private Gson gson = new Gson();

    @RequestMapping(value = {"/courses-on-landing-page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLandingPageCourses() {
        log.info("getting courses on landing page");
        return gson.toJson(landingPageService.getLandingPageCourses());
    }

    @RequestMapping(value = {"/trainers-on-landing-page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLandingPageTrainers() {
        return gson.toJson(landingPageService.getLandingPageTrainers());
    }

    @RequestMapping(value = {"/update-course-landing-page"}, method = RequestMethod.POST)
    @ResponseBody
    public void updateCourseLandingPage(@RequestParam(name = "isOnLandingPage") boolean isOnLandingPage,
                                        @RequestParam(name = "id") int id) {
        landingPageService.updateCourseLandingPage(id, isOnLandingPage);
    }

    @RequestMapping(value = {"/update-trainer-landing-page"}, method = RequestMethod.POST)
    @ResponseBody
    public void updateTrainerLandingPage(@RequestParam(name = "isOnLandingPage") boolean isOnLandingPage,
                                         @RequestParam(name = "id") int id) {
        landingPageService.updateTrainerLandingPage(id, isOnLandingPage);
    }

}
