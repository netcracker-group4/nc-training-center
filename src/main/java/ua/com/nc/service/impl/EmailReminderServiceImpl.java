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
import ua.com.nc.dto.DtoFeedback;
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

    @Value("${title.message-to-manager}")
    private String titleMessageToManager;
    @Value("${text.message-to-manager}")
    private String textMessageToManager;

    /**
     * Sends an email through emailService
     * @param to - email of receiver
     * @param subject - subject of mail
     * @param text - text of mail
     */
    @Override
    public void sendEmail (String to, String subject, String text) {
        DtoMailSender mailSender = new DtoMailSender();
        mailSender.setTo(to);
        mailSender.setSubject(subject);
        mailSender.setText(text);
        emailService.sendSimpleMessage(mailSender);
    }

    /**
     * Sends notification emails about all students who are stored in database
     * with absence on some lesson without reason.
     * They are sent to admin, trainer of lesson,
     * manager (if there are his students) and every student
     * Scheduled in cron format to do it at 17-00 every Tuesday
     */
    @Scheduled(cron = " 0 17 * * 2 *")
    @Override
    public void sendAttendanceReminders () {
        admin = userDao.getAdmin();
        List<Lesson> lessons = lessonDao.getAll();
        for (Lesson lesson : lessons) {
            checkLesson (lesson.getId());
        }
    }

    /**
     * Checks a specific lesson for students absent without reason
     * and sends all needed notification emails about them
     * @param lessonId - a lesson to check
     */
    protected void checkLesson (Integer lessonId) {
        HashMap<User, User> studentsAndManagers = getStudentsAbsentWitNoReason(lessonId);
        Set<User> students = studentsAndManagers.keySet();
        String studentsText = studentsListTextGenerator(students);
        String text = String.format(attendanceReminderTextTemplate,
                                    studentsText);

        //notifications to students
        sendUserAttendanceReminder (students);

        //notifications to admin
        String adminEmail = admin.getEmail();
        sendEmail(adminEmail, attendanceReminderSubjectTemplate, text);

        //notifications to trainer
        User trainer = getLessonTrainer(lessonId);
        String trainerEmail = trainer.getEmail();
        sendEmail(trainerEmail, attendanceReminderSubjectTemplate, text);

        //notifications to managers
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

    /**
     * Notifications to students
     * @param students - list of students
     */
    @Override
    public void sendUserAttendanceReminder (Set<User> students) {
        for (User user : students) {
            sendEmail(user.getEmail(), userAttendanceReminderSubject, userAttendanceReminderText);
        }
    }

    /**
     * @param students
     * @return - generated text with list of input students
     */
    @Override
    public String studentsListTextGenerator(Set<User> students) {
        String text = "";
        for (User student : students) {
            text += '\n' + student.getFirstName() + ' ' + student.getLastName();
        }
        return text;
    }


    public HashMap<User, User> getStudentsAbsentWitNoReason(Integer lessonId) {
        return userDao.getStudentsAbsentWitNoReason(lessonId);
    }

    /**
     * Sending email notification to admin about new infodesk request
     */
    @Override
    public void sendInfodeskNotification () {
        admin = userDao.getAdmin();
        String adminEmail = admin.getEmail ();
        String subject = newInfodeskRequestSubjectTemplate;
        String text = newInfodeskRequestTextTemplate;
        sendEmail(adminEmail, subject, text);
    }

    @Override
    public void sendMessageToManager(DtoFeedback dtoFeedback) {
        User user = userDao.getManagerByEmployeeId(dtoFeedback.getStudentId());
        User student = userDao.getEntityById(dtoFeedback.getStudentId());
        String title = titleMessageToManager;
        String text = String.format(textMessageToManager,
                student.getFirstName() + " " + student.getLastName(),
                dtoFeedback.getTeacher().getFirstName() + " " + dtoFeedback.getTeacher().getLastName(),
                dtoFeedback.getText());

        String userEmail = user.getEmail();
        sendEmail(userEmail, title, text);
    }

    /**
     * getting a list of all students absent with no reason of every manager
     * @param studentsAndManagers hashmap with a manager of every student
     * @return
     */
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
