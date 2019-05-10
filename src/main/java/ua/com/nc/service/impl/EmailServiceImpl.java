package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.service.EmailService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Log4j
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
    public HashMap<User, ArrayList<User>> reverseHashMap (HashMap <User, User> studentsAndManagers) {
        HashMap<User, ArrayList<User>> managersAndStudents = new HashMap<>();
        for(HashMap.Entry<User, User> entry : studentsAndManagers.entrySet()){
            User student = entry.getKey();
            User manager = entry.getValue();
            if (managersAndStudents.containsKey(manager)) {
                managersAndStudents.get(manager).add(student);
            } else {
                ArrayList <User> firstStudent = new ArrayList<> ();
                firstStudent.add(student);
                managersAndStudents.put(manager, firstStudent);
            }
        }
        return managersAndStudents;
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