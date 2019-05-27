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

import java.util.Set;

@Log4j2
@Repository
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(DtoMailSender dtoMailSender) {
        log.debug("try to send this message" + dtoMailSender);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dtoMailSender.getTo());
        message.setSubject(dtoMailSender.getSubject());
        message.setText(dtoMailSender.getText());
        log.debug("created message");
        emailSender.send(message);
        log.debug("sent");
    }

}