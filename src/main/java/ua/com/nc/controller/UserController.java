package ua.com.nc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.MailSender;
import ua.com.nc.domain.User;
import ua.com.nc.service.EmailService;
import ua.com.nc.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class UserController {

    private UserService userService;
    private EmailService emailService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> save(@RequestBody User user) {
        userService.add(user);
        return ResponseEntity.ok().body("User saved");
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody MailSender mailSender) {
        emailService.sendSimpleMessage(mailSender);
        return ResponseEntity.ok().body("Sent request");
    }
}
