package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.User;
import ua.com.nc.service.AttendanceReminderService;

import java.util.HashMap;

@Log4j2
@Service
public class AttendanceReminderServiceImpl implements AttendanceReminderService {

    @Autowired
    private UserDao userDao;

    public HashMap<User, User> getStudentsAbsentWitNoReason(int lessonId) {
        return userDao.getStudentsAbsentWitNoReason(lessonId);
    }

    public User getAdmin() {
        return userDao.getAdmin();
    }

    public User getLessonTrainer(int lessonId) {
        return userDao.getLessonTrainer(lessonId);
    }


}
