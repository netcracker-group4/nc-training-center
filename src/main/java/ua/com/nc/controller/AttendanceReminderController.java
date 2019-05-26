package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.service.AttendanceReminderService;
import ua.com.nc.service.EmailService;

@Log4j2
@Controller
public class AttendanceReminderController {

    @Autowired
    private AttendanceReminderService attendanceReminderService;

    @Autowired
    private EmailService emailService;

    public void sendAttendanceReminders (@RequestParam(name = "lessonId") int lessonId) {

//        emailService.sendAttendanceReminders(lessonId);

    }

}
