package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.service.DashBoardService;

@Log4j2
@Controller
@RequestMapping("/api/dashboard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;
    private Gson gson = new Gson();

    @RequestMapping(value = {"/level-and-quantity"}, method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public String getLevelAndQuantity() {
        return gson.toJson(dashBoardService.getLevelAndQuantity());
    }


    @RequestMapping(value = {"/level-and-trainers"}, method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public String getLevelAndTrainers() {
        return gson.toJson(dashBoardService.getLevelAndTrainers());
    }

    @RequestMapping(value = {"/training-and-quantity"}, method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public String getTrainingAndQuantity() {
        return gson.toJson(dashBoardService.getTrainingAndQuantity());
    }

}
