package ua.com.nc.service;

import ua.com.nc.domain.User;

import java.util.HashMap;
import java.util.TreeMap;

public interface AttendanceReminderService {

    HashMap<User, User> getStudentsAbsentWitNoReason(int lessonId);

    User getAdmin();

    User getLessonTrainer(int lessonId);
}
