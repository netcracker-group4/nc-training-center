//package ua.com.nc.controller;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestParam;
//import ua.com.nc.domain.User;
//import ua.com.nc.service.AttendanceReminderService;
//import ua.com.nc.service.EmailService;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
//
//@Log4j2
//@Controller
//public class AttendanceReminderController {
//
//    @Autowired
//    private AttendanceReminderService attendanceReminderService;
//
//    @Autowired
//    private EmailService emailService;
//
//    public void sendAttendanceReminders (@RequestParam(name = "lessonId") int lessonId) {
//
//        emailService.sendAttendanceReminders(lessonId);
//    }
//
//}
