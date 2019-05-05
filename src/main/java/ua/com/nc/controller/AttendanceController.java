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
@CrossOrigin(origins = "http://localhost:8000")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAttendance(@RequestParam(required = false, name="userId") Integer userId,
                                        @RequestParam(required = false, name="courseId") Integer courseId,
                                        @RequestParam(required = false, name="groupId") Integer groupId){

        if(userId != null && courseId != null){
            List<Attendance> attendances = attendanceService.getAttendanceByStudentIdAndCourseId(userId, courseId);
            return ResponseEntity.ok().body(new Gson().toJson(attendances));
        }if(userId != null && groupId != null){
            List<Attendance> attendances = attendanceService.getAttendanceByStudentIdAndGroupId(userId, groupId);
            return ResponseEntity.ok().body(new Gson().toJson(attendances));
        }
        else{
            AttendanceDto attendanceDto = attendanceService.getAttendance();
        return ResponseEntity.ok().body(new Gson().toJson(attendanceDto));
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateAttendance(@RequestParam String attendanceId,
                                              @RequestParam String statusId,
                                              @RequestParam String absenceId){
        Integer integerAttendanceId;
        Integer integerStatusId;
        Integer integerAbsenceId;

        if(attendanceId.trim().equals("null")){
            integerAttendanceId = null;
        }else{
            integerAttendanceId = Integer.parseInt(attendanceId);
        }
        if(statusId.trim().equals("null")){
            integerStatusId = null;
        }else{
            integerStatusId = Integer.parseInt(statusId);
        }
        if(absenceId.trim().equals("null")){
            integerAbsenceId = null;
        }else{
            integerAbsenceId = Integer.parseInt(absenceId);
        }

        log.debug(integerAttendanceId + " " + integerStatusId + " " + integerAbsenceId);
        attendanceService.attendanceUpdate(integerAttendanceId, integerStatusId, integerAbsenceId);
        return ResponseEntity.ok().body("Ok");
    }
}
