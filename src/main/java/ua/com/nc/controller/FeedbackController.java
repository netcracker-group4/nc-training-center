package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.service.FeedbackService;

@Log4j2
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * adding feedback to the system
     *
     * @param dtoFeedback   object with id, studentId, teacher, course, text and time
     * @return all feedback
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).TRAINER.name())")
    public ResponseEntity<?> save(@RequestBody DtoFeedback dtoFeedback) {
        feedbackService.add(dtoFeedback);
        return getAllByUserId(dtoFeedback.getStudentId());

    }

    /**
     * delete feedback
     *
     * @param id   id of feedback that will be deleted
     * @return ok status if feedback successful deleted
     */
    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())" +
            "|| hasAuthority(T(ua.com.nc.domain.Role).TRAINER.name())")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        feedbackService.delete(id);
        return ResponseEntity.ok().body("Feedback removed");
    }

    /**
     * get all feedback for one user
     *
     * @param userId   id of user
     * @return all feedback for user
     */
    @RequestMapping(value = "/get-by-user", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByUserId(@RequestParam Integer userId) {
        return new ResponseEntity<>(feedbackService.getAllByUserId(userId), HttpStatus.OK);
    }

    /**
     * get all feedback for one user that give one trainer
     *
     * @param userId      id of user
     * @param trainerId   id of trainer that leave feedback about this user
     * @return all feedback for user by this trainer
     */
    @RequestMapping(value = "/get-by-rainer-and-by-user", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByTrainerIdAndUserId(@RequestParam Integer userId,
                                                        @RequestParam Integer trainerId) {
        return new ResponseEntity<>(feedbackService.getAllByTrainerIdAndUserId(userId, trainerId), HttpStatus.OK);
    }
}
