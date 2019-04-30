package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoTeacherAndManager;
import ua.com.nc.dto.DtoUser;
import ua.com.nc.dto.DtoUserProfiles;
import ua.com.nc.service.UserService;

import java.util.List;

@Log4j
@RestController
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody User user) {
//        log.debug(user);
        if(     user != null &&
                user.getEmail() != null &&
                user.getFirstName() != null &&
                user.getLastName() != null &&
                user.getPassword() != null){
            userService.add(user);
            return ResponseEntity.ok().body("User saved");
        }else{
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
}
