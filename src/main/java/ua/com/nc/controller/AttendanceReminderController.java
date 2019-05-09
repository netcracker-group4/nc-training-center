package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.service.AttendanceReminderService;
import ua.com.nc.service.EmailService;

import java.util.Set;
import java.util.TreeMap;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
public class AttendanceReminderController {

    @Autowired
    AttendanceReminderService attendanceReminderService;

    @Autowired
    EmailService emailService;

    //    @Value ()
    private String subjectTemplate = "No Reason Students Absence";

    //    @Value ()
    private String textTemplate = "There are students who were absent with no reason: ";

    public void sendAttendanceReminders(@RequestParam(name = "lessonId") int lessonId) {

        TreeMap<User, User> absentUsersAndTheirManagers = attendanceReminderService.getStudentsAbsentWitNoReason(lessonId);
        Set<User> students = absentUsersAndTheirManagers.keySet();

        User admin = attendanceReminderService.getAdmin();
        sendEmail(admin.getEmail(), subjectTemplate, textGenerator(students));

        User trainer = attendanceReminderService.getLessonTrainer(lessonId);
        sendEmail(trainer.getEmail(), subjectTemplate, textGenerator(students));
    }

    private String textGenerator(Set<User> students) {
        String text = textTemplate + '\n';
        for (User student : students) {
            text += student.getFirstName() + ' ' + student.getLastName() + '\n';
        }
        return text;
    }

    private void sendEmail(String to, String subject, String text) {
        DtoMailSender mailSender = new DtoMailSender();
        mailSender.setTo(to);
        mailSender.setSubject(subject);
        mailSender.setText(text);
        emailService.sendSimpleMessage(mailSender);
    }

}
