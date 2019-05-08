package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.com.nc.domain.MailSender;
import ua.com.nc.service.EmailService;

@Component
@PropertySource("classpath:application.properties")
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender javaMailSender;

    public EmailServiceImpl (JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMessage(MailSender mailSender) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(mailSender.getTo());
        mail.setSubject(mailSender.getSubject());
        mail.setText(mailSender.getText());
        mail.setFrom("${spring.mail.username}");
        javaMailSender.send(mail);
    }
}