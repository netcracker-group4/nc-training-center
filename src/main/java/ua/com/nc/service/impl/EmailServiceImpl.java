package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.service.EmailService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Repository
@PropertySource("message_text.properties")
public class EmailServiceImpl implements EmailService {
    @Value("${title.message-to-manager}")
    private String titleMessageToManager;
    @Value("${text.message-to-manager}")
    private String textMessageToManager;
    @Autowired
    private UserDao userDao;
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
    public void sendMessageToManager(DtoFeedback dtoFeedback) {
        User user = userDao.getManagerByEmployeeId(dtoFeedback.getStudentId());
        User student = userDao.getEntityById(dtoFeedback.getStudentId());
        String title = titleMessageToManager;
        String text = String.format(textMessageToManager,
                student.getFirstName() + " " + student.getLastName(),
                dtoFeedback.getTeacher().getFirstName() + " " + dtoFeedback.getTeacher().getLastName(),
                dtoFeedback.getText());

        sendMessage(user, title, text);
    }

    private void sendMessage(User user, String title, String text) {
        DtoMailSender dtoMailSender = new DtoMailSender();
        dtoMailSender.setTo(user.getEmail());
        dtoMailSender.setSubject(title);
        dtoMailSender.setText(text);
        sendSimpleMessage(dtoMailSender);
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