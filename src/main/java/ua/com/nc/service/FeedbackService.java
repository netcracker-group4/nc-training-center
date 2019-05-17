package ua.com.nc.service;

import ua.com.nc.dto.DtoFeedback;

import java.util.List;

public interface FeedbackService {
    void add(DtoFeedback dtoFeedback);
    void delete(Integer id);
    List<DtoFeedback> getAllByUserId(Integer id);
    List<DtoFeedback> getAllByTrainerIdAndUserId(Integer userId, Integer trainerId);
}
