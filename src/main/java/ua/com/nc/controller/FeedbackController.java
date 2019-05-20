package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.service.FeedbackService;

@Log4j2
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody DtoFeedback dtoFeedback) {
        if (dtoFeedback != null &&
                dtoFeedback.getTeacher() != null &&
                dtoFeedback.getStudentId() != null &&
                dtoFeedback.getCourse() != null &&
                dtoFeedback.getText() != null) {
            feedbackService.add(dtoFeedback);
            return ResponseEntity.ok().body("Feedback saved");
        } else {
            return new ResponseEntity<>(feedbackService.getAllByUserId(dtoFeedback.getStudentId()), HttpStatus.OK);
        }

    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        feedbackService.delete(id);
        return ResponseEntity.ok().body("Feedback removed");
    }

    @RequestMapping(value = "/get-by-user", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByUserId(@RequestParam Integer userId) {
        return new ResponseEntity<>(feedbackService.getAllByUserId(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-by-rainer-and-by-user", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByTrainerIdAndUserId(@RequestParam Integer userId,
                                                        @RequestParam Integer trainerId) {
        return new ResponseEntity<>(feedbackService.getAllByTrainerIdAndUserId(userId, trainerId), HttpStatus.OK);
    }
}
