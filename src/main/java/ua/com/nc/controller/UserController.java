package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoChangePassword;
import ua.com.nc.dto.DtoUserProfiles;
import ua.com.nc.dto.DtoUserSave;
import ua.com.nc.exceptions.NotFoundException;
import ua.com.nc.service.FileTransferService;
import ua.com.nc.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileTransferService fileTransferService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * returns all users that already registered in the system
     *
     * @return list of users
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    /**
     * adding user to the system
     *
     * @param dtoUserSave   object with firstName, lastName, email, password and role
     * @return saved into database if this user not exist yet and return bad request in case
     * user already exist
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoUserSave dtoUserSave) {
        if (userService.getByEmail(dtoUserSave.getEmail()) != null) {
            return ResponseEntity.badRequest().body("This user already exists");
        } else {
            userService.add(dtoUserSave);
            return ResponseEntity.ok().body("User saved");
        }

    }

    /**
     * returns one user by id
     *
     * @param id    Id of user which need to get
     * @return user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        DtoUserProfiles userById = userService.getById(id);
        if (userById == null)
            throw new NotFoundException("User not found");
        return ResponseEntity.ok(userById);
    }

    /**
     * update user data
     *
     * @param dtoUserProfiles   object with id, firstName, lastName and dtoManager
     * @return ok status if user update
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToUpdateBasicInfo(authentication, #dtoUserProfiles)")
    public ResponseEntity<?> update(@RequestBody DtoUserProfiles dtoUserProfiles) {
        userService.updateUserByAdmin(dtoUserProfiles);
        return ResponseEntity.ok().body("User updated");
    }

    /**
     * update user activity (active if user has been activated by confirming email)
     *
     * @param user   object with id, active
     * @return ok status if update user's active
     */
    @RequestMapping(value = "/update-active", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<?> updateActive(@RequestBody User user) {
        userService.updateActive(user);
        return ResponseEntity.ok().body("Update user active");
    }

    /**
     * update showing status of trainer (in case status equals "true" then it will be "false",
     * in case status equals "false" then it will be "true")
     *
     * @param user   object with id, onLandingPage
     * @return ok status if update user's onLandingPage field
     */
    @RequestMapping(value = "/update-on-landing-page", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<?> updateOnLandingPage(@RequestBody User user) {
        userService.updateOnLandingPage(user);
        return ResponseEntity.ok().body("Update on landing page");
    }

    /**
     * upload user's image
     * save image to the folder on server and save path to this image to data base
     *
     * @param dtoUserSave   object with id, image
     * @return ok status if user's image update
     */
    @RequestMapping(value = "/upload-image", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute DtoUserSave dtoUserSave) {
        return ResponseEntity.ok(userService.uploadImage(dtoUserSave));
    }

    /**
     * download image from server by path
     *
     * @param response   HttpServletResponse
     * @param url   path to folder where image store
     * @return array of byte that represent image
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(final HttpServletResponse response,
                                           @RequestParam String url) {
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (InputStream inputStream = fileTransferService.downloadFileFromServer(url)) {
            if (inputStream != null) {
                byte[] media = IOUtils.toByteArray(inputStream);
                return ResponseEntity.ok().headers(headers).body(media);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().headers(headers).build();
    }

    /**
     * update user password in data base
     *
     * @param dtoChangePassword   userId, oldPassword, newPassword
     * @return ok status if user's password update and
     * return bad status if user's old password is incorrect
     */
    @RequestMapping(value = "/update-password", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePassword(@RequestBody DtoChangePassword dtoChangePassword) {
        User user = userService.getEntityById(dtoChangePassword.getUserId());
        if (passwordEncoder.matches(dtoChangePassword.getOldPassword(), user.getPassword())) {
            userService.updatePassword(dtoChangePassword);
            return ResponseEntity.ok().body("Update password");
        } else {
            return ResponseEntity.badRequest().body("Incorrect password");
        }
    }

    /**
     * send new password to email if this user present and active in data base
     *
     * @param user   id, email
     * @return ok status if user's password is recover and
     * return bad status if user's email don't present in data base or this user don't active
     */
    @RequestMapping(value = "/recover-password", method = RequestMethod.POST)
    public ResponseEntity<?> recoverPassword(@RequestBody User user) {
        user = userService.getByEmail(user.getEmail());
        if (user != null && user.isActive()) {
            userService.recoverPassword(user.getEmail());
            return ResponseEntity.ok().body("Password is recover");
        } else {
            return ResponseEntity.badRequest().body("Invalid email");
        }
    }

    /**
     * get all managers that present in the system
     *
     * @return all managers
     */
    @RequestMapping(value = "/get-all-managers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    /**
     * get all subordinates by manager
     *
     * @param id    id of manager who subordinates gotten
     * @return users that are subordinates of this manager
     */
    @RequestMapping(value = "/{id}/subordinates", method = RequestMethod.GET)
    public ResponseEntity<?> getSubordinatesOfManager(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getSubordinatesOfManager(id));
    }

    /**
     * get all trainers by employee
     *
     * @param employeeId    id of employee
     * @return users that are trainers of this employee
     */
    @RequestMapping(value = "/{employeeId}/trainers", method = RequestMethod.GET)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<?> getTrainersOfEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(userService.getTrainersOfEmployee(employeeId));
    }

    /**
     * get all trainers in the system
     *
     * @return users that are have role "trainer"
     */
    @RequestMapping(value = "/get-all-trainers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTrainers() {
        return ResponseEntity.ok(userService.getAllTrainers());
    }

    /**
     * activate user in the system that allow him to login in the system
     * (change active field in the data base to true)
     *
     * @param token    user's token
     * @return object which include url that redirect ot login page
     */
    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public RedirectView activate(@PathVariable String token) {
        userService.activateUser(token);
        return new RedirectView("/");
    }

    /**
     * get information about employee's attendance
     *
     * @param employeeId    user's id
     * @return attendance graph
     */
    @RequestMapping(value = "{employeeId}/getAttendanceGraph")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<Map<String, Double>> getAttendanceGraph(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(userService.getAttandanceGraph(employeeId));
    }
}
