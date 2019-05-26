package ua.com.nc.service;

import ua.com.nc.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public interface AttendanceReminderService {

    HashMap<User, User> getStudentsAbsentWitNoReason(int lessonId);

    HashMap<User, ArrayList<User>> getManagersAndTheirStudents (HashMap <User, User> studentsAndManagers);

    User getAdmin();

    User getLessonTrainer(int lessonId);

    void sendAttendanceReminders (int lessonId);

}
