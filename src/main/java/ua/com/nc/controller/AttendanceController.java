package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.domain.Attendance;
import ua.com.nc.security.CustomSecurityService;
import ua.com.nc.service.AttendanceService;
import ua.com.nc.service.SerializationService;
import ua.com.nc.utils.CastUtils;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CustomSecurityService customSecurityService;

    @Autowired
    private SerializationService serializationService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAttendance(@RequestParam(required = false, name = "userId") Integer userId,
                                           @RequestParam(required = false, name = "courseId") Integer courseId,
                                           @RequestParam(required = false, name = "groupId") Integer groupId,
                                           @RequestParam(required = false, name = "lessonId") Integer lessonId) {

//        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").serializeNulls().create();
        List<Attendance> attendances = attendanceService.getAttendance(userId, courseId, groupId, lessonId);
        if (attendances != null) {
            return ResponseEntity.ok().body(serializationService.serializeWithDateFormat(attendances));
        } else return ResponseEntity.badRequest().body("Bad request");
    }

    @RequestMapping(value = "/{lessonId}", method = RequestMethod.POST)
    public ResponseEntity<?> setAttendance(@PathVariable Integer lessonId) {
        attendanceService.attendanceInsert(lessonId);
        return ResponseEntity.ok("OK");
    }


    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToUpdateAttendance(authentication, #attendanceId)")
    public ResponseEntity<?> updateAttendance(@RequestParam Integer attendanceId,
                                              @RequestParam Integer statusId,
                                              @RequestParam String absenceId) {

        Integer integerAbsenceId = CastUtils.castIntegerToString(absenceId);
        attendanceService.attendanceUpdate(attendanceId, statusId, integerAbsenceId);
        return ResponseEntity.ok("Ok");
    }
}
