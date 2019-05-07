package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.service.impl.ReportServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/download-report") //    /download-report/attendance-report/1
public class ReportController {     //    /download-report/dashboard-report

    @Autowired
    private ReportServiceImpl reportService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserNameSimple(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal.getName();
    }

    @RequestMapping(value = "/attendance-report", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelAttendanceReport() throws IOException {

        ByteArrayInputStream in = reportService.getAttendanceExcel();

        HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment; filename = attendance-report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    @RequestMapping(value = "/attendance-report/{groupId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource>  excelGroupAttendanceReport(@PathVariable Integer groupId) throws IOException {

        ByteArrayInputStream in = reportService.getAttendanceExcel(groupId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename = attendance-report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    @RequestMapping(value = "/dashboard-report", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelDashboardReport() throws IOException {

        ByteArrayInputStream in = reportService.getDashboardExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename = dashboard-report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
