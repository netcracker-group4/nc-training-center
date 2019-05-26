package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.User;
import ua.com.nc.service.AttendanceReminderService;
import ua.com.nc.service.EmailService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class AttendanceReminderServiceImpl implements AttendanceReminderService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Override
    public void sendAttendanceReminders (int lessonId) {
        HashMap<User, User> studentsAndManagers = getStudentsAbsentWitNoReason(lessonId);
        Set<User> students = studentsAndManagers.keySet();
        String studentsText = emailService.textGenerator(students);

        User admin = getAdmin();
        String adminEmail = admin.getEmail();
        emailService.sendAttendanceReminderEmail(adminEmail, studentsText);

        User trainer = getLessonTrainer(lessonId);
        String trainerEmail = trainer.getEmail();
        emailService.sendAttendanceReminderEmail(trainerEmail, studentsText);

        HashMap<User, ArrayList<User>> managersAndStudents = getManagersAndTheirStudents (studentsAndManagers);

        for (HashMap.Entry<User, ArrayList<User>> entry : managersAndStudents.entrySet()) {
            User manager = entry.getKey();
            Set <User> managerStudents = new HashSet<>(entry.getValue());
            String managerStudentsText = emailService.textGenerator(managerStudents);
            String managerEmail = manager.getEmail();
            emailService.sendAttendanceReminderEmail(managerEmail, managerStudentsText);
        }
    }

    public HashMap<User, User> getStudentsAbsentWitNoReason(int lessonId) {
        return userDao.getStudentsAbsentWitNoReason(lessonId);
    }

    @Override
    public HashMap<User, ArrayList<User>> getManagersAndTheirStudents (HashMap <User, User> studentsAndManagers) {
        HashMap<User, ArrayList<User>> managersAndStudents = new HashMap<>();
        for(HashMap.Entry<User, User> entry : studentsAndManagers.entrySet()){
            User student = entry.getKey();
            User manager = entry.getValue();
            if (managersAndStudents.containsKey(manager)) {
                managersAndStudents.get(manager).add(student);
            } else {
                ArrayList <User> firstStudent = new ArrayList<> ();
                firstStudent.add(student);
                managersAndStudents.put(manager, firstStudent);
            }
        }
        return managersAndStudents;
    }

    public User getAdmin() {
        return userDao.getAdmin();
    }

    public User getLessonTrainer(int lessonId) {
        return userDao.getLessonTrainer(lessonId);
    }


}
