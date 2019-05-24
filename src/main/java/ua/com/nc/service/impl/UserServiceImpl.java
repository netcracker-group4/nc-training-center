package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Attendance;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.AttendanceService;
import ua.com.nc.service.EmailService;
import ua.com.nc.service.UserService;

import java.io.*;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
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

    @Override
    public void add(DtoUserSave dtoUserSave) {
        User user = new User();
        user.setFirstName(dtoUserSave.getFirstName());
        user.setLastName(dtoUserSave.getLastName());
        user.setPassword(dtoUserSave.getPassword());
        user.setEmail(dtoUserSave.getEmail());
        user.setCreated(OffsetDateTime.now());

        if (dtoUserSave.getRole() != null) {
            user.setActive(true);
            userDao.insert(user);
            userDao.addUserRole(user.getId(), dtoUserSave.getRole().name());
        } else {
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userDao.insert(user);
            userDao.addUserRole(user.getId(), "EMPLOYEE");

            DtoMailSender dtoMailSender = new DtoMailSender();
            dtoMailSender.setTo(user.getEmail());
            dtoMailSender.setSubject("Invite");
            dtoMailSender.setText("http://localhost:8080/api/users/activate/" + token);
            emailService.sendSimpleMessage(dtoMailSender);
        }
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
        user.setManagerId(dtoUserProfiles.getDtoManager().getId());
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

        if (ChronoUnit.HOURS.between(user.getCreated(), OffsetDateTime.now()) > 24) {
            return false;
        }
//        user.setToken(null);
        user.setActive(true);
        userDao.updateActive(user);

        return true;
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

//    public String uploadImage(MultipartFile image) {
//        if (!image.isEmpty()) {
//            try {
//                byte[] bytes = image.getBytes();
//                String name = image.getOriginalFilename();
//                String rootPath = "src/main/resources/avatar/";
//                File dir = new File(rootPath);
//
//                if (!dir.exists()) {
//                    if(!dir.mkdirs()){
//                        return null;
//                    }
//                }
//                String filePath = dir.getAbsolutePath() + File.separator + name;
//                File uploadedFile = new File(filePath);
//                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile))) {
//                    stream.write(bytes);
//                }
//                try(FileInputStream stream = new FileInputStream(filePath)) {
//                    String path = "/avatar/";
//                    filePath = path + name;
//                    fileService.uploadFileToServer(path, name, stream);
//                    if(!uploadedFile.delete()){
//                        log.error("Temp file " + uploadedFile.getName() + " wasn't deleted!");
//                    }
//                    return filePath;
//                }
//            } catch (Exception e) {
//                log.error(e);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void uploadImage(DtoUserSave dtoUserSave) {
//        String filePath = uploadImage(dtoUserSave.getImage());
//        User user = new User();
//        user.setId(dtoUserSave.getId());
//        user.setImageUrl(filePath);
//        userDao.updateImage(user);
//    }

    @Override
    public User uploadImage(DtoUserSave dtoUserSave) {
        String rootDir = "/avatar/";
        if (dtoUserSave.getImage().isEmpty())
            throw new LogicException("The file is empty!");
        else {
            try {
                MultipartFile multipartFile = dtoUserSave.getImage();
                String filePath = rootDir + dtoUserSave.getId().toString();
                saveToDisk(multipartFile, filePath);
                return saveToDatabase(dtoUserSave.getId(), filePath);
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
        String format = name.substring(dot - 1);
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
        user.setPassword(changePassword.getNewPassword());
        userDao.updatePassword(user);
    }

    @Override
    public void recoverPassword(String email) {
        User user = userDao.getByEmail(email);
        String password = generateRandomPassword();
        user.setPassword(password);
        userDao.updatePassword(user);

        DtoMailSender dtoMailSender = new DtoMailSender();
        dtoMailSender.setTo(email);
        dtoMailSender.setSubject("Recover password");
        dtoMailSender.setText("Your new password " + password);
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
