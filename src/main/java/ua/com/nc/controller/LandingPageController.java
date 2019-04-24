package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.service.DashBoardService;
import ua.com.nc.service.LandingPageService;
import ua.com.nc.service.impl.DashBoardServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/MainPage")
public class LandingPageController {

    @Autowired
    private LandingPageService landingPageService;

    @RequestMapping (value = {"/courses-on-landing-page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLandingPageCourses () { return landingPageService.getLandingPageCourses(); }

    @RequestMapping (value = {"/trainers-on-landing-page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLandingPageTrainers () { return landingPageService.getLandingPageTrainers(); }

    @RequestMapping (value = {"/update-course-landing-page"}, method = RequestMethod.POST)
    @ResponseBody
    public void updateCourseLandingPage (@RequestParam(name = "isOnLandingPage") boolean isOnLandingPage,
                                         @RequestParam (name = "id") int id) {
        landingPageService.updateCourseLandingPage (id, isOnLandingPage);
    }

    @RequestMapping (value = {"/update-trainer-landing-page"}, method = RequestMethod.POST)
    @ResponseBody
    public void updateTrainerLandingPage (@RequestParam (name = "isOnLandingPage") boolean isOnLandingPage,
                                          @RequestParam (name = "id") int id) {
        landingPageService.updateTrainerLandingPage(id, isOnLandingPage);
    }

}
