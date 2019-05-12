package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.service.GroupsService;
import ua.com.nc.service.ReportService;
import ua.com.nc.service.RoleService;

import java.io.IOException;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/download-report") //    /download-report/attendance-report/1
public class ReportController {     //    /download-report/dashboard-report

    @Autowired
    private ReportService reportService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupsService groupService;

    private HttpHeaders getHttpHeaders(String fileName) {
        String fileType = ".xlsx";
        String headerName = "Content-Disposition";
        String headerValue = "attachment; filename =";
        HttpHeaders headers = new HttpHeaders();
        headers.add(headerName, headerValue + fileName + fileType);
        return headers;
    }

    //TODO Security logic should be removed to service
    //get full attendance report by admin
    //get attendance of groups by their trainer id, authenticated trainer
    @RequestMapping(value = "/attendance-report", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelAttendanceReport(
            @AuthenticationPrincipal User user) throws IOException {

        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)) {
            return ResponseEntity.ok()
                    .headers(getHttpHeaders("NCTraining center attendance report"))
                    .body(new InputStreamResource(reportService.getAttendanceExcel()));
        } else if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.TRAINER)) {
            return ResponseEntity.ok()
                    .headers(getHttpHeaders(
                            (user.getLastName() != null
                                    ? user.getLastName()
                                    : user.getFirstName())
                                    + "'s groups attendance report"))
                    .body(new InputStreamResource(reportService.getAttendanceExcel(user)));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    //get group attendance report by group id
    @RequestMapping(value = "/attendance-report/{groupId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelGroupAttendanceReport(
            @PathVariable Integer groupId,
            @AuthenticationPrincipal User user) throws IOException {

        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)) {
            return ResponseEntity.ok()
                    .headers(
                            getHttpHeaders(
                                    groupService.getGroupById(groupId).getTitle()
                                            + " attendance report"))
                    .body(
                            new InputStreamResource(reportService.getAttendanceExcel(groupId)));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/dashboard-report", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelDashboardReport(
            @AuthenticationPrincipal User user) throws IOException {

        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)) {
            return ResponseEntity.ok()
                    .headers(getHttpHeaders(
                            "NCTraining center dashboard report"))
                    .body(
                            new InputStreamResource(reportService.getDashboardExcel()));
        } else {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
