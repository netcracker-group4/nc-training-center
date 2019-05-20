package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.domain.User;
import ua.com.nc.service.AttendanceReminderService;
import ua.com.nc.service.EmailService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Controller
public class AttendanceReminderController {

    @Autowired
    private AttendanceReminderService attendanceReminderService;

    @Autowired
    private EmailService emailService;

    public void sendAttendanceReminders (@RequestParam(name = "lessonId") int lessonId) {

        HashMap<User, User> absentStudentsAndTheirManagers =
                attendanceReminderService.getStudentsAbsentWitNoReason(lessonId);
        Set<User> students = absentStudentsAndTheirManagers.keySet();
        String studentsText = emailService.textGenerator(students);

        User admin = attendanceReminderService.getAdmin();
        String adminEmail = admin.getEmail();
        emailService.sendAttendanceReminderEmail(adminEmail, studentsText);

        User trainer = attendanceReminderService.getLessonTrainer(lessonId);
        String trainerEmail = trainer.getEmail();
        emailService.sendAttendanceReminderEmail(trainerEmail, studentsText);

        HashMap<User, ArrayList<User>> managersAndStudents =
                emailService.reverseHashMap (absentStudentsAndTheirManagers);

        for (HashMap.Entry<User, ArrayList<User>> entry : managersAndStudents.entrySet()) {
            User manager = entry.getKey();
            Set <User> managerStudents = new HashSet<>(entry.getValue());
            String managerStudentsText = emailService.textGenerator(managerStudents);
            String managerEmail = manager.getEmail();
            emailService.sendAttendanceReminderEmail(managerEmail, managerStudentsText);
        }
    }

}
