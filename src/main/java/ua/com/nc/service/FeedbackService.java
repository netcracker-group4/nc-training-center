package ua.com.nc.service;

import ua.com.nc.dto.DtoFeedback;

import java.util.List;

public interface FeedbackService {
    /**
     * adding feedback to the system
     *
     * @param dtoFeedback   object with id, studentId, teacher, course, text and time
     * @return all feedback
     */
    void add(DtoFeedback dtoFeedback);

    /**
     * delete feedback
     *
     * @param id   id of feedback that will be deleted
     * @return ok status if feedback successful deleted
     */
    void delete(Integer id);

    /**
     * get all feedback for one user
     *
     * @param id   id of user
     * @return all feedback for user
     */
    List<DtoFeedback> getAllByUserId(Integer id);

    /**
     * get all feedback for one user that give one trainer
     *
     * @param userId      id of user
     * @param trainerId   id of trainer that leave feedback about this user
     * @return all feedback for user by this trainer
     */
    List<DtoFeedback> getAllByTrainerIdAndUserId(Integer userId, Integer trainerId);
}
