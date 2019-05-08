package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.*;
import ua.com.nc.service.EmailService;
import ua.com.nc.service.UserService;

import java.util.List;

@Log4j
@RestController
//@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoUserSave dtoUserSave) {
//        log.debug(user);


        if(     dtoUserSave != null &&
                dtoUserSave.getEmail() != null &&
                dtoUserSave.getFirstName() != null &&
                dtoUserSave.getLastName() != null &&
                dtoUserSave.getPassword() != null &&
                dtoUserSave.getRole() != null){
            userService.add(dtoUserSave);
            return ResponseEntity.ok().body("User saved");
        } else {
            return ResponseEntity.badRequest().body("Incorrectly entered fields");
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody DtoUserProfiles dtoUserProfiles) {
        userService.updateUserByAdmin(dtoUserProfiles);
        return ResponseEntity.ok().body("User updated");
    }

    @RequestMapping(value = "/update-active", method = RequestMethod.PUT)
    public ResponseEntity<?> updateActive(@RequestBody User user) {
        userService.updateActive(user);
        return ResponseEntity.ok().body("Update user active");
    }

    @RequestMapping(value = "/get-all-managers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllManagers() {
        return new ResponseEntity<>(userService.getAllManagers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-all-trainers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTrainers() {
        return new ResponseEntity<>(userService.getAllTrainers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mail-send", method = RequestMethod.POST)
    public ResponseEntity<?> addEmployeeByAdmin(@RequestBody DtoMailSender dtoMailSender) {
        userService.addEmployeeByAdmin(dtoMailSender);
        return ResponseEntity.ok().body("Send mail");
    }

    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> activate(@PathVariable String token) {
        boolean isActivated = userService.activateUser(token);
        return new ResponseEntity<>(isActivated, HttpStatus.OK);
    }
}
