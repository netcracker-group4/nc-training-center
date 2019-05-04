package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.Attendance;
import ua.com.nc.dto.AttendanceDto;
import ua.com.nc.dto.UserAttendanceDto;
import ua.com.nc.service.AttendanceService;

import java.util.List;

@Log4j
@Controller
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAttendance(@RequestParam(required = false, name="userId") Integer userId,
                                        @RequestParam(required = false, name="courseId") Integer courseId){

        if(userId != null && courseId != null){
            List<Attendance> attendances = attendanceService.getAttendanceByStudentIdAndCourseId(userId, courseId);
            return ResponseEntity.ok().body(new Gson().toJson(attendances));
        }
        else{
            AttendanceDto attendanceDto = attendanceService.getAttendance();
        return ResponseEntity.ok().body(new Gson().toJson(attendanceDto));
        }
    }
}
