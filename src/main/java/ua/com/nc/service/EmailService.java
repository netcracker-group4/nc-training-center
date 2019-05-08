package ua.com.nc.service;

import ua.com.nc.dto.DtoMailSender;

public interface EmailService {
    void sendSimpleMessage(DtoMailSender dtoMailSender);
}
