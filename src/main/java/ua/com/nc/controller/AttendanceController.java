package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.Attendance;
import ua.com.nc.dto.AttendanceDto;
import ua.com.nc.service.AttendanceService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAttendance(@RequestParam(required = false, name = "userId") Integer userId,
                                           @RequestParam(required = false, name = "courseId") Integer courseId,
                                           @RequestParam(required = false, name = "groupId") Integer groupId,
                                           @RequestParam(required = false, name = "lessonId") Integer lessonId) {

        if (userId != null && courseId != null) {
            List<Attendance> attendances = attendanceService.getAttendanceByStudentIdAndCourseId(userId, courseId);
            return ResponseEntity.ok().body(new Gson().toJson(attendances));
        }
        if (lessonId != null) {
            Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
            List<Attendance> attendances = attendanceService.getAttendanceByLessonId(lessonId);
            return ResponseEntity.ok().body(gson.toJson(attendances));
        }
        if (userId != null && groupId != null) {
            Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").serializeNulls().create();
            List<Attendance> attendances = attendanceService.getAttendanceByStudentIdAndGroupId(userId, groupId);
            return ResponseEntity.ok().body(gson.toJson(attendances));
        } else {
            AttendanceDto attendanceDto = attendanceService.getAttendance();
            return ResponseEntity.ok().body(new Gson().toJson(attendanceDto));
        }
    }

    @RequestMapping(value = "/{lessonId}", method = RequestMethod.POST)
    public ResponseEntity<?> setAttendance(@PathVariable Integer lessonId) {
        attendanceService.attendanceInsert(lessonId);
        return ResponseEntity.ok().body("OK");
    }
    

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateAttendance(@RequestParam String attendanceId,
                                              @RequestParam String statusId,
                                              @RequestParam String absenceId) {
        Integer integerAttendanceId;
        Integer integerStatusId;
        Integer integerAbsenceId;

        if (attendanceId.trim().equals("null") || attendanceId.equals("undefined")) {
            integerAttendanceId = null;
        } else {
            integerAttendanceId = Integer.parseInt(attendanceId);
        }
        if (statusId.trim().equals("null") || statusId.equals("undefined")) {
            integerStatusId = null;
        } else {
            integerStatusId = Integer.parseInt(statusId);
        }
        if (absenceId.trim().equals("null") || absenceId.equals("undefined")) {
            integerAbsenceId = null;
        } else {
            integerAbsenceId = Integer.parseInt(absenceId);
        }

        attendanceService.attendanceUpdate(integerAttendanceId, integerStatusId, integerAbsenceId);
        return ResponseEntity.ok().body("Ok");
    }
}
