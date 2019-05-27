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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoUserSave dtoUserSave) {
        if (userService.getByEmail(dtoUserSave.getEmail()) != null) {
            return ResponseEntity.badRequest().body("This user already exists");
        } else {
            userService.add(dtoUserSave);
            return ResponseEntity.ok().body("User saved");
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        DtoUserProfiles userById = userService.getById(id);
        if (userById == null)
            throw new NotFoundException("User not found");
        return ResponseEntity.ok(userById);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToUpdateBasicInfo(authentication, #dtoUserProfiles)")
    public ResponseEntity<?> update(@RequestBody DtoUserProfiles dtoUserProfiles) {
        userService.updateUserByAdmin(dtoUserProfiles);
        return ResponseEntity.ok().body("User updated");
    }


    @RequestMapping(value = "/update-active", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<?> updateActive(@RequestBody User user) {
        userService.updateActive(user);
        return ResponseEntity.ok().body("Update user active");
    }

    @RequestMapping(value = "/update-on-landing-page", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<?> updateOnLandingPage(@RequestBody User user) {
        userService.updateOnLandingPage(user);
        return ResponseEntity.ok().body("Update on landing page");
    }

    @RequestMapping(value = "/upload-image", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute DtoUserSave dtoUserSave) {
        return ResponseEntity.ok(userService.uploadImage(dtoUserSave));
    }

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
//                return new ResponseEntity<>(media, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().headers(headers).build();
//        return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
    }

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

    @RequestMapping(value = "/get-all-managers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    @RequestMapping(value = "/{id}/subordinates", method = RequestMethod.GET)
    public ResponseEntity<?> getSubordinatesOfManager(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getSubordinatesOfManager(id));
    }

    @RequestMapping(value = "/{employeeId}/trainers", method = RequestMethod.GET)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<?> getTrainersOfEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(userService.getTrainersOfEmployee(employeeId));
    }

    @RequestMapping(value = "/get-all-trainers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTrainers() {
        return ResponseEntity.ok(userService.getAllTrainers());
    }

    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public RedirectView activate(@PathVariable String token) {
        userService.activateUser(token);
        return new RedirectView("/");
    }

    @RequestMapping(value = "{employeeId}/getAttendanceGraph")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<Map<String, Double>> getAttendanceGraph(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(userService.getAttandanceGraph(employeeId));
    }
}
