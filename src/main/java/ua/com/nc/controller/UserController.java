package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoChangePassword;
import ua.com.nc.dto.DtoUser;
import ua.com.nc.dto.DtoUserProfiles;
import ua.com.nc.dto.DtoUserSave;
import ua.com.nc.service.UserService;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoUserSave dtoUserSave) {
//        log.debug(user);
        if (dtoUserSave != null &&
                dtoUserSave.getEmail() != null &&
                dtoUserSave.getFirstName() != null &&
                dtoUserSave.getLastName() != null &&
                dtoUserSave.getPassword() != null) {
            if (userService.getByEmail(dtoUserSave.getEmail()) != null) {
                return ResponseEntity.ok().body("This user already exists");
            }
            userService.add(dtoUserSave);
            return ResponseEntity.ok().body("User saved");
        } else {
            return ResponseEntity.badRequest().body("Incorrectly entered fields");
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@AuthenticationPrincipal User user,  @PathVariable Integer id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@RequestBody DtoUserProfiles dtoUserProfiles) {
        userService.updateUserByAdmin(dtoUserProfiles);
        return ResponseEntity.ok().body("User updated");
    }


    @RequestMapping(value = "/update-active", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateActive(@RequestBody User user) {
        userService.updateActive(user);
        return ResponseEntity.ok().body("Update user active");
    }

    @RequestMapping(value = "/upload-image", method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@RequestBody DtoUserSave dtoUserSave) {
        userService.uploadImage(dtoUserSave);
        return ResponseEntity.ok().body("Image upload");
    }

    @RequestMapping(value = "/update-password", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePassword(@RequestBody DtoChangePassword dtoChangePassword) {
        User user = userService.getEntityById(dtoChangePassword.getUserId());
        if (user.getPassword().equals(dtoChangePassword.getOldPassword())) {
            userService.updatePassword(dtoChangePassword);
            return ResponseEntity.ok().body("Update password");
        } else {
            return ResponseEntity.badRequest().body("Incorrect password");
        }
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.POST)
    public ResponseEntity<?> recoverPassword(@RequestBody User user) {
        if (userService.getByEmail(user.getEmail()) != null) {
            userService.recoverPassword(user.getEmail());
            return ResponseEntity.ok().body("Password is recover");
        } else {
            return ResponseEntity.badRequest().body("Invalid email");
        }
    }

    @RequestMapping(value = "/get-all-managers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllManagers() {
        return new ResponseEntity<>(userService.getAllManagers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/subordinates", method = RequestMethod.GET)
    public ResponseEntity<?> getSubordinatesOfManager(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getSubordinatesOfManager(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{employeeId}/trainers", method = RequestMethod.GET)
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public ResponseEntity<?> getTrainersOfEmployee(@PathVariable Integer employeeId) {
        return new ResponseEntity<>(userService.getTrainersOfEmployee(employeeId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-all-trainers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTrainers() {
        return new ResponseEntity<>(userService.getAllTrainers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public RedirectView activate(@PathVariable String token) {
        userService.activateUser(token);
        return new RedirectView("/");
    }

    @RequestMapping(value = "{employeeId}/getAttendanceGraph")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToRetrieveGroups(authentication, #employeeId)")
    public Map<String, Double> getAttendanceGraph(@PathVariable Integer employeeId){
        return userService.getAttandanceGraph(employeeId);
    }
}
