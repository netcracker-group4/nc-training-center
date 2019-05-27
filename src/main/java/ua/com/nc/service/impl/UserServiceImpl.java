package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Attendance;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.mapper.UserMapper;
import ua.com.nc.service.AttendanceService;
import ua.com.nc.service.EmailService;
import ua.com.nc.service.UserService;

import java.io.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@Service
@PropertySource("message_text.properties")
public class UserServiceImpl implements UserService {
    @Value("${title.message-to-new-employee}")
    private String titleMessageToNewEmployee;
    @Value("${text.message-to-new-employee}")
    private String textMessageToNewEmployee;
    @Value("${title.message-to-new-manager-or-trainer}")
    private String titleMessageToNewManagerOrTrainer;
    @Value("${text.message-to-new-manager-or-trainer}")
    private String textMessageToNewManagerOrTrainer;
    @Value("${title.message-recover-password}")
    private String titleMessageRecoverPassword;
    @Value("${text.message-recover-password}")
    private String textMessageRecoverPassword;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AttendanceStatusDao statusDao;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private FiletransferServiceImpl fileService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void add(DtoUserSave dtoUserSave) {
        User user = userMapper.toModel(dtoUserSave);

        String title;
        String text;

        if (Objects.nonNull(dtoUserSave.getRole())) {
            user.setActive(true);
            userDao.insert(user);
            userDao.addUserRole(user.getId(), dtoUserSave.getRole().name());
            title = titleMessageToNewManagerOrTrainer;
            text = String.format(textMessageToNewManagerOrTrainer,
                    dtoUserSave.getRole(),
                    dtoUserSave.getEmail(),
                    dtoUserSave.getPassword());
        } else {
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userDao.insert(user);
            userDao.addUserRole(user.getId(), Role.EMPLOYEE.name());
            title = titleMessageToNewEmployee;
            text = String.format(textMessageToNewEmployee, user.getToken());
        }

        sendMessage(user, title, text);

    }

    @Async
    void sendMessage(User user, String title, String text) {
        DtoMailSender dtoMailSender = new DtoMailSender();
        dtoMailSender.setTo(user.getEmail());
        dtoMailSender.setSubject(title);
        dtoMailSender.setText(text);
        emailService.sendSimpleMessage(dtoMailSender);
    }

    @Override
    public List<DtoUser> getAll() {
        List<DtoUser> dtoUsers = new ArrayList<>();
        List<User> users = userDao.getAll();
        if (!users.isEmpty()) {
            for (User user : users) {
                List<Role> roles = roleDao.findAllByUserId(user.getId());
                DtoUser dtoUser = new DtoUser(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        roles,
                        user.isActive(),
                        user.getImageUrl());
                dtoUsers.add(dtoUser);
            }
        }
        return dtoUsers;
    }

    @Override
    public DtoUserProfiles getById(Integer id) {
        User user = userDao.getEntityById(id);
        List<Role> role = roleDao.findAllByUserId(id);
        User manager = userDao.getManagerByEmployeeId(id);

        DtoTeacherAndManager dtoManager = null;
        if (manager != null) {
            dtoManager = new DtoTeacherAndManager(manager);
        }

        DtoUserProfiles dtoUserProfiles = null;
        if (user != null) {
            dtoUserProfiles = new DtoUserProfiles(
                    user,
                    role,
                    user.isActive(),
                    dtoManager
            );
        }
        return dtoUserProfiles;
    }

    @Override
    public User getEntityById(Integer id) {
        return userDao.getEntityById(id);
    }


    @Override
    public User getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public void updateUserByAdmin(DtoUserProfiles dtoUserProfiles) {
        User user = userDao.getEntityById(dtoUserProfiles.getId());
        user.setFirstName(dtoUserProfiles.getFirstName());
        user.setLastName(dtoUserProfiles.getLastName());
        if (dtoUserProfiles.getDtoManager() != null) {
            user.setManagerId(dtoUserProfiles.getDtoManager().getId());
        }
        userDao.update(user);
    }

    @Override
    public void updateActive(User user) {
        userDao.updateActive(user);
    }

    @Override
    public List<DtoTeacherAndManager> getAllManagers() {
        List<DtoTeacherAndManager> dtoManagers = new ArrayList<>();
        List<User> managers = userDao.getAllManagers();
        for (User manager : managers) {
            DtoTeacherAndManager dtoTeacherAndManager = new DtoTeacherAndManager(manager);
            dtoManagers.add(dtoTeacherAndManager);
        }
        return dtoManagers;
    }

    @Override
    public List<DtoTeacherAndManager> getAllTrainers() {
        List<DtoTeacherAndManager> dtoTrainers = new ArrayList<>();
        List<User> trainers = userDao.getAllTrainers();
        for (User trainer : trainers) {
            dtoTrainers.add(new DtoTeacherAndManager(trainer));
        }
        return dtoTrainers;
    }

    @Override
    public boolean activateUser(String token) {
        User user = userDao.getByToken(token);

        if (user == null) {
            return false;
        }

        if (timeDifference(user.getCreated()) > 23) {
            return false;
        }

        user.setActive(true);
        userDao.updateActive(user);

        return true;
    }

