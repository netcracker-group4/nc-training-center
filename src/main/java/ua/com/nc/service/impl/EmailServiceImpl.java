package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.service.EmailService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    //    @Value ()
    private String subjectTemplate = "No Reason Students Absence";

    //    @Value ()
    private String textTemplate = "There are students who were absent with no reason: ";


    @Override
    public void sendSimpleMessage(DtoMailSender dtoMailSender) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(dtoMailSender.getTo());
        message.setSubject(dtoMailSender.getSubject());
        message.setText(dtoMailSender.getText());

        emailSender.send(message);
    }

    @Override
    public String textGenerator(Set<User> students) {
        String text = "";
        for (User student : students) {
            text += '\n' + student.getFirstName() + ' ' + student.getLastName();
        }
        return text;
    }

    @Override
    public void sendAttendanceReminderEmail(String to, String studentsText) {
        DtoMailSender mailSender = new DtoMailSender();
        mailSender.setTo(to);
        mailSender.setSubject(subjectTemplate);
        String text = textTemplate + studentsText;
        mailSender.setText(text);
        sendSimpleMessage(mailSender);
    }
}