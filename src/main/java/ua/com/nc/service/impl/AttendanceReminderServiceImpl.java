package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.User;
import ua.com.nc.service.AttendanceReminderService;

import java.util.TreeMap;

@Log4j
@Service
public class AttendanceReminderServiceImpl implements AttendanceReminderService {

    @Autowired
    private IUserDao userDao;

    public TreeMap<User, User> getStudentsAbsentWitNoReason(int lessonId) {
        TreeMap<User, User> absentUsersAndTheirManagers = userDao.getStudentsAbsentWitNoReason(lessonId);
        return absentUsersAndTheirManagers;
    }

    public User getAdmin() {
        return userDao.getAdmin();
    }

    public User getLessonTrainer(int lessonId) {
        return userDao.getLessonTrainer(lessonId);
    }

}
