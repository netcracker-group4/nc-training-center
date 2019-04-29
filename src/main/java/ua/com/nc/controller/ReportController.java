package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.service.impl.ReportServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/download-report") //    /download-report/attendance-report.xlsx
public class ReportController {     //    /download-report/dashboard-report.xlsx

    @Autowired
    private ReportServiceImpl reportService;

    @RequestMapping(value = "/attendance-report.xlsx", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelAttendanceReport() throws IOException {

        ByteArrayInputStream in = reportService.getAttendanceExcel();

        HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment; filename = attendance-report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    @RequestMapping(value = "/dashboard-report.xlsx", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> excelDashboardReport() throws IOException {

        ByteArrayInputStream in = reportService.getDashboardExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename = dashboard-report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
