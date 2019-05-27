package ua.com.nc.service;

import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoFeedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public interface EmailReminderService {

    HashMap<User, User> getStudentsAbsentWitNoReason(Integer lessonId);

    HashMap<User, ArrayList<User>> getManagersAndTheirStudents (HashMap <User, User> studentsAndManagers);

    User getAdmin();

    User getLessonTrainer(Integer lessonId);

    void sendEmail (String to, String subject, String studentsText);

    void sendInfodeskNotification ();

    String studentsListTextGenerator(Set<User> students);

    void sendAttendanceReminders ();

    void sendUserAttendanceReminder (Set<User> users);

    void sendMessageToManager(DtoFeedback dtoFeedback);

}
