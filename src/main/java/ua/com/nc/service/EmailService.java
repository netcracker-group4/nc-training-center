package ua.com.nc.service;

import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoMailSender;

import java.util.Set;

public interface EmailService {
    void sendSimpleMessage(DtoMailSender dtoMailSender);

    String textGenerator(Set<User> students);

    void sendAttendanceReminderEmail(String to, String studentsText);
}
