package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.service.EmailReminderService;

@Log4j2
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailReminderService emailReminderService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoFeedback dtoFeedback) {
        emailReminderService.sendMessageToManager(dtoFeedback);
        return ResponseEntity.ok().body("Message sent");
    }
}
