package ua.com.nc.service;

import ua.com.nc.domain.User;

import java.util.TreeMap;

public interface AttendanceReminderService {

    TreeMap<User, User> getStudentsAbsentWitNoReason(int lessonId);

    User getAdmin();

    User getLessonTrainer(int lessonId);
}