    private Integer timeDifference(Timestamp userCreated) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long milliseconds = currentTime.getTime() - userCreated.getTime();
        int seconds = (int) milliseconds / 1000;

        int hours = seconds / 3600;

        return hours;
    }

    @Override
    public List<DtoTeacherAndManager> getSubordinatesOfManager(Integer managerId) {
        List<DtoTeacherAndManager> dtoSubordinates = new ArrayList<>();
        List<User> subordinatesOfManager = userDao.getSubordinatesOfManager(managerId);
        for (User subordinate : subordinatesOfManager) {
            DtoTeacherAndManager dtoSubordinate = new DtoTeacherAndManager(subordinate);
            dtoSubordinates.add(dtoSubordinate);
        }
        return dtoSubordinates;
    }

    @Override
    public List<DtoTeacherAndManager> getTrainersOfEmployee(Integer id) {
        List<User> trainers = userDao.getEmployeeTrainersByEmployeeId(id);
        List<DtoTeacherAndManager> dtoTeachers = new ArrayList<>();
        if (trainers != null && trainers.size() != 0) {
            for (User trainer : trainers) {
                DtoTeacherAndManager dtoTrainer = new DtoTeacherAndManager(trainer);
                dtoTeachers.add(dtoTrainer);
            }
        }
        return dtoTeachers;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with such email not exist");
        } else {
            Set<Role> roles = new HashSet<>(roleDao.findAllByUserId(user.getId()));
            user.setRoles(roles);
            return user;
        }
    }

    @Override
    public Map<String, Double> getAttandanceGraph(Integer userId) {
        List<Group> groups = groupDao.getAllGroupsByStudent(userId);
        Map<String, Integer> att = new HashMap<>();
        statusDao.getAll().forEach(r -> {
                    att.put(r.getTitle().toLowerCase(), 0);
                }
        );
        int allLessons = 0;
        for (Group g : groups) {
            List<Attendance> at = attendanceService.getAttendanceByStudentIdAndGroupId(userId, g.getId());
            if (at != null && !at.isEmpty()) {
                for (Attendance a : at) {
                    String status = a.getStatus().toLowerCase();
                    att.put(status, att.get(status) + 1);
                    allLessons++;
                }
            }
        }
        final int a = allLessons;
        Map<String, Double> result = new HashMap<>();
        att.forEach((key, value) -> result.put(key, (value * 100.0) / a));
        return result;
    }

    @Override
    public User uploadImage(DtoUserSave dtoUserSave) {
        String rootDir = "/avatar";
        if (dtoUserSave.getImage().isEmpty())
            throw new LogicException("The file is empty!");
        else {
            try {
                MultipartFile multipartFile = dtoUserSave.getImage();
                String filePath = rootDir;
                saveToDisk(multipartFile, filePath);
                return saveToDatabase(dtoUserSave.getId(), filePath + "/" + multipartFile.getOriginalFilename());
            } catch (Exception e) {
                log.error(e);
                throw new LogicException("Error while uploading file");
            }
        }
    }

    private void saveToDisk(MultipartFile multipartFile, String filePath) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        String fileName = multipartFile.getOriginalFilename();
        log.info("File is not find in base");
        StringBuilder name = new StringBuilder(fileName);
        int dot = name.lastIndexOf(".");
        String format = name.substring(dot + 1);
        String tmpFilePath = getFilePath("tmp." + format);
        File uploadedFile = new File(tmpFilePath);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile))) {
            stream.write(bytes);
        }
        try(FileInputStream stream = new FileInputStream(tmpFilePath)) {
            fileService.uploadFileToServer(filePath, fileName, stream);
        }
        catch (FileNotFoundException e){
            log.error("Error while sending file to server");
            log.error(e);
        }
        File file = new File(tmpFilePath);
        file.delete();
    }

    private String getFilePath(String name) {
        String rootPath = "src/main/resources";
        File dir = new File(rootPath + File.separator + "images" );
        if (!dir.exists()) {
            if(!dir.mkdirs()){
                throw new LogicException("Error, could not upload file" );
            }
        }
        return dir.getAbsolutePath() + File.separator + name;
    }

    private User saveToDatabase(Integer id, String filePath) {
        User user = userDao.getEntityById(id);
        user.setImageUrl(filePath);
        userDao.updateImage(user);
        return user;
    }

    @Override
    public void updatePassword(DtoChangePassword changePassword) {
        User user = new User();
        user.setId(changePassword.getUserId());
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userDao.updatePassword(user);
    }

    @Override
    public void recoverPassword(String email) {
        User user = userDao.getByEmail(email);
        String password = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(password));
        userDao.updatePassword(user);

        DtoMailSender dtoMailSender = new DtoMailSender();
        dtoMailSender.setTo(email);
        dtoMailSender.setSubject(titleMessageRecoverPassword);
        dtoMailSender.setText(String.format(textMessageRecoverPassword, password));
        emailService.sendSimpleMessage(dtoMailSender);

    }

    private String generateRandomPassword() {
        final Random RANDOM = new SecureRandom();
        final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int passwordLength = 10;
        StringBuilder returnValue = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }
}
