package ua.com.nc.service;

import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.dto.DtoMailSender;

import java.util.Set;

public interface EmailService {
    void sendSimpleMessage(DtoMailSender dtoMailSender);
    
    void sendMessageToManager(DtoFeedback dtoFeedback);
}
