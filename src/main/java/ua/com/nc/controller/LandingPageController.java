package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private LandingPageService landingPageService;

    @RequestMapping (value = {"/courses-on-landing-page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLandingPageCourses () {
        return landingPageService.getLandingPageCourses();
    }

    @RequestMapping (value = {"/trainers-on-landing-page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLandingPageTrainers () {return landingPageService.getLandingPageTrainers(); }
}
