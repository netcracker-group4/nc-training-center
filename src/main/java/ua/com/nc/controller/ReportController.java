package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.service.GroupsService;
import ua.com.nc.service.ReportService;

import java.io.IOException;

@Log4j2
@Controller
@RequestMapping(value = "/api/download-report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private GroupsService groupService;

    /**
     * Retrieves attendance of groups by their trainer id, if authenticated trainer
     * else get full attendance report by admin
     * @param user to check who is requesting
     * @return automatically download process with excel to be downloaded
     */
    @RequestMapping(value = "/attendance-report", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name()) " +
            "|| hasAuthority(T(ua.com.nc.domain.Role).TRAINER.name())")
    public ResponseEntity<InputStreamResource> excelAttendanceReport(
            @AuthenticationPrincipal User user) throws IOException {
        return getAttendanceReport(user);
    }

    /**
     * Get group attendance report by group id
     * @param groupId used to find particular group
     * @return automatically download process with excel to be downloaded
     */
    @RequestMapping(value = "/attendance-report/{groupId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name()) " +
            "|| hasAuthority(T(ua.com.nc.domain.Role).TRAINER.name())")
    public ResponseEntity<InputStreamResource> excelGroupAttendanceReport(
            @PathVariable Integer groupId) throws IOException {
        return ResponseEntity.ok()
                .headers(getHttpHeaders(
                        groupService.getGroupById(groupId).getTitle() + " attendance report"))
                .body(new InputStreamResource(
                        reportService.getAttendanceExcel(groupId)));
    }

    /**
     * Get dashboard report, Level And Quantity, Level And Trainers, Training And Quantity
     * @return automatically download process with excel to be downloaded
     */
    @RequestMapping(value = "/dashboard-report", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name()) ")
    public ResponseEntity<InputStreamResource> excelDashboardReport()
            throws IOException {
        return ResponseEntity.ok()
                .headers(getHttpHeaders(
                        "NCTraining center dashboard report"))
                .body(
                        new InputStreamResource(
                                reportService.getDashboardExcel()));

    }

    private ResponseEntity<InputStreamResource> getAttendanceReport(
            @AuthenticationPrincipal User user) throws IOException {
        if (user.getRoles().contains(Role.ADMIN)) {
            return ResponseEntity.ok()
                    .headers(getHttpHeaders(
                            "NCTraining center attendance report"))
                    .body(
                            new InputStreamResource(
                                    reportService.getAttendanceExcel()));
        } else {
            return ResponseEntity.ok()
                    .headers(getHttpHeaders(
                            (user.getLastName() != null
                                    ? user.getLastName()
                                    : user.getFirstName())
                                    + "'s groups attendance report"))
                    .body(
                            new InputStreamResource(
                                    reportService.getAttendanceExcel(user)));
        }
    }

    private HttpHeaders getHttpHeaders(String fileName) {
        String fileType = ".xlsx";
        String headerName = "Content-Disposition";
        String headerValue = "attachment; filename =";
        HttpHeaders headers = new HttpHeaders();
        headers.add(headerName, headerValue + fileName + fileType);
        return headers;
    }
}
