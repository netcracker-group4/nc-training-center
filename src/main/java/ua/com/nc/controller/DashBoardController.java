package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.dto.CourseAndGroups;
import ua.com.nc.dto.DTOLevel;
import ua.com.nc.dto.DTOTrainer;
import ua.com.nc.service.DashBoardService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/dashboard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @RequestMapping(value = {"/level-and-quantity"}, method = RequestMethod.GET)

    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<List<DTOLevel>> getLevelAndQuantity() {
        return ResponseEntity.ok(dashBoardService.getLevelAndQuantity());
    }


    @RequestMapping(value = {"/level-and-trainers"}, method = RequestMethod.GET)

    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<List<DTOTrainer>> getLevelAndTrainers() {
        return ResponseEntity.ok(dashBoardService.getLevelAndTrainers());
    }

    @RequestMapping(value = {"/training-and-quantity"}, method = RequestMethod.GET)

    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<CourseAndGroups>> getTrainingAndQuantity() {
        return ResponseEntity.ok(dashBoardService.getTrainingAndQuantity());
    }

}
