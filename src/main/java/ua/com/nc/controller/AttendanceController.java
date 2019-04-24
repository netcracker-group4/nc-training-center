package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.dto.AttendanceDto;
import ua.com.nc.service.AttendanceService;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public String getAttendance(){
        AttendanceDto attendanceDto = attendanceService.getAttendance();
        return new Gson().toJson(attendanceDto);
    }
}
