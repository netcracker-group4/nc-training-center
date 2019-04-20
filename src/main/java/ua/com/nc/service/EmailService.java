package ua.com.nc.service;

import ua.com.nc.domain.MailSender;

public interface EmailService {
    void sendSimpleMessage(MailSender mailSender);
}
