package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(DtoMailSender dtoMailSender) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(dtoMailSender.getTo());
        message.setSubject(dtoMailSender.getSubject());
        message.setText(dtoMailSender.getText());

        emailSender.send(message);
    }
}