package ua.com.nc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.nc.model.User;
import ua.com.nc.service.UserService;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //Save the user
    @PostMapping("/user/add")
    public ResponseEntity<?> save(@RequestBody User user) {
        userService.add(user);
        return ResponseEntity.ok().body("User saved");
    }
}
