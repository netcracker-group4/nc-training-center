package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.com.nc.domain.MailSender;
import ua.com.nc.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(MailSender mailSender) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailSender.getTo());
        message.setSubject(mailSender.getSubject());
        message.setText(mailSender.getText());

        emailSender.send(message);
    }
}