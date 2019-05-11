package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.domain.Feedback;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.dto.DtoUserSave;
import ua.com.nc.service.FeedbackService;

@Log4j
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoFeedback dtoFeedback) {
        if (dtoFeedback != null &&
                dtoFeedback.getTeacher() != null &&
                dtoFeedback.getStudentId() != null &&
                dtoFeedback.getText() != null) {
            feedbackService.add(dtoFeedback);
            return ResponseEntity.ok().body("Feedback saved");
        } else {
            return new ResponseEntity<>(feedbackService.getAllByUserId(dtoFeedback.getStudentId()), HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByUserId(@PathVariable Integer id) {
        return new ResponseEntity<>(feedbackService.getAllByUserId(id), HttpStatus.OK);
    }
}
