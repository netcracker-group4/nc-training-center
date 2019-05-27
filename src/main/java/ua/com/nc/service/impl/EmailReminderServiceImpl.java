package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.LessonDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.service.EmailReminderService;
import ua.com.nc.service.EmailService;

import java.util.*;

@Log4j2
@Service
@PropertySource("message_text.properties")
public class EmailReminderServiceImpl implements EmailReminderService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private EmailService emailService;

    private User admin;

    @Value("${title.attendance-reminder-subject}")
    private String attendanceReminderSubjectTemplate;

    @Value("${text.attendance-reminder-text}")
    private String attendanceReminderTextTemplate;

    @Value ("${title.new-infodesk-request}")
    private String newInfodeskRequestSubjectTemplate;

    @Value ("${text.new-infodesk-request}")
    private String newInfodeskRequestTextTemplate;

    @Value("${title.user-attendance-reminder}")
    private String userAttendanceReminderSubject;

    @Value("${text.user-attendance-reminder}")
    private String userAttendanceReminderText;


    @Scheduled(cron = " 30 16 * * 2 *")
    @Override
    public void sendAttendanceReminders () {
        admin = userDao.getAdmin();
        List<Lesson> lessons = lessonDao.getAll();
        for (Lesson lesson : lessons) {
            checkLesson (lesson.getId());
        }
    }

    protected void checkLesson (Integer lessonId) {
        HashMap<User, User> studentsAndManagers = getStudentsAbsentWitNoReason(lessonId);
        Set<User> students = studentsAndManagers.keySet();
        String studentsText = studentsListTextGenerator(students);
        String text = String.format(attendanceReminderTextTemplate,
                                    studentsText);

        sendUserAttendanceReminder (students);

        String adminEmail = admin.getEmail();
        sendEmail(adminEmail, attendanceReminderSubjectTemplate, text);

        User trainer = getLessonTrainer(lessonId);
        String trainerEmail = trainer.getEmail();
        sendEmail(trainerEmail, attendanceReminderSubjectTemplate, text);

        HashMap<User, ArrayList<User>> managersAndStudents = getManagersAndTheirStudents (studentsAndManagers);

        for (HashMap.Entry<User, ArrayList<User>> entry : managersAndStudents.entrySet()) {
            User manager = entry.getKey();
            Set <User> managerStudents = new HashSet<>(entry.getValue());
            String managerStudentsText = studentsListTextGenerator(managerStudents);
            String managerEmail = manager.getEmail();
            text = String.format(attendanceReminderTextTemplate, studentsText);
            sendEmail(managerEmail, managerStudentsText, text);
        }
    }

    @Override
    public void sendUserAttendanceReminder (Set<User> users) {
        for (User user : users) {
            sendEmail(user.getEmail(), userAttendanceReminderSubject, userAttendanceReminderText);
        }
    }

    @Override
    public String studentsListTextGenerator(Set<User> students) {
        String text = "";
        for (User student : students) {
            text += '\n' + student.getFirstName() + ' ' + student.getLastName();
        }
        return text;
    }

    @Override
    public void sendEmail (String to, String subject, String text) {
        DtoMailSender mailSender = new DtoMailSender();
        mailSender.setTo(to);
        mailSender.setSubject(subject);
        mailSender.setText(text);
        emailService.sendSimpleMessage(mailSender);
    }

    public HashMap<User, User> getStudentsAbsentWitNoReason(Integer lessonId) {
        return userDao.getStudentsAbsentWitNoReason(lessonId);
    }

    @Override
    public void sendInfodeskNotification () {
        admin = userDao.getAdmin();
        String adminEmail = admin.getEmail ();
        String subject = newInfodeskRequestSubjectTemplate;
        String text = newInfodeskRequestTextTemplate;
        sendEmail(adminEmail, subject, text);
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

    public User getAdmin() { return admin; }

    public User getLessonTrainer(Integer lessonId) {
        return userDao.getLessonTrainer(lessonId);
    }

}
