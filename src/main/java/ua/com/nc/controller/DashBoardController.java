package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.service.DashBoardService;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @RequestMapping(value = {"/level-and-quantity"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLevelAndQuantity() {
        if (userHasRightToSeeDashBoard()) {
            return dashBoardService.getLevelAndQuantity();
        }
        else return "ERROR";
    }

    private boolean userHasRightToSeeDashBoard() {
        //TODO method that checks if user is admin or smth else
        return true;
    }

    @RequestMapping(value = {"/level-and-trainers"}, method = RequestMethod.GET)
    @ResponseBody
    public String getLevelAndTrainers() {
        if (userHasRightToSeeDashBoard()) {
            return dashBoardService.getLevelAndTrainers();
        }
        else return "ERROR";
    }

    @RequestMapping(value = {"/training-and-quantity"}, method = RequestMethod.GET)
    @ResponseBody
    public String getTrainingAndQuantity() {
        if (userHasRightToSeeDashBoard()) {
            return dashBoardService.getTrainingAndQuantity();
        }
        else return "ERROR";
    }

}
